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
    "move method" in {
      "returns true if a key was moved" in {
        connection.set("TEST", "cake")
        connection.move("TEST", 1) must equalTo(Success(true))
      }
      "returns false if no key was moved" in {
        connection.set("TEST", "cake")
        connection.move("TSET", 1) must equalTo(Success(false))
      }
      "moves the value to the specified key" in {
        connection.set("TEST", "cake")
        connection.move("TEST", 1)
        //connection.get("TEST2") must equalTo(Success(Some("cake")))
      }
    }
    "persist method" in {
      "returns false if not removing a timeout" in {
        connection.set("TEST", "cake")
        connection.persist("TEST") must equalTo(Success(false))
      }
      "returns false if the key doesn't exist" in {
        connection.persist("TEST") must equalTo(Success(false))
      }
      "when a ttl is present" in {
        "returns true" in {
          connection.set("TEST", "cake")
          connection.expire("TEST", 100)
          connection.persist("TEST") must equalTo(Success(true))
        }
        "removes the ttl" in {
          connection.set("TEST", "cake")
          connection.expire("TEST", 100)
          connection.persist("TEST")
          connection.ttl("TEST") must equalTo(Success(None))
        }
      }
    }
    "randomkey method" in {
      "returns None when no keys exist" in {
        connection.randomkey must equalTo(Success(None))
      }
      "returns a key when one exists" in {
        connection.set("TEST", "cake")
        connection.randomkey must equalTo(Success(Some("TEST")))
      }
    }
    "rename method" in {
      "when the old key exists" in {
        "renames the original key" in {
          connection.set("TEST", "cake")
          connection.rename("TEST", "TEST2") must equalTo(Success("OK"))
          connection.get("TEST2") must equalTo(Success(Some("cake")))
        }
        "returns an error when the new key is the same as the old one" in {
          connection.set("TEST", "cake")
          connection.rename("TEST", "TEST") must equalTo(Failure("Invalid status code reply: -ERR source and destination objects are the same"))
        }
      }
      "when the old key doesn't exist" in {
        "returns an error" in {
          connection.rename("TEST", "TEST2") must equalTo(Failure("Invalid status code reply: -ERR no such key"))
        }
      }
    }
    "renamenx method" in {
      "when the old key exists" in {
        "renames the original key" in {
          connection.set("TEST", "cake")
          connection.renamenx("TEST", "TEST2") must equalTo(Success(true))
          connection.get("TEST2") must equalTo(Success(Some("cake")))
        }
        "returns false when the the new key already exists" in {
          connection.set("TEST", "cake")
          connection.set("TEST2", "biscuits")
          connection.renamenx("TEST", "TEST2") must equalTo(Success(false))
        }
        "returns an error when the new key is the same as the old one" in {
          connection.set("TEST", "cake")
          connection.renamenx("TEST", "TEST") must equalTo(Failure("ERR source and destination objects are the same"))
        }
      }
      "when the old key doesn't exist" in {
        "returns an error" in {
          connection.renamenx("TEST", "TEST2") must equalTo(Failure("ERR no such key"))
        }
      }
    }
  }
}