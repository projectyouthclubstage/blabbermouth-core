package de.youthclubstage.blabbermouth.core.streaminterface;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface EventStream {
    public String EventInput = "input";
    public String EventOutput = "output";

    @Input(EventInput)
    SubscribableChannel inputEvent();

    @Output(EventOutput)
    MessageChannel outputEvent();


}
