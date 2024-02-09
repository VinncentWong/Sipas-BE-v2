package org.parent_service.parent.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.parent_service.parent.entity.Parent;
import org.parent_service.parent.entity.ParentParam;
import org.parent_service.parent.mapper.ParentMapper;
import org.example.response.HttpResponse;
import org.example.response.RepositoryData;
import org.example.util.QueryUtil;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class CustomParentRepository implements IRepository{

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ParentRepository repository;

    private ParentMapper mapper = Mappers.getMapper(ParentMapper.class);

    @Override
    public RepositoryData<Parent> get(ParentParam param) {

        log.info("sebelum manggil generateQuery");

        var query = QueryUtil.generateQuery(
                em,
                Parent.class,
                param
        );

        log.info("custom query created: {}", query);

        var res = query.getSingleResult();
        log.info("catch result of get: {}", res);

        return RepositoryData
                .<Parent>builder()
                .data(res)
                .build();
    }

    @Override
    public RepositoryData<List<Parent>> getList(ParentParam param) {
        var query = QueryUtil.generateQuery(
                em,
                Parent.class,
                param
        );

        var res = query.getResultList();
        log.info("catch result of get: {}", res);

        var queryCount = QueryUtil.generateQueryCount(
                em,
                Parent.class,
                param
        );

        var resCount = queryCount.getSingleResult();

        var totalPage = (resCount / param.getPgParam().getLimit()) + 1;

        return RepositoryData
                .<List<Parent>>builder()
                .data(res)
                .pg(
                        HttpResponse.Pagination
                                .builder()
                                .currentElements((long)res.size())
                                .currentPage((long)param.getPgParam().getOffset())
                                .totalElements(resCount)
                                .totalPage(totalPage)
                                .build()
                )
                .build();
    }

    @Override
    public RepositoryData<Parent> update(ParentParam param, Parent parent) {
        var query = QueryUtil
                .generateQuery(
                        em,
                        Parent.class,
                        param
                );

        var res = query.getSingleResult();

        if(res != null){
            res = mapper.updateParent(parent, res);
            this.repository.save(res);
        }

        log.info("update parent result: {}", res);

        return RepositoryData
                .<Parent>builder()
                .data(res)
                .build();
    }
}
