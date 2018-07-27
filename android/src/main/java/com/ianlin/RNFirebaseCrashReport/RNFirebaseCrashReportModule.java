package com.ianlin.RNFirebaseCrashReport;

import android.app.Activity;
import android.os.Bundle;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;


public class RNFirebaseCrashReportModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
    private final static String TAG = RNFirebaseCrashReportModule.class.getCanonicalName();
    private FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getReactApplicationContext());

    public RNFirebaseCrashReportModule(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addLifecycleEventListener(this);
    }

    @Override
    public String getName() {
        return "RNFirebaseCrashReport";
    }

    @ReactMethod
    public void log(String message) {
        FirebaseCrash.log(message);
    }

    @ReactMethod
    public void logcat(int level, String tag, String message) {
        FirebaseCrash.logcat(level, tag, message);
    }

    @ReactMethod
    public void report(String message) {
        FirebaseCrash.report(new Exception(message));
    }

    /**
     * @param eventName screen         SEARCH
     * @param key       screen_name    SEARCH_TERM
     * @param value     Home           010121/rice
     */

    @ReactMethod
    public void setCurrentScreen(final String eventName, final String key, final String value) {
        if (!eventName.equals("") && !key.equals("") && !value.equals("")) {
            Bundle bundle = new Bundle();
            bundle.putString(key, value);
            mFirebaseAnalytics.logEvent(eventName, bundle);
        } else if (!eventName.equals("")) {
            final Activity activity = getCurrentActivity();
            if (activity != null) {
                // needs to be run on main thread
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FirebaseAnalytics.getInstance(getReactApplicationContext()).setCurrentScreen(activity, eventName, null);
                    }
                });
            }
        }
    }

    @Override
    public void onHostResume() {
    }

    @Override
    public void onHostPause() {
    }

    @Override
    public void onHostDestroy() {
    }
}
