package entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import java.time.LocalDateTime

data class ParentMedicFacility(

    @Id
    @Column("id")
    var id: Long? = null,

    @Column("fk_parent_id")
    var fkParentId: Long? = null,

    @Column("fk_medic_facility_id")
    var fkMedicFacilityId: Long? = null,

    val createdAt: LocalDateTime? = null,

    val updatedAt: LocalDateTime? = null,

    val deletedAt: LocalDateTime? = null
)
