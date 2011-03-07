package com.futurenotfound.rediscala

import scalaz._

trait ServerOperationsSpec {
  self: ConnectionSpec =>
  def serverOperations = {
    "provide a flushall method" in {
      connection.flushall must equalTo(Success("OK"))
    }
  }
}
