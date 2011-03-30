package com.github.rediscala


trait ConnectionOperations {
  this: InternalRedisProvider =>

  def select(database: Long) = executeStatusCodeResponse(Vector("SELECT", database))

  def echo(message: Any) = executeStringResponse(Vector("ECHO", message))
}