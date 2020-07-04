package com.gnzlt.revoluttest.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import java.util.Timer
import java.util.TimerTask

class LifecycleAwareTimer(
    private val tick: () -> Unit
) : LifecycleObserver {

    companion object {
        private const val RELOAD_RATE = 1000L
    }

    private var timer: Timer? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        timer = Timer().apply {
            scheduleAtFixedRate(
                object : TimerTask() {
                    override fun run() {
                        tick()
                    }
                }, 0, RELOAD_RATE
            )
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        timer?.cancel()
        timer?.purge()
        timer = null
    }
}
