package com.github.rediscala

import java.io._
import java.net.Socket
import java.nio.CharBuffer
import scalaz._
import scalaz.Scalaz._

case class RedisConnection(val host: String, val port: Int) extends RedisOperations
                                                            with Closeable {
  def this() = this("localhost", 6379)
  protected val socket = new Socket(host, port)
  protected val outputStream = socket.getOutputStream()
  protected val reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))

  @throws(classOf[IOException])
  def close(): Unit = {
    reader.close()
    outputStream.close()
    socket.close()
  }

  @inline
  protected def readLine(): String = reader.readLine()

  protected def readList(resultNumber: Int): List[Option[String]] = {
    if (resultNumber == 0) List()
    else {
      List.fill(resultNumber)(readString())
    }
  }

  protected def readString(): Option[String] = {
    val lengthLine = readLine()
    val length: Int = if (lengthLine.head == RedisConnection.bulkReplyPrefix) lengthLine.tail.toInt else throw new RuntimeException("Invalid string reply: " + lengthLine)
    readString(length)
  }

  protected def readString(length: Int): Option[String] = {
    if (length == -1) None
    else {
      val charBuffer = CharBuffer.allocate(length)
      reader.read(charBuffer)
      readLine()
      charBuffer.rewind()
      charBuffer.toString().some
    }
  }

  protected def sendRequest(arguments: Seq[Any]): Unit = {
    val stringBuilder = new StringBuilder()
    stringBuilder.append(RedisConnection.argumentNumberPrefix)
    stringBuilder.append(arguments.length)
    stringBuilder.append(RedisConnection.crlf)
    arguments.foreach{ argument =>
      stringBuilder.append(RedisConnection.bulkReplyPrefix)
      val text = argument.toString()
      stringBuilder.append(text.length)
      stringBuilder.append(RedisConnection.crlf)
      stringBuilder.append(text)
      stringBuilder.append(RedisConnection.crlf)
    }
    val builtText = stringBuilder.toString()
    //println(builtText)
    outputStream.write(builtText.getBytes())
  }

  protected def readResponse[T]: Validation[String, T] = {
    val firstLine = readLine()
    //println("firstLine = " + firstLine)
    return (firstLine.head match {
      case RedisConnection.singleLineReplyPrefix => firstLine.tail.success
      case RedisConnection.errorLineReplyPrefix => firstLine.tail.fail
      case RedisConnection.integerReplyPrefix => firstLine.tail.toLong.success
      case RedisConnection.bulkReplyPrefix => readString(firstLine.tail.toInt).success
      case RedisConnection.multiBulkReplyPrefix => readList(firstLine.tail.toInt).success
      case _ => ("Unhandled response:" + firstLine).fail
    }).asInstanceOf[Validation[String, T]]
  }

  protected def handleFailure[T](call: => Validation[String, T]): Validation[String, T] = {
    try {
      call
    }
    catch {
      case throwable: Throwable => throwable.getMessage().fail
    }
  }

  def executeOptionalLongResponse(requestArguments: Seq[Any]) = {
    handleFailure {
      sendRequest(requestArguments)
      readResponse[Any].map{successValue =>
        successValue match {
          case number: Long => number.some
          case bulkReply: String => None
        }
      }
    }
  }

  def executeLongResponse(requestArguments: Seq[Any]) = {
    handleFailure {
      sendRequest(requestArguments)
      readResponse[Long]
    }
  }

  def executeLongReplyBooleanResponse(requestArguments: Seq[Any]) = {
    handleFailure {
      executeLongResponse(requestArguments).map(_ == 1L)
    }
  }

  def executeListResponse(requestArguments: Seq[Any]) = {
    handleFailure {
      sendRequest(requestArguments)
      readResponse[List[Option[String]]]
    }
  }

  def executeStringResponse(requestArguments: Seq[Any]) = {
    handleFailure {
      sendRequest(requestArguments)
      readResponse[Option[String]]
    }
  }

  def executeStatusCodeResponse(requestArguments: Seq[Any]) = {
    handleFailure {
      sendRequest(requestArguments)
      val line = readLine()
      if (line.head == RedisConnection.singleLineReplyPrefix) line.tail.success else ("Invalid status code reply: " + line).fail
    }
  }
}

object RedisConnection {
  val lfChar: Char = '\n'
  val crChar: Char = '\r'
  val crlf = "\r\n"
  val argumentNumberPrefix = '*'
  val multiBulkReplyPrefix = '*'
  val integerReplyPrefix = ':'
  val singleLineReplyPrefix = '+'
  val errorLineReplyPrefix = '-'
  val okReply = "+OK"
  val bulkReplyPrefix = '$'
}
