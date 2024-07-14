package com.respec.training.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.respec.training.DTO.CreateUpdateMemberDTO;
import com.respec.training.DTO.MemberDTO;
import com.respec.training.DTO.PageableDTO;
import com.respec.training.exceptions.NotFoundException;
import com.respec.training.mappers.MemberMapper;
import com.respec.training.models.DrumCorps;
import com.respec.training.models.Member;
import com.respec.training.repositories.MemberRepository;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;

@Service
@Component("memberService")
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository repo;

    private final MemberMapper mapper;

    public MemberDTO getMemberById(Long id) {

        MemberDTO memberDTO = mapper.memberToMemberDTO(repo.findById(id)
                .orElseThrow(() -> new NotFoundException("member", id)));

        return memberDTO;
    }

    public PageableDTO<MemberDTO> getAllMembers(Pageable pageable, String firstName, String lastName,
            Integer age, LocalDate birthDate, String section, String instrument, String emailAddress,
            Long corpsId, String corpsName, LocalDate dateFounded, Boolean folded, Integer numOfChamp, LocalDate dateFolded) {

        Specification<Member> spec = Specification.where(null);

        if (firstName != null) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("firstName")), firstName.toLowerCase()));
        }

        if (lastName != null) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("lastName")), lastName.toLowerCase()));
        }

        if (age != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("age"), age));
        }

        if (birthDate != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("birthDate"), birthDate));
        }

        if (section != null) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("section")), section.toLowerCase()));
        }

        if (instrument != null) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("instrument")), instrument.toLowerCase()));
        }

        if (emailAddress != null) {
            spec = spec.and((root, query, cb) -> cb.like(root.get("emailAddress"), emailAddress));
        }

        spec = spec.and((root, query, cb) -> {

            Join<Member, DrumCorps> corpsJoin = root.join("drumCorps");

            Predicate corpsPredicate = cb.conjunction();

            if (corpsId != null) {
                corpsPredicate = cb.and(corpsPredicate, cb.equal(corpsJoin.get("id"), corpsId));
            }

            if (corpsName != null) {
                corpsPredicate = cb.and(corpsPredicate, cb.like(cb.lower(corpsJoin.get("name")), corpsName.toLowerCase()));
            }

            if (numOfChamp != null) {
                corpsPredicate = cb.and(corpsPredicate, cb.equal(corpsJoin.get("numOfChamp"), numOfChamp));
            }

            if (dateFounded != null) {
                corpsPredicate = cb.and(corpsPredicate, cb.equal(corpsJoin.get("dateFounded"), dateFounded));
            }

            if (folded != null) {
                corpsPredicate = cb.and(corpsPredicate, cb.equal(corpsJoin.get("folded"), folded));
            }

            if (dateFolded != null) {
                corpsPredicate = cb.and(corpsPredicate, cb.equal(corpsJoin.get("dateFolded"), dateFolded));
            }

            return corpsPredicate;
        });

        Page<Member> memberPage = repo.findAll(spec, pageable);

        List<MemberDTO> memberDTOPage = memberPage.map(mapper::memberToMemberDTO).getContent();

        PageableDTO<MemberDTO> responsePage = new PageableDTO<>();
        responsePage.setContent(memberDTOPage);
        responsePage.setPageNumber(memberPage.getNumber());
        responsePage.setPageSize(memberPage.getSize());
        responsePage.setTotalPages(memberPage.getTotalPages());
        return responsePage;
    }

    public MemberDTO postMember(CreateUpdateMemberDTO newMemberDTO) {

        Member member = mapper.createUpdateMemberDTOToMember(newMemberDTO);
        member = repo.save(member);

        MemberDTO responseDTO = mapper.memberToMemberDTO(member);
        return responseDTO;
    }

    public MemberDTO updateMember(Long id, CreateUpdateMemberDTO memberDetails) {

        repo.findById(id).orElseThrow(() -> new NotFoundException("member", id)); // test for not found in patch

        // Map dto to the already existing entity
        Member updatedMember = mapper.createUpdateMemberDTOToMember(memberDetails);
        updatedMember.setId(id); // to keep original id, avoid autogeneration
        repo.save(updatedMember);

        MemberDTO responseDTO = mapper.memberToMemberDTO(updatedMember);
        return responseDTO;
    }

    public MemberDTO deleteMemberById(Long id) {

        // Map corps into DTO
        MemberDTO memberDTO = mapper.memberToMemberDTO(repo.findById(id).
            orElseThrow(() -> new NotFoundException("member", id)));

        repo.deleteByMemberId(id);

        return memberDTO;
    }
}