package commands

import java.util.UUID
import com.scalaprog.command.AbstractCommand

case class CreateUserProfile(id: UUID, name: String, password: String, email: String) extends AbstractCommand
