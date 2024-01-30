package org.example.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.ParentMedicFacility;
import org.example.entity.ParentMedicFacilityParam;
import org.example.exception.DataNotFoundException;
import org.example.mapper.ParentMedicMapper;
import org.example.response.HttpResponse;
import org.example.response.RepositoryData;
import org.example.util.QueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@Slf4j
public class ParentMedicRepository implements IRepository{

    @Autowired
    private R2dbcEntityTemplate template;

    private ParentMedicMapper mapper = ParentMedicMapper.INSTANCE;

    @Override
    public Mono<RepositoryData<ParentMedicFacility>> save(ParentMedicFacility data) {

        log.info("catch save data: {}", data);

        return this
                .template
                .insert(data)
                .map((d) -> RepositoryData
                        .<ParentMedicFacility>builder()
                        .data(d)
                        .build()
                );
    }

    @Override
    public Mono<RepositoryData<ParentMedicFacility>> get(ParentMedicFacilityParam param) {

        log.info("catch get param: {}", param);

        var query = QueryUtil
                .generateReactiveQuery(param, false);

        return this.template
                .selectOne(query, ParentMedicFacility.class)
                .switchIfEmpty(Mono.error(new DataNotFoundException("parent medic facility data not found")))
                .map((d) -> RepositoryData
                        .<ParentMedicFacility>builder()
                        .data(d)
                        .build()
                );
    }

    @Override
    public Mono<RepositoryData<List<ParentMedicFacility>>> getList(ParentMedicFacilityParam param) {

        var query = QueryUtil
                .generateReactiveQuery(param, false);

        var queryCount = QueryUtil
                .generateReactiveQuery(param, true);

        return Mono.zip(
                this.template
                        .select(query, ParentMedicFacility.class)
                        .collectList()
                        .switchIfEmpty(Mono.just(List.of())),
                this.template
                        .count(queryCount, Long.class)
                        .switchIfEmpty(Mono.just(0L))
        ).map((t2) -> {
            var data = t2.getT1();
            var n = t2.getT2();

            var totalPage = (n / param.getPgParam().getLimit()) + 1;

            return RepositoryData
                    .<List<ParentMedicFacility>>builder()
                    .data(data)
                    .pg(
                            HttpResponse.Pagination
                                    .builder()
                                    .totalPage(totalPage)
                                    .totalElements(n)
                                    .currentPage(param.getPgParam().getOffset().longValue())
                                    .currentElements((long)data.size())
                                    .build()
                    )
                    .build();
        });
    }

    @Override
    public Mono<RepositoryData<ParentMedicFacility>> update(ParentMedicFacilityParam param, ParentMedicFacility data) {
        return this.get(param)
                .map(RepositoryData::getData)
                .flatMap((d) -> {
                    var updateData = this.mapper.update(data, d);
                    return this.save(updateData);
                })
                .map(RepositoryData::getData)
                .map((d) ->
                        RepositoryData
                                .<ParentMedicFacility>builder()
                                .data(d)
                                .build()
                );
    }
}
