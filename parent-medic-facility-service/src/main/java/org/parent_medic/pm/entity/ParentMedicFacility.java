package org.parent_medic.pm.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class ParentMedicFacility implements Serializable {

    @Id
    private Long id;

    @Column("fk_parent_id")
    private Long fkParentId;

    @Column("fk_medic_id")
    private Long fkMedicId;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Column("deleted_at")
    private LocalDateTime deletedAt;
}
