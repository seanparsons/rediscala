package com.github.rediscala

import scalaz._

trait StringOperationsSpec {
  self: ConnectionSpec =>
  def stringOperations = {
    "provide set method" in {
      connection.set("TEST", "cake") must equalTo(Success("OK"))
    }
    "provide get method" in {
      connection.get("TEST") must equalTo(Success(None))
    }
    "provide append method" in {
      connection.append("TEST", "cake") must equalTo(Success(4L))
    }
    "provide decr method" in {
      connection.decr("TEST") must equalTo(Success(-1L))
    }
    "provide decrby method" in {
      connection.decrby("TEST", 10L) must equalTo(Success(-10L))
    }
    "result of decrby method should be that of the original value minus the specified decrement" in {
      connection.set("TEST", "100") must equalTo(Success("OK"))
      connection.decrby("TEST", 60L) must equalTo(Success(40L))
    }
    "result of decr method should be that of the original value minus 1" in {
      connection.set("TEST", "10") must equalTo(Success("OK"))
      connection.decr("TEST") must equalTo(Success(9L))
    }
    "result of get after append should be the appended result" in {
      connection.append("TEST", "cake") must equalTo(Success(4L))
      connection.get("TEST") must equalTo(Success(Some("cake")))
      connection.append("TEST", "CAKE") must equalTo(Success(8L))
      connection.get("TEST") must equalTo(Success(Some("cakeCAKE")))
    }
    "result of get after set must be the same" in {
      connection.set("TEST", "cake") must equalTo(Success("OK"))
      connection.get("TEST") must equalTo(Success(Some("cake")))      
    }
  }
}