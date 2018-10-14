package com.jshvarts.lexiesample.presentation.notelist

import com.jshvarts.lexiesample.domain.Note
import com.jshvarts.lexie.BaseState

data class State(val notes: List<Note> = listOf(),
                 val isIdle: Boolean = false,
                 val isLoading: Boolean = false,
                 val isError: Boolean = false) : BaseState