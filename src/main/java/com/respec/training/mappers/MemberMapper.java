package com.respec.training.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import com.respec.training.DTO.CreateUpdateMemberDTO;
import com.respec.training.DTO.MemberDTO;
import com.respec.training.models.Member;

@Mapper(componentModel = "spring", uses = DrumCorpsMapper.class, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MemberMapper {

    @Mapping(source = "id", target = "memberId")
    MemberDTO memberToMemberDTO(Member member);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "drumCorps", ignore = true), // taken care of in service
    })
    Member createUpdateMemberDTOToMember(CreateUpdateMemberDTO createUpdateMemberDTO);

    @Mapping(target = "drumCorpsId", ignore = true)
    CreateUpdateMemberDTO memberToCreateUpdateMemberDTO(Member member);
}