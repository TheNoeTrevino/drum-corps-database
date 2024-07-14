package com.respec.training.models;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "drum_corps")
@Data
public class DrumCorps {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Size(max = 255, message = "The name of this Drum Corps is too long. Please submit a name under 255 characters.")
    @NotNull(message = "Corps Name can not be null.")
    @Column(name = "corps_name")
    private String corpsName;

    @Min(value = 0, message = "You can not win negative times")
    @Max(value = 21, message = "The Blue Devils have the most championships, at 21. This can not be true.")
    @NotNull(message = "The number of championships can not be null.")
    @Column(name = "num_of_championships")
    private int numOfChamp;

    @PastOrPresent(message = "Date Founded must be in the past, or the current day.")
    @NotNull(message = "Date founded can not be null.")
    @Column(name = "date_founded")
    private LocalDate dateFounded;

    @NotNull(message = "Folded can not be null.")
    @Column(name = "folded")
    private Boolean folded;

    @PastOrPresent(message = "Date Folded must be in the past, or today.")
    @Column(name = "date_folded")
    private LocalDate dateFolded;

    @OneToMany(mappedBy="drumCorps")
    private List<Member> members;

    public DrumCorps(String corpsName, Integer numOfChamp, LocalDate dateFounded,
                     Boolean folded, LocalDate dateFolded, List<Member> members) {
                        
        this.corpsName = corpsName;
        this.numOfChamp = numOfChamp;
        this.dateFounded = dateFounded;
        this.folded = folded;
        this.dateFolded = dateFolded;
        this.members = members;
    }
}