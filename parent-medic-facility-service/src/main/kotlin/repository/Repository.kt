package repository

import entity.ParentMedicFacility
import entity.ParentMedicParam
import org.example.response.RepositoryData
import reactor.core.publisher.Mono

interface Repository {

    fun save(data: ParentMedicFacility): Mono<RepositoryData<ParentMedicFacility>>

    fun get(param: ParentMedicParam): Mono<RepositoryData<ParentMedicFacility>>

    fun getList(param: ParentMedicParam): Mono<RepositoryData<List<ParentMedicFacility>>>

    fun update(param: ParentMedicParam, data: ParentMedicFacility): Mono<RepositoryData<ParentMedicFacility>>
}