package org.medic.medic_facility.repository;

import org.medic.medic_facility.entity.MedicFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicFacilityRepository extends JpaRepository<MedicFacility, Long> {}
