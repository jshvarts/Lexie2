package com.jshvarts.lexiesample.presentation.notelist

import com.jshvarts.lexie.BaseAction

sealed class Action : BaseAction {
    object LoadNotes : Action()
}