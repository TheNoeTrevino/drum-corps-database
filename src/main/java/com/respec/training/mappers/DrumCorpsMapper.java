package com.respec.training.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.respec.training.DTO.CreateUpdateDrumCorpsDTO;
import com.respec.training.DTO.DrumCorpsDTO;
import com.respec.training.DTO.ResponseDrumCorpsDTO;
import com.respec.training.models.DrumCorps;

@Mapper(componentModel = "spring")
public interface DrumCorpsMapper {

    @Mapping(source = "id", target = "corpsId")
    DrumCorpsDTO drumCorpsToDrumCorpsDTO(DrumCorps drumCorps);


    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "members", ignore = true)
    })
    DrumCorps createUpdateDrumCorpsDTOToDrumCorps(CreateUpdateDrumCorpsDTO createUpdateDrumCorpsDTO);
    
    CreateUpdateDrumCorpsDTO drumCorpsToCreateUpdateDrumCorpsDTO(DrumCorps drumCorps);
    
    @Mapping(source = "id", target = "corpsId")
    ResponseDrumCorpsDTO drumCorpsToReponseDrumCorpsDTO(DrumCorps drumCorps);
}