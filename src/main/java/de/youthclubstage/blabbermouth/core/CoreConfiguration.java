package de.youthclubstage.blabbermouth.core;


import de.youthclubstage.blabbermouth.core.streaminterface.EventStream;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@org.springframework.context.annotation.Configuration
@EnableBinding(EventStream.class)
@EnableAutoConfiguration
@ComponentScan(basePackages = {"de.youthclubstage.blabbermouth.core"})
public class CoreConfiguration {

}
