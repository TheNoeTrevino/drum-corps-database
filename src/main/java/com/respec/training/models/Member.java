package com.respec.training.models;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "members")
@Data
public class Member {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Size(max = 255, message = "The First Name of this member is too long. Please submit a First Name under 255 characters.")
    @NotNull(message = "First Name can not be null.")
    @Column(name = "first_name")
    private String firstName;

    @Size(max = 255, message = "The Last Name of this member is too long. Please submit a Last Name under 255 characters.")
    @NotNull(message = "Last Name can not be null.")
    @Column(name = "last_name")
    private String lastName;

    @Min(value = 0, message = "Age cannot be a negative value")
    @Max(value = 22, message = "The age limit for this activity is 21, unless you were born in the months of June or July")
    @NotNull(message = "Age can not be null.")
    @Column(name = "age")
    private int age;

    @PastOrPresent(message = "Birthdate must be in the past, or the current day.")
    @NotNull(message = "Birthdate can not be null.")
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Size(max = 255, message = "The name of this section is too long. Please submit an entry under 255 characters.")
    @NotNull(message = "Section can not be null.")
    @Column(name = "section")
    private String section;

    @Size(max = 255, message = "The name of this instrument is too long. Please submit an entry under 255 characters.")
    @NotNull(message = "instrument can not be null.")
    @Column(name = "instrument")
    private String instrument;
    
    @Size(max = 255, message = "The name of this email address is too long. Please submit an entry under 255 characters.")
    @NotNull(message = "Email can not be null.")
    @Email(message = "Email must be well formed")
    @Column(name = "email_address")
    private String emailAddress;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "drum_corps", nullable = false)
    @JsonIgnore
    private DrumCorps drumCorps;

    public Member(String firstName, String lastName, int age, LocalDate birthDate, String section,
                    String instrument, String emailAddress, DrumCorps drumCorps) {
       
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.birthDate = birthDate;
        this.section = section;
        this.instrument = instrument;
        this.emailAddress = emailAddress;
        this.drumCorps = drumCorps;
    }
}
