package io.o2mc.sdk;

public interface O2MCCallback {
  void exception(Exception e);

  void success();
}
