package it.mltk.kes.infra.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@JsonIgnoreProperties( ignoreUnknown = true )
public class TaskModel {
    private UUID taskUuid;
    private String name;
}
