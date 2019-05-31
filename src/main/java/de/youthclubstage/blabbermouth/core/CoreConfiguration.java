package de.youthclubstage.blabbermouth.core;


import de.youthclubstage.blabbermouth.core.streaminterface.EventStream;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.stereotype.Service;

@org.springframework.context.annotation.Configuration
@Service
@EnableBinding(EventStream.class)
@EnableAutoConfiguration
public class CoreConfiguration {
}
