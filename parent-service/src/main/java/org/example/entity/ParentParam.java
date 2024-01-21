package org.example.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.response.HttpResponse;

import java.util.List;

@Builder
@Setter
@Getter
public class ParentParam {

    private Long id;

    private List<Long> ids;

    private String fatherName;

    private String motherName;

    private HttpResponse.PaginationParam pgParam;
}
