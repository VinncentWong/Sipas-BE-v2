package org.example.service;

import org.example.dto.MedicFacilityDto;
import org.example.entity.MedicFacility;
import org.example.response.HttpResponse;
import org.example.response.ServiceData;

public interface IMedicFacilityService {

    ServiceData<MedicFacility> save(MedicFacilityDto.Create dto);
    ServiceData<MedicFacility> login(MedicFacilityDto.Login dto);
}
