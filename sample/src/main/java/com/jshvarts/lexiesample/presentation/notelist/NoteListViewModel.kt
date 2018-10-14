package com.jshvarts.lexiesample.presentation.notelist

import com.jshvarts.lexie.BaseViewModel
import com.jshvarts.lexie.Reducer
import com.jshvarts.lexiesample.domain.NoteListUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class NoteListViewModel(initialState: State?,
                        private val loadNoteListUseCase: NoteListUseCase)
    : BaseViewModel<Action, State>() {

    override val initialState = initialState ?: State(isIdle = true)

    private val reducer: Reducer<State, Change> = { state, change ->
        when (change) {
            is Change.Loading -> state.copy(
                    isIdle = false,
                    isLoading = true,
                    notes = emptyList(),
                    isError = false)
            is Change.Notes -> state.copy(
                    isLoading = false,
                    notes = change.notes)
            is Change.Error -> state.copy(
                    isLoading = false,
                    isError = true)
        }
    }

    init {
        bindActions()
    }

    private fun bindActions() {
        val loadNotesChange = actions.ofType<Action.LoadNotes>()
                .switchMap {
                    loadNoteListUseCase.loadNotes()
                            .subscribeOn(Schedulers.io())
                            .toObservable()
                            .map<Change> { Change.Notes(it) }
                            .defaultIfEmpty(Change.Notes(emptyList()))
                            .onErrorReturn { Change.Error(it) }
                            .startWith(Change.Loading)
                }

        // to handle multiple Changes, use Observable.merge to merge them into a single stream:
        // val allChanges = Observable.merge(loadNotesChange, ...)

        disposables += loadNotesChange
                .scan(initialState, reducer)
                .filter { !it.isIdle }
                .distinctUntilChanged()
                .doOnNext { Timber.d("Received state: $it") }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(state::setValue, Timber::e)
    }
}
