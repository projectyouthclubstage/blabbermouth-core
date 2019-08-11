package de.youthclubstage.blabbermouth.core.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;
import java.util.UUID;

@Data
public class EventMessage {

    private UUID id;
    private Integer process;
    private Integer state;
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

}
