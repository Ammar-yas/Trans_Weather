package com.example.transweather.core

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

// Reports logs starting from WARN level and any exception that happens
class FirebaseCrashReportingTree : Timber.Tree() {
    private val crashlytics by lazy { FirebaseCrashlytics.getInstance() }
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        crashlytics.log("${tag ?: "Un specified tag"}: $message")
        if (priority > Log.INFO || t != null) {
            val throwableToRecord = t ?: RuntimeException()
            crashlytics.recordException(throwableToRecord)
        }
    }
}