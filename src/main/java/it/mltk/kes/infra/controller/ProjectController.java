package it.mltk.kes.infra.controller;

import it.mltk.kes.domain.model.Project;
import it.mltk.kes.domain.service.ProjectService;
import it.mltk.kes.infra.controller.model.ProjectModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping( "/projects" )
@Slf4j
public class ProjectController {

    private final ProjectService service;

    public ProjectController(final ProjectService service ) {

        this.service = service;

    }

    @PostMapping( "/" )
    public ResponseEntity createProject(final UriComponentsBuilder uriComponentsBuilder ) {
        log.debug( "createProject : enter" );

        UUID projectUuid = this.service.createProject();

        return ResponseEntity
                .created( uriComponentsBuilder.path( "/projects/{projectUuid}" ).buildAndExpand( projectUuid ).toUri() )
                .build();
    }

    @PatchMapping("/{projectUuid}")
    public ResponseEntity renameProject(@PathVariable("projectUuid") UUID projectUuid, @RequestParam( "name" ) String name, final UriComponentsBuilder uriComponentsBuilder ) {
        log.debug( "renameProject : enter" );

        this.service.renameProject( projectUuid, name );

        return ResponseEntity
                .accepted()
                .build();
    }

    @PostMapping("/{projectUuid}/tasks")
    public ResponseEntity addTaskToProject(@PathVariable( "projectUuid" ) UUID projectUuid, @RequestParam( "name" ) String name, final UriComponentsBuilder uriComponentsBuilder ) {
        log.debug( "addTaskToProject : enter" );

        UUID taskUuid = this.service.addTask( projectUuid, name );

        return ResponseEntity
                .created( uriComponentsBuilder.path( "/projects/{projectUuid}/tasks/{taskUuid}" ).buildAndExpand( projectUuid, taskUuid ).toUri() )
                .build();
    }

    @PutMapping("/{projectUuid}/tasks/{taskUuid}")
    public ResponseEntity renameTaskInProject(@PathVariable( "projectUuid" ) UUID projectUuid, @PathVariable("taskUuid") UUID taskUuid, @RequestParam( "name" ) String name ) {
        log.debug( "renameTaskInProject : enter" );

        this.service.renameTask( projectUuid, taskUuid, name );

        return ResponseEntity
                .accepted()
                .build();
    }

    @DeleteMapping( "/{projectUuid}/tasks/{taskUuid}" )
    public ResponseEntity removeTaskFromProject(@PathVariable( "projectUuid" ) UUID projectUuid, @PathVariable( "taskUuid" ) UUID taskUuid ) {
        log.debug( "removeTaskFromProject : enter" );

        this.service.deleteTask( projectUuid, taskUuid );

        return ResponseEntity
                .accepted()
                .build();
    }

    @GetMapping( "/{projectUuid}" )
    public ResponseEntity project( @PathVariable( "projectUuid" ) UUID projectUuid ) {
        log.debug( "project : enter" );

        Project project = this.service.find( projectUuid );
        log.debug( "project : project=" + project.toString() );

        return ResponseEntity
                .ok( ProjectModel.fromProject( project ) );
    }

}
