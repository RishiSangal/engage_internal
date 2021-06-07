package com.starkey.engage.view.model

import com.zhuinden.eventemitter.EventEmitter
import com.zhuinden.eventemitter.EventSource

public abstract class EventDispatcher<Event> {

    private val eventEmitter: EventEmitter<Event> = EventEmitter()
    public val events: EventSource<Event> get() = eventEmitter

    public fun emit(event: Event) {
        eventEmitter.emit(event)
    }

}
