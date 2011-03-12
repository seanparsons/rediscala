package com.github.rediscala

trait SortedSetOperations {
  this: InternalRedisProvider =>

  def zadd(key: Any, score: Double, member: Any) = executeLongResponse(List("ZADD", key, score, member))

  def zcard(key: Any) = executeLongResponse(List("ZCARD", key))

  def zcount(key: Any, min: Double, max: Double) = executeLongResponse(List("ZCOUNT", key, min, max))

  def zincrby(key: Any, increment: Double, member: Any) = executeListResponse(List("ZINCRBY", key, increment, member))

  /*
  TODO: Implement nice way of doing ZINTERSTORE.
  def zinterstore()
  */
  /*
  TODO: Implement nice way of doing ZUNIONSTORE.
  def zunionstore()
  */
  /*
  TODO: Implement nice way of doing ZRANGEBYSCORE.
  def zrangebyscore()
  */
  /*
  TODO: Implement nice way of doing ZREVRANGEBYSCORE.
  def zrevrangebyscore()
  */
  /*
  TODO: Implement nice way of doing ZREMRANGEBYSCORE.
  def zremrangebyscore()
  */

  def zrange(key: Any, start: Long, stop: Long, withScores: Boolean) = {
    executeListResponse(List("ZRANGE", key, start, stop) :: {if (withScores) List("WITHSCORES") else Nil})
  }

  def zrank(key: Any, member: Any) = executeOptionalLongResponse(List("ZRANK", key, member))

  def zrem(key: Any, member: Any) = (executeLongResponse(List("ZREM", key, member)) == 1)

  def zremrangebyrank(key: Any, start: Double, stop: Double) = executeLongResponse(List("ZREMRANGEBYRANK", key, start, stop))

  def zrevrange(key: Any, start: Long, stop: Long, withScores: Boolean) = {
    executeListResponse(List("ZREVRANGE", key, start, stop) :: {if (withScores) List("WITHSCORES") else Nil})
  }

  def zrevrank(key: Any, member: Any) = executeOptionalLongResponse(List("ZREVRANK", key, member))

  def zscore(key: Any, member: Any) = executeStringResponse(List("ZSCORE", key, member))
}