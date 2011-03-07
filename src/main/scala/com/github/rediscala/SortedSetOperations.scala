package com.github.rediscala

trait SortedSetOperations {
  this: InternalRedisProvider =>

  def zadd(key: String, score: Double, member: String) = executeLongResponse(List("ZADD", key, score, member))

  def zcard(key: String) = executeLongResponse(List("ZCARD", key))

  def zcount(key: String, min: Double, max: Double) = executeLongResponse(List("ZCOUNT", key, min, max))

  def zincrby(key: String, increment: Double, member: String) = executeListResponse(List("ZINCRBY", key, increment, member))

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

  def zrange(key: String, start: Long, stop: Long, withScores: Boolean) = {
    executeListResponse(List("ZRANGE", key, start, stop) :: {if (withScores) List("WITHSCORES") else Nil})
  }

  def zrank(key: String, member: String) = executeOptionalLongResponse(List("ZRANK", key, member))

  def zrem(key: String, member: String) = (executeLongResponse(List("ZREM", key, member)) == 1)

  def zremrangebyrank(key: String, start: Double, stop: Double) = executeLongResponse(List("ZREMRANGEBYRANK", key, start, stop))

  def zrevrange(key: String, start: Long, stop: Long, withScores: Boolean) = {
    executeListResponse(List("ZREVRANGE", key, start, stop) :: {if (withScores) List("WITHSCORES") else Nil})
  }

  def zrevrank(key: String, member: String) = executeOptionalLongResponse(List("ZREVRANK", key, member))

  def zscore(key: String, member: String) = executeStringResponse(List("ZSCORE", key, member))
}