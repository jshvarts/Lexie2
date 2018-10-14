package com.jshvarts.lexiesample.domain

import io.reactivex.Single
import java.lang.IllegalArgumentException

class NoteDetailUseCase {
    fun findById(id: Long): Single<Note> = when(id) {
        1L -> Single.just(Note(1, "note1"))
        2L -> Single.just(Note(2, "note2"))
        else -> Single.error(IllegalArgumentException("Invalid note id passed in"))
    }
}