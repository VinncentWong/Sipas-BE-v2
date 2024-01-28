package entity

import org.example.annotation.ParamColumn
import org.example.response.HttpResponse.PaginationParam

data class ParentMedicParam(
    val id: Long,

    @ParamColumn(name = "id")
    val ids: List<Long>,

    @ParamColumn(name = "fk_parent_id")
    val fkParentId: Long,

    @ParamColumn(name = "fk_parent_id")
    val fkParentIds: List<Long>,

    @ParamColumn(name = "fk_medic_facility_id")
    val fkMedicFacilityId: Long,

    @ParamColumn(name = "fk_medic_facility_id")
    val fkMedicFacilityIds: List<Long>,

    val pgParam: PaginationParam
)