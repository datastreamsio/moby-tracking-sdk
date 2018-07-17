package io.o2mc.sdk.domain

/**
 * Holds information about tracking events.
 */
data class Event(
  val name: String,
  val value: Any,
  val timestamp: String
)
