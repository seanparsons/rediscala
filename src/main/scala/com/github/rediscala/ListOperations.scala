package com.github.rediscala

trait ListOperations {
  this: InternalRedisProvider =>
  
  def lpush(key: String, value: String) = executeLongResponse(Array("LPUSH", key, value))

  def rpush(key: String, value: String) = executeLongResponse(Array("RPUSH", key, value))

  def rpop(key: String) = executeStringResponse(Array("RPOP", key))

  def lpop(key: String) = executeStringResponse(Array("LPOP", key))

  def lrange(key: String, start: Long, stop: Long) = executeListResponse(Array("LRANGE", key, start, stop))
  
}
