package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.MedicFacilityDto;
import org.example.entity.MedicFacility;
import org.example.enums.Role;
import org.example.exception.DataAlreadyExistException;
import org.example.exception.DataNotFoundException;
import org.example.exception.ForbiddenException;
import org.example.jwt.JwtUtil;
import org.example.repository.MedicFacilityRepository;
import org.example.response.ServiceData;
import org.example.util.BcryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class MedicFacilityService implements IMedicFacilityService{

    @Autowired
    private MedicFacilityRepository repository;

    @Value("${jwt.secret}")
    private String jwtSecret;

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
}
