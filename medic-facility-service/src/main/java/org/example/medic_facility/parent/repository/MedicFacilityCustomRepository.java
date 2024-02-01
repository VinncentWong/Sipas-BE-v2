package org.example.medic_facility.parent.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.example.medic_facility.entity.MedicFacility;
import org.example.medic_facility.entity.MedicFacilityParam;
import org.example.medic_facility.mapper.MedicFacilityMapper;
import org.example.response.HttpResponse;
import org.example.response.RepositoryData;
import org.example.util.QueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class MedicFacilityCustomRepository implements IRepository{

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MedicFacilityRepository repository;

    private MedicFacilityMapper mapper = MedicFacilityMapper.INSTANCE;

    @Override
    public RepositoryData<MedicFacility> get(MedicFacilityParam param) {
        var query = QueryUtil
                .generateQuery(em, MedicFacility.class, param);

        log.info("query generated on get: {}", query);

        var data = query
                .getSingleResult();

        log.info("get data result: {}", data);

        return RepositoryData
                .<MedicFacility>builder()
                .data(data)
                .build();
    }

    @Override
    public RepositoryData<List<MedicFacility>> getList(MedicFacilityParam param) {
        var query = QueryUtil
                .generateQuery(em, MedicFacility.class, param);

        var queryCount = QueryUtil
                .generateQueryCount(em, MedicFacility.class, param);

        log.info("query generated on getList: {}", query);


        var data = query.getResultList();

        log.info("get list data result: {}", data);

        var n = queryCount.getSingleResult();

        log.info("number of total items: {}", n);

        var totalPage = (n / param.getPgParam().getLimit()) + 1;

        return RepositoryData
                .<List<MedicFacility>>builder()
                .data(data)
                .pg(
                        HttpResponse.Pagination
                                .builder()
                                .currentElements((long)data.size())
                                .currentPage((long)param.getPgParam().getOffset())
                                .totalElements(n)
                                .totalPage(totalPage)
                                .build()
                )
                .build();
    }

    @Override
    public RepositoryData<MedicFacility> update(MedicFacilityParam param, MedicFacility data) {
        var query = QueryUtil
                .generateQuery(
                        em, MedicFacility.class, param
                );

        var queryData = query
                .getSingleResult();

        var updatedData = this.mapper
                .update(data, queryData);

        var resRepo = this.repository.save(updatedData);

        return RepositoryData
                .<MedicFacility>builder()
                .data(resRepo)
                .build();
    }
}
