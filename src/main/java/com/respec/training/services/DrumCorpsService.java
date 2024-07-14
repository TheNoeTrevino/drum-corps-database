package com.respec.training.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.respec.training.DTO.CreateUpdateDrumCorpsDTO;
import com.respec.training.DTO.DrumCorpsDTO;
import com.respec.training.DTO.PageableDTO;
import com.respec.training.exceptions.NotFoundException;
import com.respec.training.mappers.DrumCorpsMapper;
import com.respec.training.models.DrumCorps;
import com.respec.training.models.Member;
import com.respec.training.repositories.DrumCorpsRepository;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@Component("drumCorpsService")
@RequiredArgsConstructor
public class DrumCorpsService {
    
    private final DrumCorpsRepository repo;
    
    private final DrumCorpsMapper mapper;

    public DrumCorpsDTO getDrumCorpsById(Long id) {

        DrumCorpsDTO drumCorpsDTO = mapper.drumCorpsToDrumCorpsDTO(repo.findById(id)
            .orElseThrow(() -> new NotFoundException("Drum Corps", id)));
        
        return drumCorpsDTO;
    }

    public PageableDTO<DrumCorpsDTO> getAllDrumCorpsPageable(Pageable pageable, String corpsName, Integer numOfChamp,
                                            LocalDate dateFounded, Boolean folded, LocalDate dateFolded,
                                            String firstName, String lastName, Integer age, LocalDate birthDate, 
                                            String section, String instrument, String emailAddress) {

        Specification<DrumCorps> spec = Specification.where(null);

        if (corpsName != null) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("corpsName")), corpsName.toLowerCase()));
        }

        if (numOfChamp != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("numOfChamp"), numOfChamp));
        }

        if (dateFounded != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("dateFounded"), dateFounded));
        }

        if (folded != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("folded"), folded));
        }

        if (dateFolded != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("dateFolded"), dateFolded));
        }

        spec = spec.and((root, query, cb) -> {

            Join<DrumCorps, Member> memberJoin = root.join("members", JoinType.LEFT);

            Predicate memberPredicate = cb.conjunction();

            if (firstName != null) {
                memberPredicate = cb.and(memberPredicate, cb.like(cb.lower(memberJoin.get("firstName")), firstName.toLowerCase()));
            }
        
            if (lastName != null) {
                memberPredicate = cb.and(memberPredicate, cb.like(cb.lower(memberJoin.get("lastName")), lastName.toLowerCase()));
            }
        
            if (age != null) {
                memberPredicate = cb.and(memberPredicate, cb.equal(memberJoin.get("age"), age));
            }
        
            if (birthDate != null) {
                memberPredicate = cb.and(memberPredicate, cb.equal(memberJoin.get("birthDate"), birthDate));
            }
        
            if (section != null) {
                memberPredicate = cb.and(memberPredicate, cb.like(cb.lower(memberJoin.get("section")), section.toLowerCase()));
            }
        
            if (instrument != null) {
                memberPredicate = cb.and(memberPredicate, cb.like(cb.lower(memberJoin.get("instrument")), instrument.toLowerCase()));
            }
        
            if (emailAddress != null) {
                memberPredicate = cb.and(memberPredicate, cb.like(memberJoin.get("emailAddress"), emailAddress));
            }
            
            return memberPredicate;
        });

        Page<DrumCorps> drumCorpsPage = repo.findAll(spec, pageable);

        List<DrumCorpsDTO> dtos = drumCorpsPage.map(mapper::drumCorpsToDrumCorpsDTO).getContent();

        PageableDTO<DrumCorpsDTO> responsePage = new PageableDTO<>();
        responsePage.setContent(dtos);
        responsePage.setPageNumber(drumCorpsPage.getNumber());
        responsePage.setPageSize(drumCorpsPage.getSize());
        responsePage.setTotalPages(drumCorpsPage.getTotalPages());
        return responsePage;
    }

    public DrumCorpsDTO postDrumCorps(CreateUpdateDrumCorpsDTO newDrumCorpsDTO) {

        DrumCorps drumCorps = mapper.createUpdateDrumCorpsDTOToDrumCorps(newDrumCorpsDTO);
        drumCorps = repo.save(drumCorps);

        DrumCorpsDTO responseDTO = mapper.drumCorpsToDrumCorpsDTO(drumCorps);
        return responseDTO;
    }

    public DrumCorpsDTO updateDrumCorps(Long id, CreateUpdateDrumCorpsDTO drumCorpsDetails) {

        repo.findById(id).orElseThrow(() -> new NotFoundException("Drum Corps", id));

        // Map dto to the already existing entity
        DrumCorps updatedDrumCorps = mapper.createUpdateDrumCorpsDTOToDrumCorps(drumCorpsDetails);
        updatedDrumCorps.setId(id); // to keep original id, avoid autogeneration
        repo.save(updatedDrumCorps);

        DrumCorpsDTO responseDTO = mapper.drumCorpsToDrumCorpsDTO(updatedDrumCorps);
        return responseDTO;
    }
    
    public DrumCorpsDTO deleteDrumCorpsById(Long id) {
        
        // Map corps into DTO
        DrumCorpsDTO drumCorpsDTO = mapper.drumCorpsToDrumCorpsDTO(repo.findById(id).
            orElseThrow(() -> new NotFoundException("Drum Corps", id)));

        repo.deleteByDrumCorpsId(id);
        
        return drumCorpsDTO;
    }
}
