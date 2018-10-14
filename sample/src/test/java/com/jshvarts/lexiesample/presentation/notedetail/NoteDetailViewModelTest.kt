package com.jshvarts.lexiesample.presentation.notedetail

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.jshvarts.lexiesample.domain.Note
import com.jshvarts.lexiesample.domain.NoteDetailUseCase
import com.jshvarts.lexiesample.presentation.RxTestSchedulerRule
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val NOTE_ID = 1L
private const val NOTE_TEXT = "dummy text"

class NoteDetailViewModelTest {
    @get:Rule val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule val testSchedulerRule = RxTestSchedulerRule()

    private lateinit var testSubject: NoteDetailViewModel

    private val idleState = State(isIdle = true)

    private val noteDetailUseCase = mock<NoteDetailUseCase>()

    private val observer = mock<Observer<State>>()

    @Before fun setUp() {
        testSubject = NoteDetailViewModel(idleState, noteDetailUseCase)
        testSubject.observableState.observeForever(observer)
    }

    @Test fun `Given note successfully loaded, when action LoadNoteDetail is received, then State contains note`() {
        // GIVEN
        val note = Note(NOTE_ID, NOTE_TEXT)
        val successState = State(note)

        whenever(noteDetailUseCase.findById(NOTE_ID)).thenReturn(Single.just(note))

        // WHEN
        testSubject.dispatch(Action.LoadNoteDetail(NOTE_ID))
        testSchedulerRule.triggerActions()

        // THEN
        verify(observer).onChanged(successState)
        verifyNoMoreInteractions(observer)
    }

    @Test fun `Given note failed to load, when action LoadNoteDetail is received, then State contains error`() {
        // GIVEN
        val errorState = State(isError = true)

        whenever(noteDetailUseCase.findById(NOTE_ID)).thenReturn(Single.error(RuntimeException()))

        // WHEN
        testSubject.dispatch(Action.LoadNoteDetail(NOTE_ID))
        testSchedulerRule.triggerActions()

        // THEN
        verify(observer).onChanged(errorState)
        verifyNoMoreInteractions(observer)
    }
}