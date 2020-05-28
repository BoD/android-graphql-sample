package com.example.graphqlsample.core.lifecycle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T> mutableMediatorLiveDataOf(v: T) = MediatorLiveData<T>().apply { value = v }

fun <T> MediatorLiveData<T>.addValueSource(source: LiveData<T>) =
    addSource(source) { this.value = it }
