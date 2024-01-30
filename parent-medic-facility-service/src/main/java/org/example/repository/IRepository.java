package org.example.repository;

import org.example.entity.ParentMedicFacility;
import org.example.entity.ParentMedicFacilityParam;
import org.example.response.RepositoryData;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IRepository {
    Mono<RepositoryData<ParentMedicFacility>> save(ParentMedicFacility data);
    Mono<RepositoryData<ParentMedicFacility>> get(ParentMedicFacilityParam param);
    Mono<RepositoryData<List<ParentMedicFacility>>> getList(ParentMedicFacilityParam param);
    Mono<RepositoryData<ParentMedicFacility>> update(ParentMedicFacilityParam param, ParentMedicFacility data);
}
