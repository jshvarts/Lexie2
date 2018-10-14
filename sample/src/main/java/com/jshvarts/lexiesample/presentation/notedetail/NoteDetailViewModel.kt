package com.jshvarts.lexiesample.presentation.notedetail

import com.jshvarts.lexie.BaseViewModel
import com.jshvarts.lexie.Reducer
import com.jshvarts.lexiesample.domain.NoteDetailUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class NoteDetailViewModel(initialState: State?,
                          private val noteDetailUseCase: NoteDetailUseCase)
    : BaseViewModel<Action, State>() {

    override val initialState = initialState ?: State(isIdle = true)

    private val reducer: Reducer<State, Change> = { state, change ->
        when (change) {
            is Change.Loading -> state.copy(
                    note = null,
                    isIdle = false,
                    isLoading = true,
                    isError = false)
            is Change.NoteDetail -> state.copy(
                    isLoading = false,
                    note = change.note)
            is Change.NoteLoadError -> state.copy(
                    isLoading = false,
                    isError = true)
        }
    }

    init {
        bindActions()
    }

    private fun bindActions() {
        val loadNoteChange = actions.ofType<Action.LoadNoteDetail>()
                .switchMap { action ->
                    noteDetailUseCase.findById(action.noteId)
                            .subscribeOn(Schedulers.io())
                            .toObservable()
                            .map<Change> { Change.NoteDetail(it) }
                            .onErrorReturn { Change.NoteLoadError(it) }
                            .startWith(Change.Loading)
                }

        disposables += loadNoteChange
                .scan(initialState, reducer)
                .filter { !it.isIdle && !it.isLoading }
                .distinctUntilChanged()
                .doOnNext { Timber.d("Received state: $it") }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(state::setValue, Timber::e)
    }
}
