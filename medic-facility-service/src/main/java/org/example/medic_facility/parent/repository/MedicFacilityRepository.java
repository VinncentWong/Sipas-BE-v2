package org.example.medic_facility.parent.repository;

import org.example.medic_facility.entity.MedicFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicFacilityRepository extends JpaRepository<MedicFacility, Long> {}
