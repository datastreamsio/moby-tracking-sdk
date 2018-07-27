package io.o2mc.sdk.exceptions;

/**
 * Occurs on errors related to the user's device.
 */
// Suppress unused because the constructors are likely useful for future development.
@SuppressWarnings("unused") public class O2MCDeviceException extends O2MCException {
  public O2MCDeviceException(String message) {
    super(message);
  }

  public O2MCDeviceException(Throwable cause) {
    super(cause);
  }

  public O2MCDeviceException(String message, Throwable cause) {
    super(message, cause);
  }
}
