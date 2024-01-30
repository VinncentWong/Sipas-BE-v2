package org.example.mapper;

import org.example.entity.ParentMedicFacility;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ParentMedicMapper {

    ParentMedicMapper INSTANCE = Mappers.getMapper(ParentMedicMapper.class);

    @Mapping(source = "deletedAt", target = "deletedAt", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    ParentMedicFacility update(ParentMedicFacility from, @MappingTarget ParentMedicFacility to);
}
