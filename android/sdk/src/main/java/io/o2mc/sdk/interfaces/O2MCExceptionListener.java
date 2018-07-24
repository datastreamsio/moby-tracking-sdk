package io.o2mc.sdk.interfaces;

import io.o2mc.sdk.exceptions.O2MCDeviceException;
import io.o2mc.sdk.exceptions.O2MCDispatchException;
import io.o2mc.sdk.exceptions.O2MCEndpointException;

/**
 * Defines which exceptions the SDK can potentially throw.
 */
public interface O2MCExceptionListener {

  /**
   * Occurs on errors related to dispatching a batch.
   *
   * @param e An exception descriptive of what went wrong.
   */
  void onO2MCDispatchException(O2MCDispatchException e);

  /**
   * Occurs on errors related to the user's device.
   *
   * @param e An exception descriptive of what went wrong.
   */
  void onO2MCDeviceException(O2MCDeviceException e);

  /**
   * Occurs on errors related to the supplied endpoint.
   *
   * @param e An exception descriptive of what went wrong.
   */
  void onO2MCEndpointException(O2MCEndpointException e);
}
