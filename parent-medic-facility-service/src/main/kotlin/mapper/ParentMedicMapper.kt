package mapper

import entity.ParentMedicFacility
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.NullValuePropertyMappingStrategy
import org.mapstruct.factory.Mappers

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
interface ParentMedicMapper {

    companion object{
        val INSTANCE: ParentMedicMapper = Mappers.getMapper(ParentMedicMapper::class.java)
    }

    @Mapping(source = "deletedAt", target = "deletedAt", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    fun updateParentMedic(from: ParentMedicFacility, @MappingTarget to: ParentMedicFacility): ParentMedicFacility
}