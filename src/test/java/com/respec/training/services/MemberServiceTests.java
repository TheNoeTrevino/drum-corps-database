package com.respec.training.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.respec.training.DTO.CreateUpdateMemberDTO;
import com.respec.training.DTO.MemberDTO;
import com.respec.training.DTO.ResponseDrumCorpsDTO;
import com.respec.training.exceptions.NotFoundException;
import com.respec.training.mappers.MemberMapper;
import com.respec.training.models.DrumCorps;
import com.respec.training.models.Member;
import com.respec.training.repositories.MemberRepository;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTests {

    @InjectMocks
    private MemberService service;

    @Mock
    private MemberMapper mapper;

    @Mock
    private MemberRepository repo;

    DrumCorps sampleDrumCorps = new DrumCorps("Troopers", 10,
            LocalDate.of(2007, 07, 21),true,
            LocalDate.of(2008, 03, 15), null);

    // unhappy path testing for non existing Id
    @Test
    public void getShouldThrowNotFoundException() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> {
                service.getMemberById(99L);
            }).hasMessage("Could not find member with id:99").isInstanceOf(NotFoundException.class);
        });
    }

    // happy path testing for existing id
    @Test
    public void getShouldReturn200Found() {

        Member expectedMember = new Member("Noe", "Trevino", 20, LocalDate.of(2007, 07, 21),
        "Front Ensemble", "Marimba", "noe.trevino@gmail.com", sampleDrumCorps);

        MemberDTO expectedMemberDTO = mapper.memberToMemberDTO(expectedMember);

        when(repo.findById(1L)).thenReturn(Optional.of(expectedMember));
        when(mapper.memberToMemberDTO(expectedMember)).thenReturn(expectedMemberDTO);
        
        assertEquals(service.getMemberById(1L), expectedMemberDTO);
    }

    // UNHAPPY path testing for patch mapping happy
    @Test
    public void patchingShouldThrowNotFoundException() {
        CreateUpdateMemberDTO newMember = new CreateUpdateMemberDTO(1L, "Noe", "Trevino", 20,
            LocalDate.of(2007, 07, 21), "Front Ensemble", "Marimba", "noe.trevino@gmail.com");
        
        when(repo.findById(99L)).thenReturn(Optional.empty());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> {
                service.updateMember(99L, newMember); // test method
            }).hasMessage("Could not find member with id:99").isInstanceOf(NotFoundException.class);
        });
    }

    // HAPPY path testing for patch mapping
    // test method updateMember()
    @Test
    public void updateShouldReturnMemberDTO() {

        

        ResponseDrumCorpsDTO responseDrumCorps = new ResponseDrumCorpsDTO(1L,"Troopers", 10,
                                                    LocalDate.of(2007, 07, 21),true,
                                                    LocalDate.of(2008, 03, 15));

        Member oldMember = new Member("OLD", "OLD", 20, LocalDate.of(2007, 07, 21),
                            "Front Ensemble", "Marimba", "noe.trevino@gmail.com", sampleDrumCorps);

        CreateUpdateMemberDTO memberUpdate = new CreateUpdateMemberDTO(1L, "OLD", "OLD", 20, LocalDate.of(2007, 07, 21),
                            "Front Ensemble", "Marimba", "noe.trevino@gmail.com");

        Member expectedMember = new Member("Noe", "Trevino", 20, LocalDate.of(2007, 07, 21),
                            "Front Ensemble", "Marimba", "noe.trevino@gmail.com", sampleDrumCorps);

        MemberDTO responseDTO = new MemberDTO(1L, responseDrumCorps, "Noe", "Trevino", 20, LocalDate.of(2007, 07, 21),
                            "Front Ensemble", "Marimba", "noe.trevino@gmail.com");

        when(repo.findById(1L)).thenReturn(Optional.of(oldMember.setId(1L)));
        when(mapper.createUpdateMemberDTOToMember(memberUpdate)).thenReturn(expectedMember);

        expectedMember.setId(1L);
        
        when(mapper.memberToMemberDTO(expectedMember)).thenReturn(responseDTO);

        service.updateMember(1L, memberUpdate);

        verify(mapper, times(1)).memberToMemberDTO(expectedMember);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(expectedMember)
                    .usingRecursiveComparison()
                    .ignoringFields("memberId", "id", "drumCorps", "corpsId")
                    .isEqualTo(responseDTO);
        });

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(expectedMember.getDrumCorps())
                    .usingRecursiveComparison()
                    .ignoringFields("id", "corpsId", "members")
                    .isEqualTo(responseDTO.getDrumCorps());
        });
    
    }

    // unhappy delete, should return not found
    @Test
    public void deleteShouldThrowNotFoundException() {

        when(repo.findById(1L)).thenReturn(Optional.empty());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> {
                service.deleteMemberById(1L); // test method
            }).hasMessage("Could not find member with id:1").isInstanceOf(NotFoundException.class);
        });
    }

    // happy delete, should return drum corps dto
    // test method deleteByDrumCorpsId()
    @Test
    public void deleteShouldReturnDrumCorpsDTO() {

        Member foundMember = new Member("Noe", "Trevino", 20, LocalDate.of(2007, 07, 21),
        "Front Ensemble", "Marimba", "noe.trevino@gmail.com", sampleDrumCorps);;

        // turn it into a dto. mapper
        MemberDTO expectedDTO = mapper.memberToMemberDTO(foundMember);

        when(repo.findById(1L)).thenReturn(Optional.of(foundMember));
        when(mapper.memberToMemberDTO(foundMember)).thenReturn(expectedDTO);
        when(repo.deleteByMemberId(1L)).thenReturn(1);

        MemberDTO actualDTO = service.deleteMemberById(1L);
        assertEquals(expectedDTO, actualDTO);
    }
}

