package it.mltk.kes.domain.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Data
@EqualsAndHashCode( callSuper = true )
@ToString( callSuper = true )
@JsonPropertyOrder({ "eventType", "projectUuid", "occurredOn", "name" })
public class ProjectRenamed extends DomainEvent {

    private final String name;

    @JsonCreator
    public ProjectRenamed(
            @JsonProperty( "name" ) final String name,
            @JsonProperty( "projectUuid" ) final UUID projectUuid,
            @JsonProperty( "occurredOn" ) final Instant when
    ) {
        super( projectUuid, when );

        this.name = name;

    }

    @Override
    @JsonIgnore
    public String eventType() {

        return this.getClass().getSimpleName();
    }

}

