package com.github.rediscala

import java.io._
import java.net.Socket
import java.nio.CharBuffer
import scalaz._

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

  protected def readList(resultNumber: Int): List[Option[String]] = {
    if (resultNumber == 0) List()
    else {
      List.fill(resultNumber)(readString())
    }
  }

  protected def readString(): Option[String] = {
    val lengthLine = reader.readLine()
    val length: Int = if (lengthLine.head == RedisConnection.bulkReplyPrefix) lengthLine.tail.toInt else throw new RuntimeException("Invalid string reply: " + lengthLine)
    readString(length)
  }

  protected def readString(length: Int): Option[String] = {
    if (length == -1) None
    else {
      val charBuffer = CharBuffer.allocate(length)
      reader.read(charBuffer)
      reader.readLine()
      charBuffer.rewind()
      Some(charBuffer.toString())
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
    outputStream.write(builtText.getBytes())
  }

  protected def readResponse[T]: Validation[String, T] = {
    val firstLine = reader.readLine()
    return (firstLine.head match {
      case RedisConnection.singleLineReplyPrefix => Success(firstLine.tail)
      case RedisConnection.errorLineReplyPrefix => Failure(firstLine.tail)
      case RedisConnection.integerReplyPrefix => Success(firstLine.tail.toLong)
      case RedisConnection.bulkReplyPrefix => Success(readString(firstLine.tail.toInt))
      case RedisConnection.multiBulkReplyPrefix => Success(readList(firstLine.tail.toInt))
      case _ => Failure("Unhandled response:" + firstLine)
    }).asInstanceOf[Validation[String, T]]
  }

  protected def handleFailure[T](call: => Validation[String, T]): Validation[String, T] = {
    try {
      call
    }
    catch {
      case throwable: Throwable => Failure[String, T](throwable.getMessage())
    }
  }

  def executeOptionalLongResponse(requestArguments: Seq[Any]) = {
    handleFailure {
      sendRequest(requestArguments)
      readResponse[Any] match {
        case Success(successValue) => {
          successValue match {
            case number: Long => Success(Some(number))
            case bulkReply: String => Success(None)
            case _ => Failure("Unexpected return: " + successValue)
          }
        }
        case failure => failure.asInstanceOf[Failure[String, Option[Long]]]
      }
    }
  }

  def executeLongResponse(requestArguments: Seq[Any]) = {
    handleFailure {
      sendRequest(requestArguments)
      readResponse[Long]
    }
  }

  def executeIntReplyBooleanResponse(requestArguments: Seq[Any]) = {
    handleFailure {
      executeLongResponse(requestArguments)
      readResponse[Long] match {
        case Success(successValue) => {
          successValue match {
            case number: Long => Success(number == 1L)
            case _ => Failure("Unexpected response: " + successValue)
          }
        }
        case failure => failure.asInstanceOf[Failure[String, Boolean]]
      }
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
      val line = reader.readLine()
      if (line.head == RedisConnection.singleLineReplyPrefix) Success(line.tail) else Failure("Invalid status code reply: " + line)
    }
  }
}

object RedisConnection {
  val crlf = "\r\n"
  val argumentNumberPrefix = '*'
  val multiBulkReplyPrefix = '*'
  val integerReplyPrefix = ':'
  val singleLineReplyPrefix = '+'
  val errorLineReplyPrefix = '-'
  val okReply = "+OK"
  val bulkReplyPrefix = '$'
}
