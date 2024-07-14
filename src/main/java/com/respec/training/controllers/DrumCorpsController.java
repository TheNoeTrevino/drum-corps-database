package com.respec.training.controllers;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.respec.training.DTO.CreateUpdateDrumCorpsDTO;
import com.respec.training.DTO.DrumCorpsDTO;
import com.respec.training.DTO.PageableDTO;
import com.respec.training.services.DrumCorpsService;
import com.respec.training.validations.PageableConstraint;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("drum-corps")
public class DrumCorpsController {

    private static final Logger logger = LoggerFactory.getLogger(DrumCorpsController.class);

    private final DrumCorpsService drumCorpsService;

    @GetMapping("/{id}")
    public DrumCorpsDTO getDrumCorpsById(@PathVariable Long id) {
        logger.info("Searching for Drum Corps with ID: {}", id);
        return drumCorpsService.getDrumCorpsById(id);
    }

    @GetMapping("")
    public ResponseEntity<PageableDTO<DrumCorpsDTO>> getAllDrumCorps(
        @Valid @PageableConstraint(maxPerPage = 50)
        @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String corpsName,
            @RequestParam(required = false) LocalDate dateFounded,
            @RequestParam(required = false) Boolean folded,
            @RequestParam(required = false) Integer numOfChamp,
            @RequestParam(required = false) LocalDate dateFolded,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) LocalDate birthDate,
            @RequestParam(required = false) String section,
            @RequestParam(required = false) String instrument,
            @RequestParam(required = false) String emailAddress) {
        logger.info("Searching for all Drum Corps"); // add sepcifications
        return ResponseEntity.ok(drumCorpsService.getAllDrumCorpsPageable(pageable,
                                corpsName, numOfChamp, dateFounded, folded, dateFolded,
                                firstName, lastName, age, birthDate, section, instrument, emailAddress));
    }

    @PostMapping("")
    public ResponseEntity<DrumCorpsDTO> postDrumCorps(@Valid @RequestBody CreateUpdateDrumCorpsDTO newDrumCorpsDTO) {
        logger.info("Creating Drum Corps Details: {}", newDrumCorpsDTO);
        return ResponseEntity.ok(drumCorpsService.postDrumCorps(newDrumCorpsDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DrumCorpsDTO> updateDrumCorps(@Valid @PathVariable Long id,
            @RequestBody CreateUpdateDrumCorpsDTO drumCorpsDetails) {
        logger.info("Updating Drum Corps with ID: {}, Details: {}", id, drumCorpsDetails);
        return ResponseEntity.ok(drumCorpsService.updateDrumCorps(id, drumCorpsDetails));
    }

    // Delete DrumCorps by corpsId
    @DeleteMapping("/{id}")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<DrumCorpsDTO> deleteById(@PathVariable Long id) {
        logger.info("Deleteing Drum Corps with ID: {}, Details: {}", id);
        return ResponseEntity.ok(drumCorpsService.deleteDrumCorpsById(id));
    }
}
