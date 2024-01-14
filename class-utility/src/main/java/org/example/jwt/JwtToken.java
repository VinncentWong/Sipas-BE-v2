package org.example.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class JwtToken {

    private Long id;

    private String role;
}
