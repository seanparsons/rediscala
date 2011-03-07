package com.github.rediscala

import org.specs.mock.Mockito
import org.specs.SpecificationWithJUnit

trait ConnectionSpec extends SpecificationWithJUnit with Mockito {
  def connection: RedisConnection
}