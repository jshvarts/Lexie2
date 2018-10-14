package com.jshvarts.lexiesample.domain

import io.reactivex.Single

class NoteListUseCase {
    fun loadNotes(): Single<List<Note>> =
            Single.just(listOf(Note(1, "note1"), Note(2, "note2")))
}