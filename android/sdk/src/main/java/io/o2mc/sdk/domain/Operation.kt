package io.o2mc.sdk.domain

/**
 * Holds information about operations for the backend.
 */
data class Operation(
  val code: Int,
  val value: Any?, // value can be null when sending operations without properties
  val timestamp: String
) {
  companion object {
    const val FORGET_BY_ID = 0
//    const val GET_DATA_BY_ID = 1 // example
//    const val MODIFY_BY_ID = 2 // example
  }
}
