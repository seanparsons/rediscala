package com.github.rediscala

trait SetOperations {
  this: InternalRedisProvider =>

  def sadd(key: Any, member: Any) = executeLongResponse(Array("SADD", key, member))

  def srem(key: Any, member: Any) = executeLongResponse(Array("SREM", key, member))

  def smembers(key: Any) = executeListResponse(Array("SMEMBERS", key))

  def sinter(keys: Seq[Any]) = executeListResponse("SINTER" +: keys)

  def sunion(keys: Seq[Any]) = executeListResponse("SUNION" +: keys)
}