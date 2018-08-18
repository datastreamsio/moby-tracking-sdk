package io.o2mc.sdk.business.operations;

import io.o2mc.sdk.business.event.EventBus;
import io.o2mc.sdk.domain.Operation;
import io.o2mc.sdk.exceptions.O2MCOperationException;
import io.o2mc.sdk.interfaces.O2MCExceptionNotifier;
import io.o2mc.sdk.util.Util;

/**
 * Manages everything that's related to operations by making use of an OperationsBus.
 */
public class OperationManager {

  private OperationBus operationBus;

  // Will be used for future exception handling, once this class gets more complex
  @SuppressWarnings({ "FieldCanBeLocal", "unused" })
  private O2MCExceptionNotifier notifier;

  public void init(O2MCExceptionNotifier notifier) {
    this.operationBus = new OperationBus();
    this.notifier = notifier;
  }

  /**
   * Generates a new operation and adds it to the OperationBus.
   *
   * @param operationCode code of operation to generate
   */
  public void newOperation(int operationCode) {
    if (!Util.isValidOperationCode(operationCode)) {
      notifier.notifyException(
          new O2MCOperationException(String.format("Operation code '%s' is invalid.", operationCode)),
              false); // is not fatal for base SDK functionality, next event name may be valid again
      return;
    }

    Operation o = operationBus.generateOperation(operationCode);
    operationBus.add(o);
  }

  /**
   * Generates a new operation with additional properties and adds it to the OperationBus.
   *
   * @param operationCode code of operation to generate
   * @param value value to include in the operation
   */
  public void newOperationWithProperties(int operationCode, Object value) {
    if (!Util.isValidOperationCode(operationCode)) {
      notifier.notifyException(
          new O2MCOperationException(String.format("Operation code '%s' is invalid.", operationCode)),
          false); // is not fatal for base SDK functionality, next event name may be valid again
      return;
    }

    if (!Util.isValidOperationValue(value)) {
      notifier.notifyException(
          new O2MCOperationException(String.format("Operation value '%s' is invalid.", operationCode)),
          false); // is not fatal for base SDK functionality, next event name may be valid again
      return;
    }

    Operation o = operationBus.generateOperation(operationCode);
    operationBus.add(o);
  }
}
