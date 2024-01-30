package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.dto.MedicFacilityDto;
import org.example.dubbo.DubboMedicFacilityServiceTriple;
import org.example.dubbo.ListMedicFacility;
import org.example.dubbo.Pagination;
import org.example.entity.MedicFacility;
import org.example.entity.MedicFacilityParam;
import org.example.enums.Role;
import org.example.exception.DataAlreadyExistException;
import org.example.exception.DataNotFoundException;
import org.example.exception.ForbiddenException;
import org.example.jwt.JwtUtil;
import org.example.repository.IRepository;
import org.example.repository.MedicFacilityRepository;
import org.example.response.HttpResponse;
import org.example.response.ServiceData;
import org.example.util.BcryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@Slf4j
@DubboService
public class MedicFacilityService
        extends DubboMedicFacilityServiceTriple.MedicFacilityServiceImplBase
        implements IMedicFacilityService{

    @Autowired
    private MedicFacilityRepository repository;

    @Autowired
    private IRepository iRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public MedicFacilityService(MedicFacilityRepository repository) {
        this.repository = repository;
    }

    @Override
    public ServiceData<MedicFacility> save(MedicFacilityDto.Create dto) {

        log.info("catch create dto: {}", dto);

        // check if data already exist or not by email
        var exist = this.repository
                .findOne(
                        Example.of(
                                MedicFacility
                                        .builder()
                                        .email(dto.email())
                                        .build()
                        )
                )
                .isPresent();

        if(exist){
            throw new DataAlreadyExistException("medic facility already exist");
        }

        var data = this.repository
                .save(dto.toMedicFacility());

        return ServiceData
                .<MedicFacility>builder()
                .data(data)
                .build();
    }

    @Override
    public ServiceData<MedicFacility> login(MedicFacilityDto.Login dto) {

        log.info("catch login dto: {}", dto);
        var tempMedicFacility = dto.toMedicFacility();
        var medicFacility = this.repository
                .findOne(
                        Example.of(
                                MedicFacility
                                        .builder()
                                        .email(dto.email())
                                        .build()
                        )
                ).orElseThrow(() -> new DataNotFoundException("medic facility not found"));

        if(BcryptUtil.isMatch(tempMedicFacility.getPassword(), medicFacility.getPassword())){
            var jwtToken = JwtUtil.generateJwtToken(this.jwtSecret, Role.MEDIC_FACILITY.name(), medicFacility.getId());
            return ServiceData
                    .<MedicFacility>builder()
                    .data(medicFacility)
                    .metadata(jwtToken)
                    .build();
        } else {
            throw new ForbiddenException("credential not valid");
        }
    }

    @Override
    public ServiceData<MedicFacility> get(MedicFacilityParam param) {
        log.info("catch get param: {}", param);

        var data = this.iRepository
                .get(param);

        return ServiceData
                .<MedicFacility>builder()
                .data(data.getData())
                .build();
    }

    @Override
    public ServiceData<List<MedicFacility>> getList(MedicFacilityParam param) {
        log.info("catch get list param: {}", param);

        var data = this.iRepository
                .getList(param);

        return ServiceData
                .<List<MedicFacility>>builder()
                .data(data.getData())
                .pg(data.getPg())
                .build();
    }

    @Override
    public ServiceData<MedicFacility> update(MedicFacilityParam param, MedicFacility data) {
        log.info("catch update param: {}", param);
        log.info("catch update to be updated: {}", data);

        var updateData = this.iRepository
                .update(param, data);

        return ServiceData
                .<MedicFacility>builder()
                .data(updateData.getData())
                .build();
    }

    @Override
    public ServiceData<MedicFacility> delete(MedicFacilityParam param) {
        log.info("catch delete param: {}", param);

        var updateData = this.iRepository
                .update(param, MedicFacility
                        .builder()
                        .isActive(false)
                        .deletedAt(LocalDateTime.now())
                        .build());

        return ServiceData
                .<MedicFacility>builder()
                .data(updateData.getData())
                .build();
    }

    @Override
    public ServiceData<MedicFacility> activate(MedicFacilityParam param) {
        log.info("catch activate param: {}", param);

        var updateData = this.iRepository
                .update(param, MedicFacility
                        .builder()
                        .isActive(true)
                        .deletedAt(null)
                        .updatedAt(LocalDateTime.now())
                        .build());

        return ServiceData
                .<MedicFacility>builder()
                .data(updateData.getData())
                .build();
    }

    // gRPC with Dubbo implementation here

    @Override
    public org.example.dubbo.MedicFacility get(org.example.dubbo.MedicFacilityParam request) {
        var reqParam = MedicFacilityParam
                .builder()
                .id(request.getId())
                .ids(request.getIdsList())
                .email(request.getEmail())
                .uniqueCode(request.getUniqueCode())
                .pgParam(
                        HttpResponse.PaginationParam
                                .builder()
                                .offset((int)request.getPgParam().getOffset())
                                .limit((int)request.getPgParam().getLimit())
                                .build()
                )
                .build();

        var repoData = this.iRepository
                .get(reqParam);

        if(repoData != null){
            var data = repoData.getData();

            return convertFromEntityToPb(data);
        }

        return null;
    }

    @Override
    public ListMedicFacility getList(org.example.dubbo.MedicFacilityParam request) {
        var reqParam = MedicFacilityParam
                .builder()
                .id(request.getId())
                .ids(request.getIdsList())
                .email(request.getEmail())
                .uniqueCode(request.getUniqueCode())
                .pgParam(
                        HttpResponse.PaginationParam
                                .builder()
                                .offset((int)request.getPgParam().getOffset())
                                .limit((int)request.getPgParam().getLimit())
                                .build()
                )
                .build();

        var repoData = this.iRepository
                .getList(reqParam);

        if(repoData != null){
            var data = repoData.getData()
                    .stream().map(this::convertFromEntityToPb)
                    .toList();

            var pg = repoData.getPg();

            return ListMedicFacility
                    .newBuilder()
                    .addAllMedicFacilities(data)
                    .setPg(
                            Pagination
                                    .newBuilder()
                                    .setCurrentElement(pg.getCurrentElements())
                                    .setCurrentPage(pg.getCurrentPage())
                                    .setTotalElement(pg.getTotalElements())
                                    .setTotalPage(pg.getTotalPage())
                                    .build()
                    )
                    .build();
        }

        return null;
    }

    private org.example.dubbo.MedicFacility convertFromEntityToPb(MedicFacility data){
        return org.example.dubbo.MedicFacility
                .newBuilder()
                .setId(data.getId())
                .setEmail(data.getEmail())
                .setAddress(data.getAddress())
                .setIsActive(data.getIsActive())
                .setTelephoneNumber(data.getTelephoneNumber())
                .setWhatsappNumber(data.getWhatsappNumber())
                .build();
    }
}
