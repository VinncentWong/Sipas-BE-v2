package org.parent_service.parent.repository;

import org.parent_service.parent.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {
    @Query(name = "Parent.queryByEmail")
    Parent queryByEmail(@Param("email") String email);
}
