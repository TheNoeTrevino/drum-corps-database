package com.respec.training.controllers;

import org.springframework.data.domain.Pageable;
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

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.respec.training.DTO.CreateUpdateMemberDTO;
import com.respec.training.DTO.MemberDTO;
import com.respec.training.DTO.PageableDTO;
import com.respec.training.services.MemberService;
import com.respec.training.validations.PageableConstraint;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    private final MemberService memberService;
    
    @GetMapping("/{id}")
    public MemberDTO getMemberById(@PathVariable Long id) {
        logger.info("Searching for member with ID: {}", id);
        return memberService.getMemberById(id);
    }

    @GetMapping("")
    public ResponseEntity<PageableDTO<MemberDTO>> getAllMembers(
        @Valid @PageableConstraint(maxPerPage = 50)
        @PageableDefault(page = 0, size = 25, sort = "id") Pageable pageable,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) LocalDate birthDate,
            @RequestParam(required = false) String section,
            @RequestParam(required = false) String instrument,
            @RequestParam(required = false) String emailAddress,
            @RequestParam(required = false) Long corpsId,
            @RequestParam(required = false) String corpsName,
            @RequestParam(required = false) LocalDate dateFounded,
            @RequestParam(required = false) Boolean folded,
            @RequestParam(required = false) Integer numOfChamp,
            @RequestParam(required = false) LocalDate dateFolded) {
        logger.info("Searching for all members");
        return ResponseEntity.ok(memberService.getAllMembers(pageable, 
            firstName, lastName, age, birthDate, section, instrument, emailAddress,
            corpsId, corpsName, dateFounded, folded, numOfChamp, dateFolded));
    }

    @PostMapping("")
    public ResponseEntity<MemberDTO> postMember(@Valid @RequestBody CreateUpdateMemberDTO newMemberDTO) {
        logger.info("Creating Drum Corps Details: {}", newMemberDTO);
        return ResponseEntity.ok(memberService.postMember(newMemberDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MemberDTO> updateMember(@Valid @PathVariable Long id,
            @RequestBody CreateUpdateMemberDTO memberDetails) {
        logger.info("Updating Member with ID: {}, Details: {}", id, memberDetails);
        return ResponseEntity.ok(memberService.updateMember(id, memberDetails));
    }

    @DeleteMapping("/{id}")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<MemberDTO> deleteById(@PathVariable Long id) {
        logger.info("Deleteing member with ID: {}", id);
        return ResponseEntity.ok(memberService.deleteMemberById(id));
    }
}