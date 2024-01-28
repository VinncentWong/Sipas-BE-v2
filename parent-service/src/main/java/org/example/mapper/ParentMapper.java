package org.example.mapper;

import org.example.entity.Parent;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ParentMapper {

    Parent updateParent(Parent from, @MappingTarget Parent to);
}
