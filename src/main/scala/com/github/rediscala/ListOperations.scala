package com.github.rediscala

trait ListOperations {
  this: InternalRedisProvider =>
  
  def lpush(key: Any, value: Any) = executeLongResponse(Array("LPUSH", key, value))

  def rpush(key: Any, value: Any) = executeLongResponse(Array("RPUSH", key, value))

  def rpop(key: Any) = executeStringResponse(Array("RPOP", key))

  def lpop(key: Any) = executeStringResponse(Array("LPOP", key))

  def lrange(key: Any, start: Long, stop: Long) = executeListResponse(Array("LRANGE", key, start, stop))
  
}
