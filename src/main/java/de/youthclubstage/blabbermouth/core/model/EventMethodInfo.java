package de.youthclubstage.blabbermouth.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

@Data
@AllArgsConstructor
public class EventMethodInfo {
    private Class myClass;
    private Object object;
    private Method method;

}
