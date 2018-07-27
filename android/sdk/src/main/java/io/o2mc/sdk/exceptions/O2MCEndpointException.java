package io.o2mc.sdk.exceptions;

/**
 * Occurs on errors related to the supplied endpoint.
 */
// Suppress unused because the constructors are likely useful for future development.
@SuppressWarnings("unused") public class O2MCEndpointException extends O2MCException {
  public O2MCEndpointException(String message) {
    super(message);
  }

  public O2MCEndpointException(Throwable cause) {
    super(cause);
  }

  public O2MCEndpointException(String message, Throwable cause) {
    super(message, cause);
  }
}
