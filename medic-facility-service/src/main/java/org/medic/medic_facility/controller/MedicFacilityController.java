package org.medic.medic_facility.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.constant.ContextConstant;
import org.medic.medic_facility.dto.MedicFacilityDto;
import org.medic.medic_facility.service.IMedicFacilityService;
import org.example.response.HttpResponse;
import org.medic.medic_facility.service.MedicFacilityService;
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
@RequestMapping("/medic-facility")
public class MedicFacilityController {

    @Autowired
    private MedicFacilityService service;

    @PostMapping(
            value = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<HttpResponse> save(
            HttpServletRequest req,
            @RequestBody @Valid MedicFacilityDto.Create dto
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
            @RequestBody @Valid MedicFacilityDto.Login dto
    ){
        var initialTime = LocalDateTime.now();
        var res = this.service.login(dto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        HttpResponse
                                .sendSuccessResponse(
                                        Context
                                                .of(ContextConstant.TIME_START, initialTime)
                                                .put(ContextConstant.REQUEST_PATH, req.getRequestURI()),
                                        HttpStatus.CREATED,
                                        "successfully create parent",
                                        res.getData(),
                                        null,
                                        res.getMetadata()
                                )
                );
    }
}
