package com.jshvarts.lexiesample.presentation.notedetail

import com.jshvarts.lexie.BaseAction

sealed class Action : BaseAction {
    data class LoadNoteDetail(val noteId: Long) : Action()
}