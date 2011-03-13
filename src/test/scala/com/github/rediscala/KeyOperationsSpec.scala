package com.github.rediscala

import scalaz._
import scalaz.Scalaz._

trait KeyOperationsSpec {
  self: ConnectionSpec =>
  def keyOperations = {
    "delete method removes a key" in {
      connection.set("TEST", "cake")
      connection.del(Vector("TEST")) must equalTo(Success(1L))
      connection.get("TEST") must equalTo(Success(None))
    }
    "exists returns" in {
      "true when a key exists" in {
        connection.set("TEST", "cake")
        connection.exists("TEST") must equalTo(Success(true))
      }
      "false when a key does not exist" in {
        connection.exists("TEST") must equalTo(Success(false))
      }
    }
    "expire method returns" in {
      "true when setting the expiry for a key that exists" in {
        connection.set("TEST", "cake")
        connection.expire("TEST", 10) must equalTo(Success(true))
      }
      "false when setting the expiry for a key that does not exist" in {
        connection.expire("TEST", 10) must equalTo(Success(false))
      }
    }
    "expireat method returns" in {
      "true when setting the expiry for a key that exists" in {
        connection.set("TEST", "cake")
        connection.expireat("TEST", 10) must equalTo(Success(true))
      }
      "false when setting the expiry for a key that does not exist" in {
        connection.expireat("TEST", 10) must equalTo(Success(false))
      }
    }
    "keys returns" in {
      "an empty list for a query that should find nothing" in {
        connection.keys("TEST") must equalTo(Success(List()))
      }
      "a list with a single item for a case that should find just one" in {
        connection.set("TEST", "cake")
        connection.keys("TEST") must equalTo(Success(List(Some("TEST"))))
      }
      "a list with multiple items for a wildcard query" in {
        connection.set("TEST1", "cake")
        connection.set("TEST2", "biscuits")
        connection.keys("TEST[12]") must equalTo(Success(List(Some("TEST1"), Some("TEST2"))))
      }
    }
  }
}