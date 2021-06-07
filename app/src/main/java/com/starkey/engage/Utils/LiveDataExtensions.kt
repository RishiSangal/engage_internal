package com.starkey.engage.Utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

public fun <T> Fragment.observe(liveData: LiveData<T>?, block: (T) -> Unit) {
    liveData?.observe(viewLifecycleOwner, Observer(block))
}