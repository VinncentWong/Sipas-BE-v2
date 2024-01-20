package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.constant.ContextConstant;
import org.example.dto.ParentDto;
import org.example.response.HttpResponse;
import org.example.service.IParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.util.context.Context;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/parent")
public class ParentController {

    @Autowired
    private IParentService service;

    @PostMapping(
            value = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<HttpResponse> save(
            HttpServletRequest req,
            @RequestBody @Valid ParentDto.Create dto
            ){
        var initialTime = LocalDateTime.now();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        HttpResponse
                                .sendSuccessResponse(
                                        Context
                                                .of(ContextConstant.TIME_START, initialTime)
                                                .put(ContextConstant.REQUEST_PATH, req.getRequestURI()),
                                        HttpStatus.CREATED,
                                        "successfully create parent",
                                        this.service.save(dto),
                                        null,
                                        null
                                )
                );
    }

    @PostMapping(
            value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<HttpResponse> login(
            @RequestBody @Valid ParentDto.Login dto
    ){
        return ResponseEntity
                .ok(this.service.login(dto));
    }
}
