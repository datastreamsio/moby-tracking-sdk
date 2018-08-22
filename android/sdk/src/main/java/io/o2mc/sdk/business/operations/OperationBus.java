package io.o2mc.sdk.business.operations;

import io.o2mc.sdk.domain.Operation;
import io.o2mc.sdk.util.TimeUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds all operations generated by user interaction from the app implementing our SDK.
 */
public class OperationBus {

  private final List<Operation> operations;

  public OperationBus() {
    operations = new ArrayList<>();
  }

  public void add(Operation o) {
    operations.add(o);
  }

  public void clearOperations() {
    operations.clear();
  }

  public List<Operation> getOperations() {
    return operations;
  }

  public Operation generateOperation(int operationCode) {
    return new Operation(operationCode, null, TimeUtil.generateTimestamp());
  }

  public Operation generateOperationWithProperties(int operationCode, Object properties) {
    return new Operation(operationCode, properties, TimeUtil.generateTimestamp());
  }
}
