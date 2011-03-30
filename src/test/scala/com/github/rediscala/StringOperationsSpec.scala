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
      connection.getrange("TEST", 0, 10) must equalTo(Success(Some("")))
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
    "provide incr method" in {
      connection.incr("TEST") must equalTo(Success(1L))
    }
    "provide incrby method" in {
      connection.incrby("TEST", 10L) must equalTo(Success(10L))
    }
    "provide mset method" in {
      connection.mset(Vector(("TEST1", "9"), ("TEST2", "11"))) must equalTo(Success("OK"))
    }
    "provide msetnx method" in {
      connection.msetnx(Vector(("TEST1", "9"), ("TEST2", "11"))) must equalTo(Success(true))
    }
    "provide setex method" in {
      connection.setex("TEST", 100, "cake") must equalTo(Success("OK"))
    }
    "provide setnx method" in {
      connection.setnx("TEST", "9") must equalTo(Success(true))
    }
    "provide setrange method" in {
      connection.setrange("TEST", 0, "cake") must equalTo(Success(4L))
    }
    "provide strlen method" in {
      connection.strlen("TEST") must equalTo(Success(0L))
    }
    "result of strlen method should be the length of the value set against a key" in {
      connection.set("TEST", "cake")
      connection.strlen("TEST") must equalTo(Success(4L))
    }
    "result of setrange should be correct when setting in the middle of a string" in {
      connection.set("TEST", "cake1cake2")
      connection.setrange("TEST", 5, "cakecakecake") must equalTo(Success(17L))
    }
    "when a key already exists the setnx method should return false" in {
      connection.set("TEST", 8)
      connection.setnx("TEST", "9") must equalTo(Success(false))
    }
    "result of get after setex must be the same" in {
      connection.setex("TEST", 100, "cake") must equalTo(Success("OK"))
      connection.get("TEST") must equalTo(Success(Some("cake")))
    }
    "when a key already exists the msetnx method should return false" in {
      connection.set("TEST1", 8)
      connection.msetnx(Vector(("TEST1", "9"), ("TEST2", "11"))) must equalTo(Success(false))
    }
    "multiple values are set as a result of calling the msetnx method" in {
      connection.msetnx(Vector(("test1", "7"), ("test3", "3")))
      connection.mget(List("test1", "test2", "test3")) must equalTo(Success(List(Some("7"), None, Some("3"))))
    }
    "multiple values are set as a result of calling the mset method" in {
      connection.mset(Vector(("test1", "7"), ("test3", "3")))
      connection.mget(List("test1", "test2", "test3")) must equalTo(Success(List(Some("7"), None, Some("3"))))
    }
    "result of incrby method should be that of the original value plus the specified increment" in {
      connection.set("TEST", "100") must equalTo(Success("OK"))
      connection.incrby("TEST", 60L) must equalTo(Success(160L))
    }
    "result of incr method should be that of the original value plus 1" in {
      connection.set("TEST", "10") must equalTo(Success("OK"))
      connection.incr("TEST") must equalTo(Success(11L))
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
      connection.set("test1", "7")
      connection.set("test3", "3")
      connection.mget(List("test1", "test2", "test3")) must equalTo(Success(List(Some("7"), None, Some("3"))))
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