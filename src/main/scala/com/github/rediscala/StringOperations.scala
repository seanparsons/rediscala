package com.github.rediscala


trait StringOperations {
  this: InternalRedisProvider =>

  def set(key: Any, value: Any) = executeStatusCodeResponse(List("SET", key, value))

  def get(key: Any) = executeStringResponse(List("GET", key))

  def append(key: Any, value: Any) = executeLongResponse(List("APPEND", key, value))

  def decr(key: Any) = executeLongResponse(List("DECR", key))

  def decrby(key: Any, decrement: Long) = executeLongResponse(List("DECRBY", key, decrement))

  def getrange(key: Any, start: Long, end: Long) = executeStringResponse(List("GETRANGE", key, start, end))

  def getset(key: Any, value: Any) = executeStringResponse(List("GETSET", key, value))

  def mget(keys: Seq[Any]) = executeListResponse("MGET" +: keys)

  def setbit(key: Any, bit: Int, value: Boolean) = executeLongReplyBooleanResponse(List("SETBIT", key, bit, if (value) 1 else 0))

  def getbit(key: Any, bit: Long) = executeLongReplyBooleanResponse(List("GETBIT", key, bit))

  def incr(key: Any) = executeLongResponse(List("INCR", key))

  def incrby(key: Any, increment: Long) = executeLongResponse(List("INCRBY", key, increment))

  def mset(keyValuePairs: Seq[(Any, Any)]) = executeStatusCodeResponse("MSET" +: keyValuePairs.flatMap(pair => List(pair._1, pair._2)))

  def msetnx(keyValuePairs: Seq[(Any, Any)]) = executeLongReplyBooleanResponse("MSETNX" +: keyValuePairs.flatMap(pair => List(pair._1, pair._2)))

  def setex(key: Any, expirySeconds: Long, value: Any) = executeStatusCodeResponse(List("SETEX", key, expirySeconds, value))

  def setnx(key: Any, value: Any) = executeLongReplyBooleanResponse(List("SETNX", key, value))

  def setrange(key: Any, offset: Long, value: Any) = executeLongResponse(List("SETRANGE", key, offset, value))

  def strlen(key: Any) = executeLongResponse(List("STRLEN", key))
}
