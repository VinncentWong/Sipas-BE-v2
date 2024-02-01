package org.example.parent.mapper;

import org.example.parent.entity.Parent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ParentMapper {

    @Mapping(source = "deletedAt", target = "deletedAt", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    Parent updateParent(Parent from, @MappingTarget Parent to);
}
