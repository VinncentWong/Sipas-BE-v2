package org.example.medic_facility.entity;

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
public class MedicFacilityParam {

    private Long id;

    @ParamColumn(name = "id")
    private List<Long> ids;

    @ParamColumn(name = "email")
    private String email;

    @ParamColumn(name = "unique_code")
    private String uniqueCode;

    private HttpResponse.PaginationParam pgParam;
}
