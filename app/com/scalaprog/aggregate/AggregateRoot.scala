package aggregate;

import com.scalaprog.events.AbstractEvent
import eventstore.EventStore
import java.util.UUID
import com.scalaprog.engine.Server
;

abstract class AggregateRoot(id: UUID) {

  // initialize the aggregate from the eventstore
  loadFromHistory()

	protected def applyEvent(cmd: AbstractEvent , storeEvent: Boolean)
	
	def loadFromHistory() {
    val events = Server.eventStore.getEvents(id)
		for ( abstractEvent <- events) {
			applyEvent(abstractEvent, false)
		}
	}
}
