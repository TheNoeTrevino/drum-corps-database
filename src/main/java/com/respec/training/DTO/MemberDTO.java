package com.respec.training.DTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MemberDTO {
    private long memberId;
    private ResponseDrumCorpsDTO drumCorps;
    private String firstName;
    private String lastName;
    private int age;
    private LocalDate birthDate;
    private String section;
    private String instrument;
    private String emailAddress;
}
