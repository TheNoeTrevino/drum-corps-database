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

import com.respec.training.DTO.CreateUpdateDrumCorpsDTO;
import com.respec.training.DTO.DrumCorpsDTO;
import com.respec.training.exceptions.NotFoundException;
import com.respec.training.mappers.DrumCorpsMapper;
import com.respec.training.models.DrumCorps;
import com.respec.training.repositories.DrumCorpsRepository;

@ExtendWith(MockitoExtension.class)
public class DrumCorpsServiceTests {

    @InjectMocks
    private DrumCorpsService service;

    @Mock
    private DrumCorpsMapper mapper;

    @Mock
    private DrumCorpsRepository repo;

    // unhappy path testing for non existing Id
    @Test
    public void getShouldThrowNotFoundException() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> {
                service.getDrumCorpsById(99L); // test method
            }).hasMessage("Could not find Drum Corps with id:99").isInstanceOf(NotFoundException.class);
        });
    }

    // happy path testing for existing id
    @Test
    public void getShouldReturn200Found() {

        DrumCorps expectedDrumCorps = new DrumCorps(1L, "Test", 1,
                LocalDate.of(2000, 7, 21), true,
                LocalDate.of(2008, 7, 21), null);

        DrumCorpsDTO expectedDrumCorpsDTO = mapper.drumCorpsToDrumCorpsDTO(expectedDrumCorps);

        when(repo.findById(1L)).thenReturn(Optional.of(expectedDrumCorps));
        when(mapper.drumCorpsToDrumCorpsDTO(expectedDrumCorps)).thenReturn(expectedDrumCorpsDTO);
        
        assertEquals(service.getDrumCorpsById(1L), expectedDrumCorpsDTO);
    }

    // UNHAPPY path testing for patch mapping happy
    @Test
    public void patchingShouldThrowNotFoundException() {
        CreateUpdateDrumCorpsDTO newDrumCorps = new CreateUpdateDrumCorpsDTO("Troopers", 90,
                LocalDate.of(2000, 7, 21), true,
                LocalDate.of(2008, 7, 21));
        
        when(repo.findById(99L)).thenReturn(Optional.empty());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> {
                service.updateDrumCorps(99L, newDrumCorps); // test method
            }).hasMessage("Could not find Drum Corps with id:99").isInstanceOf(NotFoundException.class);
        });
    }

    // HAPPY path testing for patch mapping
    // test method updateDrumCorps()
    @Test
    public void updateShouldReturnDrumCorpsDTO() {

        DrumCorps oldDrumCorps = new DrumCorps("OLD", 9,
                LocalDate.of(2000, 7, 21), false,
                null, null);

        CreateUpdateDrumCorpsDTO drumCorpsUpdate = new CreateUpdateDrumCorpsDTO("Troopers", 9,
                LocalDate.of(2000, 7, 21), true,
                LocalDate.of(2008, 7, 21));

        DrumCorps expectedDrumCorps = new DrumCorps(1L, "Troopers", 9,
                LocalDate.of(2000, 7, 21), true,
                LocalDate.of(2008, 7, 21), null);

        DrumCorpsDTO responseDTO = new DrumCorpsDTO(1L, "Troopers", 9,
                LocalDate.of(2000, 7, 21), true,
                LocalDate.of(2008, 7, 21), null);

        when(repo.findById(1L)).thenReturn(Optional.of(oldDrumCorps.setId(1L)));
        when(mapper.createUpdateDrumCorpsDTOToDrumCorps(drumCorpsUpdate)).thenReturn(expectedDrumCorps);

        expectedDrumCorps.setId(1L);
        
        when(mapper.drumCorpsToDrumCorpsDTO(expectedDrumCorps)).thenReturn(responseDTO);

        service.updateDrumCorps(1L, drumCorpsUpdate);

        verify(mapper, times(1)).drumCorpsToDrumCorpsDTO(expectedDrumCorps);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(expectedDrumCorps)
                    .usingRecursiveComparison()
                    .ignoringFields("corpsId", "id")
                    .isEqualTo(responseDTO);
        });
    };

    // unhappy delete, should return not found
    @Test
    public void deleteShouldThrowNotFoundException() {

        when(repo.findById(1L)).thenReturn(Optional.empty());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> {
                service.deleteDrumCorpsById(1L); // test method
            }).hasMessage("Could not find Drum Corps with id:1").isInstanceOf(NotFoundException.class);
        });
    }

    // happy delete, should return drum corps dto
    // test method deleteByDrumCorpsId()
    @Test
    public void deleteShouldReturnDrumCorpsDTO() {

        DrumCorps foundDrumCorps = new DrumCorps(1L, "Troopers", 9,
                LocalDate.of(2000, 7, 21), true,
                LocalDate.of(2008, 7, 21), null);

        // turn it into a dto. mapper
        DrumCorpsDTO expectedDTO = mapper.drumCorpsToDrumCorpsDTO(foundDrumCorps);

        when(repo.findById(1L)).thenReturn(Optional.of(foundDrumCorps));
        when(mapper.drumCorpsToDrumCorpsDTO(foundDrumCorps)).thenReturn(expectedDTO);
        when(repo.deleteByDrumCorpsId(1L)).thenReturn(1);

        DrumCorpsDTO actualDTO = service.deleteDrumCorpsById(1L);
        assertEquals(expectedDTO, actualDTO);
    }
}