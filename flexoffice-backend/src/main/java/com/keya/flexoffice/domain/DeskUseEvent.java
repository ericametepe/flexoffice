package com.keya.flexoffice.domain;

import org.springframework.context.ApplicationEvent;

public class DeskUseEvent extends ApplicationEvent {

    public DeskUseEvent(Object source) {
        super(source);
    }
}
