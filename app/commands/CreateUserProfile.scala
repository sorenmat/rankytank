package commands

import java.util.UUID

case class CreateUserProfile(id: UUID, name: String, password: String, email:String)
