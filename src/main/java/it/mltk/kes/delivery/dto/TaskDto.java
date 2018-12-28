package it.mltk.kes.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TaskDto {
    final private UUID taskUuid;
    final private String name;
}
