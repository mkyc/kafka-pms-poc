package it.mltk.kes.delivery.controller;

import it.mltk.kes.domain.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/projects")
@Slf4j
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @PostMapping("/")
    public ResponseEntity createProject(final UriComponentsBuilder uriComponentsBuilder) {
        UUID projectUuid = projectService.initProject();

        return ResponseEntity.created(uriComponentsBuilder.path("/projects/{projectUuid}").buildAndExpand(projectUuid).toUri()).build();
    }

    @PatchMapping("/{projectUuid}")
    public ResponseEntity renameProject(@PathVariable("projectUuid") UUID projectUuid, @RequestParam("name") String name, final UriComponentsBuilder uriComponentsBuilder) {
        projectService.renameProject(projectUuid, name);

        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{projectUuid}/tasks")
    public ResponseEntity addTaskToProject(@PathVariable("projectUuid") UUID projectUuid, @RequestParam("name") String name, final UriComponentsBuilder uriComponentsBuilder) {
        UUID taskUuid = projectService.addTask(projectUuid, name);
        return ResponseEntity
                .created(uriComponentsBuilder.path("/projects/{projectUuid}/tasks/{taskUuid}").buildAndExpand(projectUuid, taskUuid).toUri())
                .build();
    }
}
