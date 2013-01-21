package com.scalaprog.domain

import java.util.UUID

/**
 * User: soren
 */
trait IRepository[T <: AggregateRoot] {

  def save(aggregate: AggregateRoot,  expectedVersion: Int)
  def getById(id: UUID, c: Class[T]) : T
}
