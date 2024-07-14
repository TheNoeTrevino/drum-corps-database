package com.respec.training.DTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResponseDrumCorpsDTO {
    private long corpsId;
    private String corpsName;
    private int numOfChamp;
    private LocalDate dateFounded;
    private Boolean folded;
    private LocalDate dateFolded;
}