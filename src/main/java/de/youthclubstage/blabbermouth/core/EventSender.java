package de.youthclubstage.blabbermouth.core;


import de.youthclubstage.blabbermouth.core.model.EventMessage;
import de.youthclubstage.blabbermouth.core.streaminterface.EventStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

@Service
@Slf4j
public class EventSender {

    @Autowired
    EventStream eventStream;

    public void sendEvent(EventMessage message){
        log.error(message.toString());
        log.error(eventStream.outputEvent().toString());
        eventStream.outputEvent().send(MessageBuilder.withPayload(message)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }
}
