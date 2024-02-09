package org.parent_service.parent.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.dubbo.springboot.MedicFacilityService;
import org.dubbo.springboot.ParentMedic;
import org.example.enums.Role;
import org.example.exception.DataAlreadyExistException;
import org.example.exception.DataNotFoundException;
import org.example.exception.ForbiddenException;
import org.example.jwt.JwtUtil;
import org.parent_service.parent.dto.ParentDto;
import org.parent_service.parent.entity.Parent;
import org.parent_service.parent.entity.ParentParam;
import org.parent_service.parent.repository.IRepository;
import org.parent_service.parent.repository.ParentRepository;
import org.parent_medic.pm.entity.ParentMedicFacility;
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

    @DubboReference(timeout = 10000)
    private ParentMedic parentMedicStub;

    @DubboReference(timeout = 10000)
    private MedicFacilityService medicFacilityStub;

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
    public ServiceData<ParentMedicFacility> connect(ParentParam param, String uniqueCode) {

        log.info("parentMedicStub: {}", parentMedicStub);
        log.info("medicStub: {}", medicFacilityStub);

        log.info("catch connect param: {} with unique code = {}", param, uniqueCode);

        var medicFacilityParam = org.dubbo.springboot.MedicFacilityParam
                .newBuilder()
                .setUniqueCode(uniqueCode)
                .build();

        log.info("sending medic facility param: {}", medicFacilityParam);

        var medicFacility = this.medicFacilityStub
                .get(
                        medicFacilityParam
                );

        if(medicFacility == null){
            throw new DataNotFoundException("no medic facility found");
        }

        log.info("medicFacilityStub Dubbo result: {}", medicFacility);

        var parentMedicFacility = this.parentMedicStub
                .save(
                        org.dubbo.springboot.ParentMedicFacility
                                .newBuilder()
                                .setFkMedicId(medicFacility.getId())
                                .setFkParentId(param.getId())
                                .build()
                );

        log.info("parentMedicStub Dubbo result: {}", parentMedicFacility);

        return ServiceData
                .<ParentMedicFacility>builder()
                .data(convertFromPbToEntity(parentMedicFacility))
                .build();
    }

    private ParentMedicFacility convertFromPbToEntity(org.dubbo.springboot.ParentMedicFacility data){
        return ParentMedicFacility
                .builder()
                .id(data.getId())
                .fkParentId(data.getFkParentId())
                .fkMedicId(data.getFkMedicId())
                .build();
    }
}
