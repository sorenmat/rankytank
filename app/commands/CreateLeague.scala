package commands

import com.scalaprog.command.AbstractCommand
import java.util.UUID

case class CreateLeague(id: UUID, name: String, password: String) extends AbstractCommand
