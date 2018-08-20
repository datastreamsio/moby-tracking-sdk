package io.o2mc.sdk.domain

/**
 * Holds information about operations for the backend.
 */
data class Operation(
  val code: Int,
  val remark: Any? // value can be null when sending operations without properties
) {
  companion object {
    const val FORGET_BY_ID = 0
  }
}
