package it.mltk.kes.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Task {
    private String name;

    Task(final String name) {
        this.name = name;
    }
}