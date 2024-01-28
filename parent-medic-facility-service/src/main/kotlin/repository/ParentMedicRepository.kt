package repository

import entity.ParentMedicFacility
import entity.ParentMedicParam
import mapper.ParentMedicMapper
import org.example.response.HttpResponse.Pagination
import org.example.response.RepositoryData
import org.example.util.QueryUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import reactor.core.publisher.Mono

@org.springframework.stereotype.Repository
class ParentMedicRepository(
    private val r2dbcEntityTemplate: R2dbcEntityTemplate,
    private val log: Logger = LoggerFactory.getLogger(ParentMedicRepository::class.java),
): Repository {

    private var mapper = ParentMedicMapper.INSTANCE

    override fun save(data: ParentMedicFacility): Mono<RepositoryData<ParentMedicFacility>> {

        log.info("catch save data: {}", data)

        return this
            .r2dbcEntityTemplate
            .insert(data)
            .map {
                RepositoryData
                    .builder<ParentMedicFacility>()
                    .data(it)
                    .build()
            }
    }

    override fun get(param: ParentMedicParam): Mono<RepositoryData<ParentMedicFacility>> {

        log.info("catch get param: {}", param)

        val query = QueryUtil
            .generateReactiveQuery(param, false)

        return this.r2dbcEntityTemplate
            .selectOne(query, ParentMedicFacility::class.java)
            .map {
                RepositoryData
                    .builder<ParentMedicFacility>()
                    .data(it)
                    .build()
            }
    }

    override fun getList(param: ParentMedicParam): Mono<RepositoryData<List<ParentMedicFacility>>> {

        log.info("catch get list param: {}", param)

        val query = QueryUtil
            .generateReactiveQuery(param, false)

        val queryCount = QueryUtil
            .generateReactiveQuery(param, true)

        val executeQuery = this.r2dbcEntityTemplate
            .select(query, ParentMedicFacility::class.java)
            .collectList()

        val executeQueryCount = this.r2dbcEntityTemplate
            .count(queryCount, Long::class.java)

        return Mono.zip(
            executeQuery, executeQueryCount
        ).map {
            val list = it.t1
            val n = it.t2

            val totalPage = (n / param.pgParam.limit) + 1

            RepositoryData
                .builder<List<ParentMedicFacility>>()
                .data(list)
                .pg(
                    Pagination
                        .builder()
                        .currentPage(param.pgParam.offset.toLong())
                        .currentElements(list.size.toLong())
                        .totalPage(totalPage)
                        .totalElements(n)
                        .build()
                )
                .build()
        }
    }

    override fun update(param: ParentMedicParam, data: ParentMedicFacility): Mono<RepositoryData<ParentMedicFacility>> {

        log.info("catch update param: {} with data to be updated: {}", param, data)

        val repositoryData = this.get(param)

        return repositoryData
            .map {
                val databaseData = it.data
                this.mapper.updateParentMedic(data, databaseData)
            }
            .flatMap {
                this.save(it)
            }.then(Mono.just(
                RepositoryData
                    .builder<ParentMedicFacility>()
                    .build()
            ))
    }


}