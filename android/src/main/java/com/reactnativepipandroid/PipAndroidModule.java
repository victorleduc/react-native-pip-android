package com.reactnativepipandroid;

import android.app.PictureInPictureParams;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Rational;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;

@ReactModule(name = PipAndroidModule.NAME)
public class PipAndroidModule extends ReactContextBaseJavaModule implements LifecycleEventObserver {
  public static final String NAME = "PipAndroid";
  public static final String PIP_MODE_CHANGE = "PIP_MODE_CHANGE";
  private static DeviceEventManagerModule.RCTDeviceEventEmitter eventEmitter = null;
  private boolean isInPiPMode = false;
  ReactApplicationContext reactApplicationContext;

  public static void pipModeChanged(Boolean isInPictureInPictureMode) {
    eventEmitter.emit(PIP_MODE_CHANGE, isInPictureInPictureMode);
  }

  public PipAndroidModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactApplicationContext = reactContext;
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  @Override
  public void initialize() {
    super.initialize();

    eventEmitter = getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class);
  }

  @ReactMethod
  public void enterPipMode(int width, int height) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      int ratWidth = width > 0 ? width : 380;
      int ratHeight = height > 0 ? height : 214;

      Rational ratio = new Rational(ratWidth, ratHeight);
      PictureInPictureParams.Builder pip_Builder = null;

      pip_Builder = new PictureInPictureParams.Builder();
      pip_Builder.setAspectRatio(ratio).build();
      reactApplicationContext.getCurrentActivity().enterPictureInPictureMode(pip_Builder.build());
    }
  }

  @ReactMethod
  public void registerLifecycleEventObserver() {
    getCurrentActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
        AppCompatActivity activity = (AppCompatActivity) reactApplicationContext.getCurrentActivity();
        if (activity != null) {
          activity.getLifecycle().addObserver(PipAndroidModule.this);
        }
      }
    });

  }

  @Override
  public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      AppCompatActivity activity = (AppCompatActivity) source;

      final Handler handler = new Handler(Looper.getMainLooper());
      handler.postDelayed(new Runnable() {
        @Override
        public void run() {
          boolean isInPiPMode = activity.isInPictureInPictureMode();

          if (PipAndroidModule.this.isInPiPMode != isInPiPMode) {
            PipAndroidModule.this.isInPiPMode = isInPiPMode;
            pipModeChanged(isInPiPMode);
          }
        }
      }, 100);
    }
  }
}
