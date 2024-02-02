package org.medic.medic_facility.repository;

import org.medic.medic_facility.entity.MedicFacility;
import org.medic.medic_facility.entity.MedicFacilityParam;
import org.example.response.RepositoryData;

import java.util.List;

public interface IRepository {
    RepositoryData<MedicFacility> get(MedicFacilityParam param);
    RepositoryData<List<MedicFacility>> getList(MedicFacilityParam param);
    RepositoryData<MedicFacility> update(MedicFacilityParam param, MedicFacility data);
}
