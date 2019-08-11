package de.youthclubstage.blabbermouth.core.model;

import de.youthclubstage.blabbermouth.core.annotation.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@Data
@EqualsAndHashCode
public class EventInfo {
    private Integer process;
    private Integer state;
    private EventType eventTyp;
}
