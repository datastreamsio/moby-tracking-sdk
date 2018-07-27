package io.o2mc.sdk.exceptions;

/**
 * Abstract O2MC exception which every O2MC exception should extend from.
 */
// Suppress unused because the constructors are likely useful for future development.
@SuppressWarnings("unused") public abstract class O2MCException extends Exception {
  O2MCException(String message) {
    super(message);
  }

  O2MCException(Throwable cause) {
    super(cause);
  }

  O2MCException(String message, Throwable cause) {
    super(message, cause);
  }
}
