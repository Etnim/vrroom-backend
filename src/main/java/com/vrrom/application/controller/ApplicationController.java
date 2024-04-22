package com.vrrom.application.controller;
import com.vrrom.application.dtos.ApplicationResponse;
import com.vrrom.application.dtos.ApplicationRequest;
import com.vrrom.application.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
@Tag(name = "Application Controller", description = "To work with application data")
public class ApplicationController {
    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create application")
    public ApplicationResponse createApplication(@RequestBody ApplicationRequest applicationRequest) {
        return applicationService.createApplication(applicationRequest);

    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse getApplicationById(@PathVariable long id) {
        return applicationService.findApplicationById(id);
    }

    @PutMapping(value="/{id}")
    public ApplicationResponse updateApplicationById(@PathVariable long id, @RequestBody ApplicationRequest applicationRequest) {
        return applicationService.updateApplication(id, applicationRequest);
    }

}
