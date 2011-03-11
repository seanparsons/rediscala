package com.github.rediscala

trait KeyOperations {
  this: InternalRedisProvider =>

  def del(keys: Seq[String]) = executeLongResponse("DEL" +: keys)

  def exists(key: String) = executeLongReplyBooleanResponse(List("EXISTS", key))

  def expiry(key: String, seconds: Long) = executeLongReplyBooleanResponse(List("EXPIRE", key, seconds))

  def expireat(key: String, timestamp: Long) = executeLongReplyBooleanResponse(List("EXPIREAT", key, timestamp))

  def keys(pattern: String) = executeListResponse(List("KEYS", pattern))

  def move(key: String, database: String) = executeLongReplyBooleanResponse(List("MOVE", key, database))

  def persist(key: String) = executeLongReplyBooleanResponse(List("PERSIST", key))

  def randomkey = executeStringResponse(List("RANDOMKEY"))

  def rename(key: String, newKey: String) = executeStatusCodeResponse(List("RENAME", key, newKey))

  def renamenx(key: String, newKey: String) = executeLongReplyBooleanResponse(List("RENAMENX", key, newKey))

  /* TODO Come up with idiomatic implementation of SORT.
  def sort()
  */

  def ttl(key: String) = executeLongResponse(List("TTL", key))

  def typeOfKey(key: String) = executeStatusCodeResponse(List("TYPE", key))
}
