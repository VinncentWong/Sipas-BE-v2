package org.example.parent.entity;

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
public class ParentParam {

    private Long id;

    @ParamColumn(name = "id")
    private List<Long> ids;

    @ParamColumn(name = "father_name")
    private String fatherName;

    @ParamColumn(name = "mother_name")
    private String motherName;

    private HttpResponse.PaginationParam pgParam;
}
