package com.respec.training.DTO;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
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
public class CreateUpdateMemberDTO {

    @NotNull(message = "Drum Corps ID can not be null.")
    private Long drumCorpsId;

    @Size(max = 255, message = "The First Name of this member is too long. Please submit a First Name under 255 characters.")
    @NotNull(message = "First Name can not be null.")
    private String firstName;

    @Size(max = 255, message = "The Last Name of this member is too long. Please submit a Last Name under 255 characters.")
    @NotNull(message = "Last Name can not be null.")
    private String lastName;

    @Min(value = 0, message = "Age cannot be a negative value")
    @Max(value = 22, message = "The age limit for this activity is 21, unless you were born in the months of June or July")
    @NotNull(message = "Age can not be null.")
    private int age;

    @PastOrPresent(message = "Birthdate must be in the past, or the current day.")
    @NotNull(message = "Birthdate can not be null.")
    private LocalDate birthDate;

    @Size(max = 255, message = "The name of this section is too long. Please submit an entry under 255 characters.")
    @NotNull(message = "Section can not be null.")
    private String section;

    @Size(max = 255, message = "The name of this instrument is too long. Please submit an entry under 255 characters.")
    @NotNull(message = "instrument can not be null.")
    private String instrument;
    
    @Size(max = 255, message = "The name of this email address is too long. Please submit an entry under 255 characters.")
    @NotNull(message = "Email can not be null.")
    @Email(message = "Email must be formatted. For Example: \n'john.doe@gmail.com'")
    private String emailAddress;
}
