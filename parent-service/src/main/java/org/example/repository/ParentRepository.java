package org.example.repository;

import org.example.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {
    @Query(name = "Parent.queryByEmail")
    Parent queryByEmail(@Param("email") String email);
}
