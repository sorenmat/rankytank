package com.scalaprog.command

/**
 * User: soren
 */
trait CommandHandler {

  def handle(cmd: AbstractCommand)

}
