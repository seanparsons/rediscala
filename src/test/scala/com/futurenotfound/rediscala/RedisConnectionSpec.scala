package com.futurenotfound.rediscala

import org.specs.mock.Mockito
import org.specs.SpecificationWithJUnit
import java.io.{InputStreamReader, BufferedReader}
import java.net.Socket

object RedisConnectionSpec extends SpecificationWithJUnit
                           with ConnectionSpec
                           with Mockito
                           with StringOperationsSpec
                           with ServerOperationsSpec {
  val host = "localhost"
  val port = 9786
  val redisAvailable = try {
    val socket = new Socket(host, port)
    val outputStream = socket.getOutputStream()
    val reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
    outputStream.write("*1\r\n$4\r\nPING\r\n".getBytes())
    val line = reader.readLine()
    reader.close()
    outputStream.close()
    line == "+PONG"
  }
  catch {
    case throwable: Throwable => false
  }
  protected def startup() = {
    connection = new RedisConnection(host, port)
    connection.flushall
  }
  protected def shutdown() = {
    connection.close()
    connection = null
  }
  var connection: RedisConnection = null
  "RedisConnection" should {
    if (!redisAvailable) {
      skip("Redis is not available")
    }
    doBefore(startup())
    doAfter(shutdown())
    "must have string commands" in {
      stringOperations
    }
    "must have server commands" in {
      serverOperations
    }
  }
}
