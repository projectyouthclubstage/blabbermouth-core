package de.youthclubstage.blabbermouth.core.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventMessage {

    private UUID id;
    private Integer process;
    private Integer state;
    private String application;
    private String content;
    private Long version;
    private Long retryCount = 0L;
    private boolean isRetryMessage = false;
    private UUID previousMessage;

    public Object getContextAs(Class returnClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(content,returnClass);
    }

    public void setContextFrom(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        content = mapper.writeValueAsString(object);
    }

    public EventMessage deepClone(){
        return new EventMessage(id,process,state,application,content,version,retryCount,isRetryMessage,previousMessage);
    }

}
