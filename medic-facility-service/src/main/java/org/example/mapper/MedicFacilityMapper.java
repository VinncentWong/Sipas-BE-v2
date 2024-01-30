package org.example.mapper;

import org.example.entity.MedicFacility;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MedicFacilityMapper {

    MedicFacilityMapper INSTANCE = Mappers.getMapper(MedicFacilityMapper.class);

    @Mapping(source = "deletedAt", target = "deletedAt", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    MedicFacility update(MedicFacility from, @MappingTarget MedicFacility to);
}
