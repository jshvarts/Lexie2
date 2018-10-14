package com.jshvarts.lexie

object Lexie {
    internal var loggingEnabled = false
        private set

    fun enableLogging() {
        loggingEnabled = true
    }
}
