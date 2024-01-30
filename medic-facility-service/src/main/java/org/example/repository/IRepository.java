package org.example.repository;

import org.example.entity.MedicFacility;
import org.example.entity.MedicFacilityParam;
import org.example.response.RepositoryData;

import java.util.List;

public interface IRepository {
    RepositoryData<MedicFacility> get(MedicFacilityParam param);
    RepositoryData<List<MedicFacility>> getList(MedicFacilityParam param);
    RepositoryData<MedicFacility> update(MedicFacilityParam param, MedicFacility data);
}
