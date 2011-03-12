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
    "provide getrange method" in {
      connection.getrange("TEST", 0, 10) must equalTo(Success(None))
    }
    "provide getset method" in {
      connection.getset("TEST", "cake") must equalTo(Success(None))
    }
    "provide mget method" in {
      connection.mget(List("test1", "test2", "test3")) must equalTo(Success(List(None, None, None)))
    }
    "provide setbit method" in {
      connection.setbit("TEST", 4, true) must equalTo(Success(false))
    }
    "provider getbit method" in {
      connection.getbit("TEST", 5) must equalTo(Success(false))
    }
    "result of getbit method should be the value set in that specific bit" in {
      connection.setbit("TEST", 4, true)
      connection.getbit("TEST", 5) must equalTo(Success(false))
      connection.setbit("TEST", 5, true)
      connection.getbit("TEST", 5) must equalTo(Success(true))
    }
    "result of setbit method is the prior value held in that bit" in {
      connection.setbit("TEST", 4, true) must equalTo(Success(false))
      connection.setbit("TEST", 4, true) must equalTo(Success(true))
      connection.setbit("TEST", 4, false) must equalTo(Success(true))
      connection.setbit("TEST", 4, true) must equalTo(Success(false))
    }
    "result of mget method contains the values for the keys passed in" in {
      "which have values" in {
        connection.set("test1", "7")
        connection.set("test3", "3")
        connection.mget(List("test1", "test2", "test3")) must equalTo(Success(List(Some("7"), None, Some("3"))))
      }
    }
    "result of getset method is the previous value of the key" in {
      connection.getset("TEST", "cake") must equalTo(Success(None))
      connection.getset("TEST", "cake2") must equalTo(Success(Some("cake")))
    }
    "result of getrange method should be the subsection of the value for the given key" in {
      connection.set("TEST", "This is a string")
      connection.getrange("TEST", 0, 3) must equalTo(Success(Some("This")))
      connection.getrange("TEST", -3, -1) must equalTo(Success(Some("ing")))
      connection.getrange("TEST", 0, -1) must equalTo(Success(Some("This is a string")))
      connection.getrange("TEST", 10, 100) must equalTo(Success(Some("string")))
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