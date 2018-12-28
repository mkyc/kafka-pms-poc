package it.mltk.kes.delivery.controller;

import it.mltk.kes.delivery.dto.ProjectDto;
import it.mltk.kes.domain.model.Project;
import it.mltk.kes.domain.service.ProjectService;
import it.mltk.kes.infrastructure.jpa.service.ListableProjectService;
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

    @Autowired
    ListableProjectService listableProjectService;

    @PostMapping("/")
    public ResponseEntity createProject(
            final UriComponentsBuilder uriComponentsBuilder
    ) {
        UUID projectUuid = projectService.initProject();
        return ResponseEntity
                .created(uriComponentsBuilder
                        .path("/projects/{projectUuid}")
                        .buildAndExpand(projectUuid)
                        .toUri())
                .build();
    }

    @PatchMapping("/{projectUuid}")
    public ResponseEntity renameProject(
            @PathVariable("projectUuid") UUID projectUuid,
            @RequestParam("name") String name,
            final UriComponentsBuilder uriComponentsBuilder
    ) {
        projectService.renameProject(projectUuid, name);
        return ResponseEntity
                .accepted()
                .build();
    }

    @PostMapping("/{projectUuid}/tasks")
    public ResponseEntity addTaskToProject(
            @PathVariable("projectUuid") UUID projectUuid,
            @RequestParam("name") String name,
            final UriComponentsBuilder uriComponentsBuilder
    ) {
        UUID taskUuid = projectService.addTask(projectUuid, name);
        return ResponseEntity
                .created(uriComponentsBuilder
                        .path("/projects/{projectUuid}/tasks/{taskUuid}")
                        .buildAndExpand(projectUuid, taskUuid)
                        .toUri())
                .build();
    }

    @DeleteMapping("/{projectUuid}/tasks/{taskUuid}")
    public ResponseEntity removeTaskFromProject(
            @PathVariable("projectUuid") UUID projectUuid,
            @PathVariable("taskUuid") UUID taskUuid
    ) {
        projectService.deleteTask(projectUuid, taskUuid);
        return ResponseEntity
                .accepted()
                .build();
    }

    @PatchMapping("/{projectUuid}/tasks/{taskUuid}")
    public ResponseEntity renameTaskInProject(
            @PathVariable("projectUuid") UUID projectUuid,
            @PathVariable("taskUuid") UUID taskUuid,
            @RequestParam("name") String name
    ) {
        projectService.renameTask(projectUuid, taskUuid, name);
        return ResponseEntity
                .accepted()
                .build();
    }

    @GetMapping("/{projectUuid}")
    public ResponseEntity project(
            @PathVariable("projectUuid") UUID projectUuid
    ) {
        Project project = projectService.find(projectUuid);
        log.debug("project : project=" + project.toString());
        return ResponseEntity
                .ok(ProjectDto.fromProject(project));
    }

    @GetMapping("/")
    public ResponseEntity projects() {
        return ResponseEntity
                .ok(listableProjectService.getAll());
    }
}
