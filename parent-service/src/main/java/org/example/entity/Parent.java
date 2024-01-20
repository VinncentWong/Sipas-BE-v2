package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Entity
@NamedQueries({
        @NamedQuery(
                name = "Parent.queryByEmail",
                query = "SELECT p FROM Parent p WHERE p.email = :email"
        )
})
public class Parent extends TimestampMeta{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fatherName;

    private String motherName;

    private String email;

    private String password;

    private Boolean isConnectedWithFaskes;
}
