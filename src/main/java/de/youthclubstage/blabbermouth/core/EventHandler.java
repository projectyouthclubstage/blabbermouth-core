package de.youthclubstage.blabbermouth.core;


import de.youthclubstage.blabbermouth.core.annotation.EventType;
import de.youthclubstage.blabbermouth.core.annotation.EventhandlerMethod;
import de.youthclubstage.blabbermouth.core.model.EventInfo;
import de.youthclubstage.blabbermouth.core.model.EventMessage;
import de.youthclubstage.blabbermouth.core.model.EventMethodInfo;

import de.youthclubstage.blabbermouth.core.streaminterface.EventStream;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Service
@Slf4j
public class EventHandler {

    Logger LOGGER = LoggerFactory.getLogger(EventHandler.class);
    private HashMap<EventInfo, List<EventMethodInfo>> methods;
    public static int ALL_PROCESSES = -1;
    public static int ALL_STATES = -1;
    private final ApplicationContext ctx;
    private final EventSender eventSender;

    public EventHandler(ApplicationContext ctx,EventSender eventSender){
        this.ctx = ctx;
        this.eventSender = eventSender;
    }




    @StreamListener(EventStream.EventInput)
    public void messageListener(@Payload EventMessage message) {
        log.error(message.toString());
        if (methods == null) {
            buildEventHandler();
        }

        EventType type = message.isRetryMessage() ? EventType.RETRY : EventType.FUNCTION;

        List<EventMethodInfo> eventMethodInfos = getMethodsOrEmptyList(new EventInfo(message.getProcess(), message.getState(), type));
        eventMethodInfos.addAll(getMethodsOrEmptyList(new EventInfo(ALL_PROCESSES, ALL_STATES, type)));
        eventMethodInfos.addAll(getMethodsOrEmptyList(new EventInfo(message.getProcess(), message.getState(), EventType.ALL)));
        eventMethodInfos.addAll(getMethodsOrEmptyList(new EventInfo(ALL_PROCESSES, ALL_STATES, EventType.ALL)));

        if (eventMethodInfos != null) {
            try {
                for (EventMethodInfo method : eventMethodInfos) {
                    EventMessage newMessage = (EventMessage) method.getMethod().invoke(method.getObject(), message);
                    if(newMessage != null)
                    {
                        sendEvent(newMessage,message);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("EventHander", e);
                retry(message);
            }
        }

    }

    private List<EventMethodInfo> getMethodsOrEmptyList(EventInfo eventInfo){
        List<EventMethodInfo> result = methods.get(eventInfo);
        return result != null ? result : new ArrayList<>();
    }

    private void retry(EventMessage message){
        EventMessage newMessage = message.deepClone();
        newMessage.setPreviousMessage(newMessage.getId());
        newMessage.setId(UUID.randomUUID());
        newMessage.setRetryCount(newMessage.getRetryCount() == null ? 1L :(newMessage.getRetryCount()+1L));
        newMessage.setRetryMessage(true);
        eventSender.sendEvent(newMessage);
    }

    private void sendEvent(EventMessage newMessage, EventMessage oldMessage){
        newMessage.setPreviousMessage(oldMessage.getId());
        if(newMessage.getId() == null){
            newMessage.setId(UUID.randomUUID());
        }
        eventSender.sendEvent(newMessage);
    }


    private void buildEventHandler() {
        methods = new HashMap<>();
        Map<String, Object> beans = ctx.getBeansWithAnnotation(org.springframework.stereotype.Service.class);
        for (Object object : beans.values()) {
            Class myclass = object.getClass();
            for (Method method : myclass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(EventhandlerMethod.class)) {
                    EventhandlerMethod annotaion = method.getAnnotation(EventhandlerMethod.class);
                    EventInfo key = new EventInfo(annotaion.process(), annotaion.state(), annotaion.eventTyp());
                    List<EventMethodInfo> eventMethodInfos = methods.get(key);
                    if (eventMethodInfos == null) {
                        eventMethodInfos = new ArrayList<>();
                        methods.put(key, eventMethodInfos);
                    }
                    eventMethodInfos.add(new EventMethodInfo(myclass, object, method));
                }

            }
        }
    }


}
