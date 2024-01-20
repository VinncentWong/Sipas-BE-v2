package org.example.service;

import org.apache.dubbo.config.annotation.DubboService;
import org.example.constant.ContextConstant;
import org.example.dto.ParentDto;
import org.example.entity.Parent;
import org.example.enums.Role;
import org.example.exception.DataNotFoundException;
import org.example.exception.ForbiddenException;
import org.example.jwt.JwtUtil;
import org.example.repository.ParentRepository;
import org.example.response.HttpResponse;
import org.example.util.BcryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.context.Context;

import java.time.LocalDateTime;

@Service
@DubboService
@Transactional
public class ParentService implements IParentService {

    @Autowired
    private ParentRepository repository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public Parent save(ParentDto.Create dto) {
        var parent = dto.toParent();
        return this
                .repository
                .save(parent);
    }

    @Override
    public HttpResponse login(ParentDto.Login dto) {
        var initialTime = LocalDateTime.now();
        var dtoParent = dto.toParent();
        var parent = this.repository.findOne(
                Example.of(
                        Parent
                                .builder()
                                .email(dtoParent.getEmail())
                                .build()
                )
        ).orElseThrow(() -> new DataNotFoundException("email of parent not found"));

        if(BcryptUtil.isMatch(dtoParent.getPassword(), parent.getPassword())){
            var jwtToken = JwtUtil.generateJwtToken(jwtSecret, Role.PARENT.name(), parent.getId());
            return HttpResponse
                    .sendSuccessResponse(
                            Context.of(ContextConstant.REQUEST_PATH, "/parent/login").put(ContextConstant.TIME_START, initialTime),
                            HttpStatus.OK,
                            "user authenticated",
                            parent,
                            null,
                            jwtToken
                    );
        } else {
            throw new ForbiddenException("credential not valid!");
        }
    }
}
