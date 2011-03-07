package com.futurenotfound.rediscala

trait SetOperations {
  this: InternalRedisProvider =>

  def sadd(key: String, member: String) = executeLongResponse(Array("SADD", key, member))

  def srem(key: String, member: String) = executeLongResponse(Array("SREM", key, member))

  def smembers(key: String) = executeListResponse(Array("SMEMBERS", key))

  def sinter(keys: Seq[String]) = executeListResponse("SINTER" +: keys)

  def sunion(keys: Seq[String]) = executeListResponse("SUNION" +: keys)
}