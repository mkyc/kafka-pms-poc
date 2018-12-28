package it.mltk.kes.domain.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

import static lombok.AccessLevel.NONE;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "eventType",
        defaultImpl = ProjectDomainEventIgnored.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ProjectInitialized.class, name = "ProjectInitialized"),
        @JsonSubTypes.Type(value = ProjectRenamed.class, name = "ProjectRenamed"),
        @JsonSubTypes.Type(value = TaskAdded.class, name = "TaskAdded"),
        @JsonSubTypes.Type(value = TaskDeleted.class, name = "TaskDeleted"),
        @JsonSubTypes.Type(value = TaskRenamed.class, name = "TaskRenamed")
})
@Data
public abstract class ProjectDomainEvent {

    private final UUID projectUuid;

    @Getter(NONE)
    @JsonIgnore
    private final Instant when;

    ProjectDomainEvent(final UUID projectUuid, final Instant when) {
        this.projectUuid = projectUuid;
        this.when = when;
    }

    @JsonProperty("occurredOn")
    public Instant occurredOn() {
        return when;
    }

    @JsonProperty("eventType")
    public abstract String eventType();
}
