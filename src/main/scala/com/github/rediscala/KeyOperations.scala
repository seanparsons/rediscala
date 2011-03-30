package com.github.rediscala

trait KeyOperations {
  this: InternalRedisProvider =>

  def del(keys: Seq[Any]) = executeLongResponse("DEL" +: keys)

  def exists(key: Any) = executeLongReplyBooleanResponse(Vector("EXISTS", key))

  def expire(key: Any, seconds: Long) = executeLongReplyBooleanResponse(Vector("EXPIRE", key, seconds))

  def expireat(key: Any, timestamp: Long) = executeLongReplyBooleanResponse(Vector("EXPIREAT", key, timestamp))

  def keys(pattern: Any) = executeListResponse(Vector("KEYS", pattern))

  def move(key: Any, database: Long) = executeLongReplyBooleanResponse(Vector("MOVE", key, database))

  def persist(key: Any) = executeLongReplyBooleanResponse(Vector("PERSIST", key))

  def randomkey = executeStringResponse(Vector("RANDOMKEY"))

  def rename(key: Any, newKey: Any) = executeStatusCodeResponse(Vector("RENAME", key, newKey))

  def renamenx(key: Any, newKey: Any) = executeLongReplyBooleanResponse(Vector("RENAMENX", key, newKey))

  /* TODO Come up with idiomatic implementation of SORT.
  def sort()
  */

  def ttl(key: Any) = executeLongResponse(Vector("TTL", key)).map(ttl => if (ttl == -1) None else Some(ttl))

  def typeOfKey(key: Any) = executeStatusCodeResponse(Vector("TYPE", key))
}
