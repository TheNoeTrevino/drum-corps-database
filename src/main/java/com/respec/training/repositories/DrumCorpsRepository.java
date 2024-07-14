package com.respec.training.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.respec.training.models.DrumCorps;

public interface DrumCorpsRepository extends JpaRepository<DrumCorps, Long>,
        JpaSpecificationExecutor<DrumCorps>{
            
    @Modifying
    @Query("delete DrumCorps d where d.id = :id")
    int deleteByDrumCorpsId(@Param(value = "id") Long id);
}