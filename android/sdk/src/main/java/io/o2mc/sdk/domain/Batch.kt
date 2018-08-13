package io.o2mc.sdk.domain

/**
 * Represents a batch of Events.
 * Essentially contains events and meta data for analytical purposes.
 */
data class Batch(
  val device: DeviceInformation?, // may be null, final (provides only a getter automatically)
  val timestamp: String, // final
  val events: List<Event>, // final
  val number: Int, // final
  val sessionId: String?, // may be null, final
  var retries: Int // not final (additionally provides a setter automatically)
)

