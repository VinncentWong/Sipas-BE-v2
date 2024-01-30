package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.dto.ParentDto;
import org.example.dubbo.ParentMedic;
import org.example.dubbo.ParentMedicFacility;
import org.example.entity.Parent;
import org.example.entity.ParentParam;
import org.example.enums.Role;
import org.example.exception.DataAlreadyExistException;
import org.example.exception.DataNotFoundException;
import org.example.exception.ForbiddenException;
import org.example.jwt.JwtUtil;
import org.example.repository.IRepository;
import org.example.repository.ParentRepository;
import org.example.response.ServiceData;
import org.example.util.BcryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@DubboService
@Transactional
@Slf4j
public class ParentService implements IParentService {

    @Autowired
    private ParentRepository repository;

    @DubboReference
    private ParentMedic parentMedicStub;

    @Autowired
    private IRepository iRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public ServiceData<Parent> save(ParentDto.Create dto) {

        log.info("catch dto save: {}", dto);

        // check parent email first(should be unique)
        var optPerson = this.repository
                .findOne(
                        Example.of(
                                Parent
                                        .builder()
                                        .email(dto.email())
                                        .build()
                        )
                );
        if(optPerson.isPresent()){
            throw new DataAlreadyExistException("parent email already exist");
        }

        var parent = dto.toParent();
        parent.setIsActive(true);
        var data = this
                .repository
                .save(parent);

        return ServiceData
                .<Parent>builder()
                .data(data)
                .build();
    }

    @Override
    public ServiceData<Parent> login(ParentDto.Login dto) {

        log.info("catch dto login: {}", dto);
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
            return ServiceData
                    .<Parent>builder()
                    .data(parent)
                    .metadata(jwtToken)
                    .build();
        } else {
            throw new ForbiddenException("credential not valid!");
        }
    }

    @Override
    public ServiceData<Parent> get(ParentParam param) {

        log.info("catch get param: {}", param);

        var res = this.iRepository
                .get(param);

        return ServiceData
                .<Parent>builder()
                .data(res.getData())
                .build();
    }

    @Override
    public ServiceData<List<Parent>> getList(ParentParam param) {

        log.info("catch get param: {}", param);

        var res = this.iRepository
                .getList(param);

        log.info("pg result: {}", res.getPg());

        return ServiceData
                .<List<Parent>>builder()
                .data(res.getData())
                .pg(res.getPg())
                .build();
    }

    @Override
    public ServiceData<Parent> update(ParentParam param, Parent parent) {

        log.info("catch get list param: {}", param);
        log.info("catch parent: {}", parent);

        var res = this
                .iRepository
                .update(param, parent);

        return ServiceData
                .<Parent>builder()
                .data(res.getData())
                .build();
    }

    @Override
    public ServiceData<Parent> delete(ParentParam param) {

        log.info("catch delete param: {}", param);

        var deletedParent = Parent
                .builder()
                .deletedAt(LocalDateTime.now())
                .isActive(false)
                .build();

        var res = this
                .iRepository
                .update(param, deletedParent);

        return ServiceData
                .<Parent>builder()
                .data(res.getData())
                .build();
    }

    @Override
    public ServiceData<Parent> activate(ParentParam param) {

        log.info("catch activate param: {}", param);

        var updatedParent = Parent
                .builder()
                .updatedAt(LocalDateTime.now())
                .deletedAt(null)
                .isActive(true)
                .build();

        var res = this
                .iRepository
                .update(param, updatedParent);

        return ServiceData
                .<Parent>builder()
                .data(res.getData())
                .build();
    }

    @Override
    public ServiceData<ParentMedicFacility> connect(ParentParam param, String uniqueId) {

        this.parentMedicStub
                .save(
                        ParentMedicFacility
                                .newBuilder()
                                .build()
                );

        return null;
    }
}
