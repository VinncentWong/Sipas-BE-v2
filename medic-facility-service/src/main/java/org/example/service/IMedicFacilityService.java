package org.example.service;

import org.example.dto.MedicFacilityDto;
import org.example.entity.MedicFacility;
import org.example.entity.MedicFacilityParam;
import org.example.response.HttpResponse;
import org.example.response.ServiceData;

import java.util.List;

public interface IMedicFacilityService {

    ServiceData<MedicFacility> save(MedicFacilityDto.Create dto);
    ServiceData<MedicFacility> login(MedicFacilityDto.Login dto);
    ServiceData<MedicFacility> get(MedicFacilityParam param);
    ServiceData<List<MedicFacility>> getList(MedicFacilityParam param);
    ServiceData<MedicFacility> update(MedicFacilityParam param, MedicFacility data);
    ServiceData<MedicFacility> delete(MedicFacilityParam param);
    ServiceData<MedicFacility> activate(MedicFacilityParam param);
}
