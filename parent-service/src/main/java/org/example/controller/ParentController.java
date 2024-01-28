package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.constant.ContextConstant;
import org.example.constant.HttpHeaderConstant;
import org.example.dto.ParentDto;
import org.example.entity.Parent;
import org.example.entity.ParentParam;
import org.example.response.HttpResponse;
import org.example.service.IParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.util.context.Context;

import java.time.LocalDateTime;
import java.util.List;

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
                                        this.service.save(dto).getData(),
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
            HttpServletRequest req,
            @RequestBody @Valid ParentDto.Login dto
    ){
        var initialTime = LocalDateTime.now();
        var data = this.service.login(dto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        HttpResponse
                                .sendSuccessResponse(
                                        Context
                                                .of(ContextConstant.TIME_START, initialTime)
                                                .put(ContextConstant.REQUEST_PATH, req.getRequestURI()),
                                        HttpStatus.OK,
                                        "parent authenticated",
                                        data.getData(),
                                        null,
                                        data.getMetadata()
                                )
                );
    }

    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<HttpResponse> get(
            HttpServletRequest req,
            @PathVariable("id") Long id
            ){
        var initialTime = LocalDateTime.now();
        var data = this.service.get(
                ParentParam
                        .builder()
                        .id(id)
                        .build()
        );
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        HttpResponse
                                .sendSuccessResponse(
                                        Context
                                                .of(ContextConstant.TIME_START, initialTime)
                                                .put(ContextConstant.REQUEST_PATH, req.getRequestURI()),
                                        HttpStatus.OK,
                                        "success get parent data",
                                        data.getData(),
                                        null,
                                        data.getMetadata()
                                )
                );


    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<HttpResponse> getList(
            HttpServletRequest req,
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "ids", required = false) List<Long> ids,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset
    ){
        var initialTime = LocalDateTime.now();

        var data = this.service.getList(
                ParentParam
                        .builder()
                        .id(id)
                        .ids(ids)
                        .pgParam(
                                HttpResponse.PaginationParam
                                        .builder()
                                        .limit(limit)
                                        .offset(offset)
                                        .build()
                        )
                        .build()
        );
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        HttpResponse
                                .sendSuccessResponse(
                                        Context
                                                .of(ContextConstant.TIME_START, initialTime)
                                                .put(ContextConstant.REQUEST_PATH, req.getRequestURI()),
                                        HttpStatus.OK,
                                        "success get parent list data",
                                        data.getData(),
                                        data.getPg(),
                                        data.getMetadata()
                                )
                );
    }

    @PutMapping(
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE
            },
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public ResponseEntity<HttpResponse> update(
            HttpServletRequest req,
            @RequestBody Parent updateDto
    ){

        var id = Long.parseLong(
                req.getHeader(HttpHeaderConstant.USER_ID)
        );

        var initialTime = LocalDateTime.now();

        var res = this.service
                .update(
                        ParentParam
                                .builder()
                                .id(id)
                                .build(),
                        updateDto
                );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        HttpResponse.sendSuccessResponse(
                                Context
                                        .of(ContextConstant.TIME_START, initialTime)
                                        .put(ContextConstant.REQUEST_PATH, req.getRequestURI()),
                                HttpStatus.OK,
                                "success update parent",
                                res.getData(),
                                null,
                                null
                        )
                );
    }

    @PatchMapping(
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public ResponseEntity<HttpResponse> activate(
            HttpServletRequest req
    ){

        var initialTime = LocalDateTime.now();

        var id = Long.parseLong(
                req.getHeader(HttpHeaderConstant.USER_ID)
        );

        var res = this.service
                .activate(
                        ParentParam
                                .builder()
                                .id(id)
                                .build()
                );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        HttpResponse.sendSuccessResponse(
                                Context
                                        .of(ContextConstant.TIME_START, initialTime)
                                        .put(ContextConstant.REQUEST_PATH, req.getRequestURI()),
                                HttpStatus.OK,
                                "success activate parent",
                                res.getData(),
                                null,
                                null
                        )
                );
    }

    @DeleteMapping(
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public ResponseEntity<HttpResponse> delete(
            HttpServletRequest req
    ){

        var initialTime = LocalDateTime.now();

        var id = Long.parseLong(
                req.getHeader(HttpHeaderConstant.USER_ID)
        );

        var res = this.service
                .delete(
                        ParentParam
                                .builder()
                                .id(id)
                                .build()
                );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        HttpResponse.sendSuccessResponse(
                                Context
                                        .of(ContextConstant.TIME_START, initialTime)
                                        .put(ContextConstant.REQUEST_PATH, req.getRequestURI()),
                                HttpStatus.OK,
                                "success delete parent",
                                res.getData(),
                                null,
                                null
                        )
                );
    }
}
