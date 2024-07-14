package com.respec.training.DTO;

import java.time.LocalDate;
import java.util.List;

import com.respec.training.models.Member;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DrumCorpsDTO {
    private long corpsId;
    private String corpsName;
    private int numOfChamp;
    private LocalDate dateFounded;
    private Boolean folded;
    private LocalDate dateFolded;
    private List<Member> members;
}