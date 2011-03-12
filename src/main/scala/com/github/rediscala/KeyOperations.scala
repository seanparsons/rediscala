package com.github.rediscala

trait KeyOperations {
  this: InternalRedisProvider =>

  def del(keys: Seq[Any]) = executeLongResponse("DEL" +: keys)

  def exists(key: Any) = executeLongReplyBooleanResponse(List("EXISTS", key))

  def expiry(key: Any, seconds: Long) = executeLongReplyBooleanResponse(List("EXPIRE", key, seconds))

  def expireat(key: Any, timestamp: Long) = executeLongReplyBooleanResponse(List("EXPIREAT", key, timestamp))

  def keys(pattern: Any) = executeListResponse(List("KEYS", pattern))

  def move(key: Any, database: Any) = executeLongReplyBooleanResponse(List("MOVE", key, database))

  def persist(key: Any) = executeLongReplyBooleanResponse(List("PERSIST", key))

  def randomkey = executeStringResponse(List("RANDOMKEY"))

  def rename(key: Any, newKey: Any) = executeStatusCodeResponse(List("RENAME", key, newKey))

  def renamenx(key: Any, newKey: Any) = executeLongReplyBooleanResponse(List("RENAMENX", key, newKey))

  /* TODO Come up with idiomatic implementation of SORT.
  def sort()
  */

  def ttl(key: Any) = executeLongResponse(List("TTL", key))

  def typeOfKey(key: Any) = executeStatusCodeResponse(List("TYPE", key))
}
