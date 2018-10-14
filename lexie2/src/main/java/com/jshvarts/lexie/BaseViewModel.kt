package com.jshvarts.lexie

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

/**
 * Store which manages business data and state.
 */
abstract class BaseViewModel<A : BaseAction, S : BaseState> : ViewModel() {
    protected val actions: PublishSubject<A> = PublishSubject.create<A>()

    protected val disposables: CompositeDisposable = CompositeDisposable()

    protected abstract val initialState: S

    protected val state = MutableLiveData<S>()

    /**
     * Returns the current state. It is equal to the last value returned by the store's reducer.
     */
    val observableState: LiveData<S>
        get() = state

    /**
     * Dispatches an action. This is the only way to trigger a state change.
     */
    fun dispatch(action: A) {
        println(this::class.java.simpleName + ": Received action: $action")
        actions.onNext(action)
    }

    override fun onCleared() {
        disposables.clear()
    }
}