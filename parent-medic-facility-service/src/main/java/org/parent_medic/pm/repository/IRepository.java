package org.parent_medic.pm.repository;

import org.parent_medic.pm.entity.ParentMedicFacility;
import org.parent_medic.pm.entity.ParentMedicFacilityParam;
import org.example.response.RepositoryData;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface IRepository {
    Mono<RepositoryData<ParentMedicFacility>> save(ParentMedicFacility data);
    Mono<RepositoryData<ParentMedicFacility>> get(ParentMedicFacilityParam param);
    Mono<RepositoryData<List<ParentMedicFacility>>> getList(ParentMedicFacilityParam param);
    Mono<RepositoryData<ParentMedicFacility>> update(ParentMedicFacilityParam param, ParentMedicFacility data);
}
