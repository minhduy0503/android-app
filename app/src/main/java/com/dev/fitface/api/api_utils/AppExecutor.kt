package com.dev.fitface.api.api_utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Created by Dang Minh Duy on 09,May,2021
 */
open class AppExecutor(
        private val diskIO: Executor,
        private val networkIO: Executor,
        private val mainThread: Executor
) {

    constructor() : this(
            Executors.newSingleThreadExecutor(),
            Executors.newFixedThreadPool(3),
            MainThreadExecutor()
    )

    fun diskIO(): Executor {
        return diskIO
    }

    fun networkIO(): Executor {
        return networkIO
    }

    fun mainThread(): Executor {
        return mainThread
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }

    }

}

