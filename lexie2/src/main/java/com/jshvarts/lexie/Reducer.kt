package com.jshvarts.lexie

typealias Reducer<S, C> = (state: S, change: C) -> S