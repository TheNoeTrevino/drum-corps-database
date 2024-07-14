package com.respec.training.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.respec.training.models.Member;


public interface MemberRepository extends JpaRepository<Member, Long>,
        JpaSpecificationExecutor<Member>{
    
    @Modifying
    @Query("delete Member m where m.id = :id")
    int deleteByMemberId(@Param(value = "id") Long id);
}