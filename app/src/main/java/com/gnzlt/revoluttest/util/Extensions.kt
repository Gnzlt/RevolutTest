package com.gnzlt.revoluttest.util

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}

fun <K, V> LinkedHashMap<K, V>.moveToBottom(key: K) {
    val temp = get(key)
    if (temp != null) {
        remove(key)
        put(key, temp)
    }
}
