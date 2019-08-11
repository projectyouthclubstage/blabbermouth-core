package de.youthclubstage.blabbermouth

import de.youthclubstage.blabbermouth.core.App
import de.youthclubstage.blabbermouth.core.EventHandler
import de.youthclubstage.blabbermouth.core.EventSender
import de.youthclubstage.blabbermouth.core.TestHandler
import de.youthclubstage.blabbermouth.core.model.EventMessage
import de.youthclubstage.blabbermouth.core.streaminterface.EventStream
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.messaging.support.MessageBuilder
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.lang.Unroll

@SpringBootTest( classes = App.class)
@EmbeddedKafka(topics = "youthclubstage" ,ports = 9092, partitions = 1, controlledShutdown = true,
        brokerProperties = "listeners=PLAINTEXT://localhost:9092")
class EventhandlerTest extends Specification {

    @Autowired
    TestHandler testHandler

    @Autowired
    EventHandler eventHandler

    @Autowired
    EventSender eventSender


    @Unroll
    def "Teste HandlerMapping"(){
        given:
        testHandler.message = null
        EventMessage message = new EventMessage()
        message.setId(UUID.randomUUID())
        message.process = process
        message.state = state

        def expected = null
        if(positiv)
        {
            expected = message
        }

        when:
        eventHandler.messageListener(message)

        then:
        testHandler.message == expected

        where:
        process|state|positiv
        0|2|true
        1|2|false
        0|3|false
    }

    def "Teste Input über Stream"(){
        testHandler.message = null
        EventMessage message = new EventMessage()
        message.setId(UUID.randomUUID())
        message.process = 0
        message.state = 2

        def expected = message

        when:
        Thread.sleep(3000)
        eventSender.sendEvent(message)


        then:
        Thread.sleep(3000)
        testHandler.message == expected

    }

}