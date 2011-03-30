package com.github.rediscala

import scalaz._

trait ConnectionOperationsSpec {
  self: ConnectionSpec =>
  def connectionOperations = {
    "echo method" in {
      "returns message sent" in {
        connection.echo("A test message") must equalTo(Success(Some("A test message")))
      }
    }
    "select method" in {
      "switches to another database" in {
        connection.select(1) must equalTo(Success("OK"))
      }
    }
  }
}