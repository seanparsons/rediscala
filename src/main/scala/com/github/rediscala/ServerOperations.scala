package com.github.rediscala


trait ServerOperations {
  this: InternalRedisProvider =>

  def flushall = executeStatusCodeResponse(Vector("FLUSHALL"))
}