package com.respec.training.mappers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.respec.training.DTO.CreateUpdateMemberDTO;
import com.respec.training.DTO.MemberDTO;
import com.respec.training.DTO.ResponseDrumCorpsDTO;
import com.respec.training.models.DrumCorps;
import com.respec.training.models.Member;

public class MemberMapperTests {
        
    DrumCorps sampleDrumCorps = new DrumCorps("Troopers", 10,
        LocalDate.of(2007, 07, 21),true,
        LocalDate.of(2008, 03, 15), null);

    @Mock
    private DrumCorpsMapper corpsMapper;

    @InjectMocks
    private MemberMapperImpl mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldMapMemberToMemberDTO() {

        Member member = new Member("Noe", "Trevino", 20, LocalDate.of(2007, 07, 21),
                             "Front Ensemble", "Marimba", "noe.trevino@gmail.com", sampleDrumCorps);

        ResponseDrumCorpsDTO reponseDrumCorps = new ResponseDrumCorpsDTO(1, "Troopers", 10,
            LocalDate.of(2007, 07, 21),true,
            LocalDate.of(2008, 03, 15));

        when(corpsMapper.drumCorpsToReponseDrumCorpsDTO(any(DrumCorps.class)))
                    .thenReturn(reponseDrumCorps);

        MemberDTO memberDTO = mapper.memberToMemberDTO(member);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(memberDTO)
                    .usingRecursiveComparison()
                    .ignoringFields("memberId", "id", "drumCorps")
                    .isEqualTo(member);
        });

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(memberDTO.getDrumCorps())
                    .isNotNull()
                    .usingRecursiveComparison()
                    .ignoringFields("corpsId", "id")
                    .isEqualTo(member.getDrumCorps());
        });
    }

    @Test // fix this test
    public void shouldMapCreateUpdateMemberDTOToMember() {

        CreateUpdateMemberDTO createUpdateMemberDTO = new CreateUpdateMemberDTO(1L, "Noe", "Trevino", 20, LocalDate.of(2007, 07, 21), 
                                                    "Front Ensemble", "Marimba", "noe.trevino@gmail.com");
        
        Member updatedMember = mapper.createUpdateMemberDTOToMember(createUpdateMemberDTO);
            
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(createUpdateMemberDTO)
                    .usingRecursiveComparison()
                    .ignoringFields("memberId", "id", "drumCorps", "drumCorpsId")
                    .isEqualTo(updatedMember);
        });
    }

    @Test
    public void shouldMapMemberToCreateUpdateMemberDTO() {

        Member member = new Member("Noe", "Trevino", 20, LocalDate.of(2007, 07, 21),
        "Front Ensemble", "Marimba", "noe.trevino@gmail.com", sampleDrumCorps);
        
        CreateUpdateMemberDTO createUpdateDTO = mapper.memberToCreateUpdateMemberDTO(member);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(member)
                    .usingRecursiveComparison()
                    .ignoringFields("corpsId", "id", "members", "drumCorps")
                    .isEqualTo(createUpdateDTO);
        });
    }
}
