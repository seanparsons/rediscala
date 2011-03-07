package com.futurenotfound.rediscala


trait ServerOperations {
  this: InternalRedisProvider =>

  def flushall = executeStatusCodeResponse(List("FLUSHALL"))
}