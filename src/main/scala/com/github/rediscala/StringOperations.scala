package com.github.rediscala


trait StringOperations {
  this: InternalRedisProvider =>

  def set(key: String, value: String) = executeStatusCodeResponse(List("SET", key, value))

  def get(key: String) = executeStringResponse(List("GET", key))

  def append(key: String, value: String) = executeLongResponse(List("APPEND", key, value))

  def decr(key: String) = executeLongResponse(List("DECR", key))

  def decrby(key: String, decrement: Long) = executeLongResponse(List("DECRBY", key, decrement))

  def getrange(key: String, start: Long, end: Long) = executeStringResponse(List("GETRANGE", key, start, end))

  def getset(key: String, value: String) = executeStringResponse(List("GETSET", key, value))

  def mget(keys: Seq[String]) = executeListResponse("MGET" +: keys)

  def setbit(key: String, bit: Int, value: Boolean) = executeLongReplyBooleanResponse(List("SETBIT", key, bit, if (value) 1 else 0))

  def getbit(key: String, bit: Long) = executeLongReplyBooleanResponse(List("GETBIT", key, bit))

  def incr(key: String) = executeLongResponse(List("INCR", key))

  def incrby(key: String, increment: Long) = executeLongResponse(List("INCRBY", key, increment))

  def mset(keyValuePairs: Seq[(String, String)]) = executeStatusCodeResponse("MSET" +: keyValuePairs.flatMap(pair => List(pair._1, pair._2)))
}
