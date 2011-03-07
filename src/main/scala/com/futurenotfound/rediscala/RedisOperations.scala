package com.futurenotfound.rediscala

import scalaz.Validation

trait RedisOperations extends InternalRedisProvider
                    with KeyOperations
                    with StringOperations
                    with ListOperations
                    with SetOperations
                    with TransactionOperations
                    with ServerOperations

trait InternalRedisProvider {
  def executeLongResponse(requestArguments: Seq[Any]): Validation[String, Long]
  def executeOptionalLongResponse(requestArguments: Seq[Any]): Validation[String, Option[Long]]
  def executeListResponse(requestArguments: Seq[Any]): Validation[String, List[Option[String]]]
  def executeIntReplyBooleanResponse(requestArguments: Seq[Any]): Validation[String, Boolean]
  def executeStringResponse(requestArguments: Seq[Any]): Validation[String, Option[String]]
  def executeStatusCodeResponse(requestArguments: Seq[Any]): Validation[String, String]
}