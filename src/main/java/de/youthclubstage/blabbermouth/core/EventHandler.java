package de.youthclubstage.blabbermouth.core;



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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EventHandler {

    Logger LOGGER = LoggerFactory.getLogger(EventHandler.class);
    private HashMap<EventInfo, List<EventMethodInfo>> methods ;

    @Autowired
    ApplicationContext ctx;

    @StreamListener(EventStream.EventInput)
    public void messageListener(@Payload EventMessage message){
        log.error(message.toString());
        if(methods == null)
        {
            buildEventHandler();
        }
        List<EventMethodInfo> eventMethodInfos = methods.get(new EventInfo(message.getProcess(),message.getState()));
        if(eventMethodInfos != null){
            for(EventMethodInfo method : eventMethodInfos){
                try {
                    method.getMethod().invoke(method.getObject(),message);
                } catch (IllegalAccessException e) {
                    LOGGER.error("EventHander",e);
                } catch (InvocationTargetException e) {
                    LOGGER.error("EventHander",e);
                }
            }
        }

    }

    private void buildEventHandler(){
        methods =  new HashMap<>();
        Map<String,Object> beans = ctx.getBeansWithAnnotation(org.springframework.stereotype.Service.class);
        for (Object object: beans.values() ) {
            Class myclass = object.getClass();
            for (Method method : myclass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(EventhandlerMethod.class)) {
                    EventhandlerMethod annotaion = method.getAnnotation(EventhandlerMethod.class);
                    EventInfo key = new EventInfo(annotaion.process(), annotaion.state());
                    List<EventMethodInfo> eventMethodInfos = methods.get(key);
                    if(eventMethodInfos == null){
                        eventMethodInfos = new ArrayList<>();
                        methods.put(key,eventMethodInfos);
                    }
                    eventMethodInfos.add(new EventMethodInfo(myclass,object,method));
                }

            }
        }
    }


}
