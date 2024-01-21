package org.example.repository;

import org.example.entity.MedicFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicFacilityRepository extends JpaRepository<MedicFacility, Long> {}
