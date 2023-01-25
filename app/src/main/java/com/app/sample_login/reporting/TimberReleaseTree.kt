package com.app.sample_login.reporting

import android.util.Log
//import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class TimberReleaseTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (isLoggable(tag, priority)) {
            if (priority == Log.ASSERT) {
                Log.wtf(tag, message)
            } else {
                // Log to console
                Log.println(priority, tag, message)

                if (t != null) {
                    // Record exceptions to the firebase crashlytics
                   //TODO FirebaseCrashlytics.getInstance().recordException(t)
                }
            }
        }
    }

    override fun isLoggable(tag: String?, priority: Int): Boolean {
        // Don't log VERBOSE, DEBUG and INFO
        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
            return false;
        }

        // Log only ERROR, WARN and WTF
        return true;
    }

}