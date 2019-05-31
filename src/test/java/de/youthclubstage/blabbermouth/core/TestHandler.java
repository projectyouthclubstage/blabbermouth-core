package de.youthclubstage.blabbermouth.core;

import de.youthclubstage.blabbermouth.core.annotation.EventhandlerMethod;
import de.youthclubstage.blabbermouth.core.model.EventMessage;
import org.springframework.stereotype.Service;


@Service
public class TestHandler {

    public EventMessage message;

    @EventhandlerMethod(process = 0, state = 2)
    public void test(EventMessage message){
        this.message = message;
    }
}
