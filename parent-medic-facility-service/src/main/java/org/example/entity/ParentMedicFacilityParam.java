package org.example.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.annotation.ParamColumn;
import org.example.response.HttpResponse;

import java.util.List;

@Builder
@Setter
@Getter
@ToString
public class ParentMedicFacilityParam {

    private Long id;

    @ParamColumn(name = "fk_parent_id")
    private Long fkParentId;

    @ParamColumn(name = "fk_parent_id")
    private List<Long> fkParentIds;

    @ParamColumn(name = "fk_medic_id")
    private Long fkMedicId;

    @ParamColumn(name = "fk_medic_id")
    private List<Long> fkMedicIds;

    private HttpResponse.PaginationParam pgParam;
}
