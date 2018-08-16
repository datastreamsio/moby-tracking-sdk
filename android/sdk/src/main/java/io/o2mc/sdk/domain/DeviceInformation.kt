package io.o2mc.sdk.domain

/**
 * Holds information about user's device.
 */
data class DeviceInformation(
  val appId: String,
  val os: String,
  val osVersion: String,
  val locale: String,
  val name: String
)
