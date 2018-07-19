package io.o2mc.sdk.business.batch;

import android.support.annotation.NonNull;
import io.o2mc.sdk.exceptions.O2MCDispatchException;
import io.o2mc.sdk.interfaces.O2MCCallback;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static io.o2mc.sdk.util.LogUtil.LogE;
import static io.o2mc.sdk.util.LogUtil.LogI;
import static io.o2mc.sdk.util.LogUtil.LogW;

public class BatchCallback implements Callback {

  private static final String TAG = "BatchCallback";

  private O2MCCallback callback;

  BatchCallback(O2MCCallback callback) {
    this.callback = callback;
  }

  @Override
  public void onFailure(@NonNull Call call, @NonNull IOException e) {
    callback.exception(e);
  }

  @Override
  public void onResponse(@NonNull Call call, @NonNull Response response) {
    if (response.isSuccessful()) {
      // Http response indicates success, inform user and SDK
      LogI(TAG, "onResponse: Http response indicates success");
      callback.success();
    } else {
      // Http response indicates failure, inform user and SDK
      LogW(TAG, "onResponse: Http response indicates failure");
      callback.exception(new O2MCDispatchException(
          String.format("Backend HTTP response status code indicated failure. Status was '%s'",
              response.code())));
    }
  }
}
