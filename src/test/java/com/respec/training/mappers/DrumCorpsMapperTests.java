package com.respec.training.mappers;

import java.time.LocalDate;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import com.respec.training.DTO.CreateUpdateDrumCorpsDTO;
import com.respec.training.DTO.DrumCorpsDTO;
import com.respec.training.models.DrumCorps;

public class DrumCorpsMapperTests {

    private final DrumCorpsMapper mapper;

    public DrumCorpsMapperTests() {
        this.mapper = new DrumCorpsMapperImpl();
    }

    @Test
    public void shouldMapDrumCorpsToDrumCorpsDTO() {
        DrumCorps drumCorps = new DrumCorps("Troopers", 10,
                                LocalDate.of(2007, 07, 21),true,
                                LocalDate.of(2008, 03, 15), null);

        DrumCorpsDTO drumCorpsDTO = mapper.drumCorpsToDrumCorpsDTO(drumCorps);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(drumCorpsDTO)
                    .usingRecursiveComparison()
                    .ignoringFields("corpsId", "id")
                    .isEqualTo(drumCorps);
        });
    }

    @Test
    public void shouldMapCreateUpdateDrumCorpsDTOToDrumCorps() {
        CreateUpdateDrumCorpsDTO createUpdateDrumCorpsDTO = new CreateUpdateDrumCorpsDTO("Troopers", 2,
                                                            LocalDate.of(2007, 07, 21), true,
                                                            LocalDate.of(2007, 03, 15));
        
        DrumCorps updatedDrumCorps = mapper.createUpdateDrumCorpsDTOToDrumCorps(createUpdateDrumCorpsDTO);
            
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(createUpdateDrumCorpsDTO)
                    .usingRecursiveComparison()
                    .ignoringFields("corpsId", "id")
                    .isEqualTo(updatedDrumCorps);
        });
    }

    @Test
    public void shouldMapDrumCorpsToCreateUpdateDrumCorpsDTO() {
        DrumCorps drumCorps = new DrumCorps("Troopers", 10,
            LocalDate.of(2007, 07, 21), true,
            LocalDate.of(2008, 03, 15), null);
        
            CreateUpdateDrumCorpsDTO createUpdateDTO = mapper.drumCorpsToCreateUpdateDrumCorpsDTO(drumCorps);

            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(drumCorps)
                        .usingRecursiveComparison()
                        .ignoringFields("id", "members")
                        .isEqualTo(createUpdateDTO);
        });
    }
}
