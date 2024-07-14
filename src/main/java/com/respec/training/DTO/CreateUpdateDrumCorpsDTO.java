package com.respec.training.DTO;

import java.time.LocalDate;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateUpdateDrumCorpsDTO {

    @Size(max = 255, message = "The name of this Drum Corps is too long. Please submit a name under 255 characters.")
    @NotNull(message = "Corps Name can not be null.")
    private String corpsName;

    @Min(value = 0, message = "You can not win negative times")
    @Max(value = 21, message = "The Blue Devils have the most championships, at 21. This can not be true.")
    @NotNull(message = "The number of championships can not be null.")
    private int numOfChamp;

    @PastOrPresent(message = "Date Founded must be in the past, or the current day.")
    @NotNull(message = "Date founded can not be null.")
    private LocalDate dateFounded;

    @NotNull(message = "Folded can not be null.")
    private Boolean folded;

    @PastOrPresent(message = "Date Folded must be in the past, or today.")
    private LocalDate dateFolded;
}