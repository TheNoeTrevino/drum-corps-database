package com.respec.training.initalization;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import com.github.javafaker.Faker;
import com.respec.training.models.DrumCorps;
import com.respec.training.models.Member;
import com.respec.training.repositories.DrumCorpsRepository;
import com.respec.training.repositories.MemberRepository;

@Configuration
public class InitDatabase {

    @Autowired
    private DrumCorpsRepository corpsRepo;

    @Autowired
    private MemberRepository memberRepo;

    Faker faker = new Faker();
    Random random = new Random();

    List<String> sectionLibrary = Arrays.asList(
        "Brass", "Colorguard", "Front Ensemble", "Battery"
    );

    List<String> descriptionLibrary = Arrays.asList(
        "Troopers", "Guards", "Brigade", "Legion", "Battalion",
        "Rangers", "Sentinels", "Regiment", "Vanguard", "Cavaliers",
        "Stars", "Knights", "Phantoms", "Guardians", "Pioneers", "Crusaders"
    );
    
    List<String> brassLibrary = Arrays.asList(
        "Baritone", "Contra", "Trumpet", "Mellophone", "Euphonium"
    );

    List<String> colorguardLibrary = Arrays.asList(
        "Rifle", "Saber", "Flag"
    );

    List<String> batteryLibrary = Arrays.asList(
        "Snare Drum", "Tenor Drums", "Bass Drum"
    );

    List<String> frontEnsembleLibrary = Arrays.asList(
        "Marimba", "Xylophone", "Vibraphone"
    );


    @Transactional(rollbackFor = Exception.class)
    // @Bean
    public InitDatabase generateSampleData() {
        
        for (int i = 0; i < 100; i++) {

            Instant start = Instant.now();
            for (int j = 0; j < 100; j++) {

                //Generating Drum Corps info
                String fakeCorpsName = getFakeCorpsName();
                int fakeNumOfChamp = random.nextInt(0, 21);
                LocalDate fakeDateFounded = getFakeDateFounded();
                Boolean fakeFolded = faker.bool().bool();
                LocalDate fakeDateFolded = null;
                
                if (fakeFolded) {
                    fakeDateFolded = getFakeDateFolded(fakeDateFounded);
                }
                
                DrumCorps sampleDrumCorps = new DrumCorps(fakeCorpsName, fakeNumOfChamp,
                    fakeDateFounded, fakeFolded, fakeDateFolded, new ArrayList<Member>());

                corpsRepo.save(sampleDrumCorps);

                List<Member> sampleMemberList = new ArrayList<Member>();
                for (int k = 0; k < random.nextInt(125, 150); k++) {

                    // Generating Member info
                    String fakeFirstName = faker.name().firstName();
                    String fakeLastName = faker.name().lastName();
                    int fakeAge = random.nextInt(18, 22);
                    LocalDate fakeBirthdate = getBirthdate(fakeAge);
                    String fakeSection = sectionLibrary.get(random.nextInt(sectionLibrary.size()));
                    String fakeInstrument = getInstrument(fakeSection);
                    String fakeEmailAddress = faker.internet().emailAddress();

                    Member sampleMember = new Member(fakeFirstName, fakeLastName, fakeAge, fakeBirthdate,
                        fakeSection, fakeInstrument, fakeEmailAddress, sampleDrumCorps);

                    sampleMemberList.add(sampleMember);
                }
                memberRepo.saveAll(sampleMemberList);
                sampleDrumCorps.setMembers(sampleMemberList);
            }
            Instant end = Instant.now();
            Long timeTook = Duration.between(start, end).toMillis();
            System.out.println(String.format("%s/10000 - took %sms", i, timeTook.toString()));

        }
    return null;
    }
    
    // Data generation functions
    private String getFakeCorpsName() {
        String city = faker.address().city();
        String description = descriptionLibrary.get(random.nextInt(descriptionLibrary.size()));
        return city + " " + description;
    }

    private LocalDate getFakeDateFounded() {
        LocalDate fakeDateFounded = faker.date().past(200 * 365, 50 * 365, TimeUnit.DAYS).toInstant()
        .atZone(ZoneId.systemDefault()).toLocalDate();
        return fakeDateFounded;
    }
    
    private LocalDate getFakeDateFolded(LocalDate fakeDateFounded) {
        Date startDate = Date.from(fakeDateFounded.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date fakeDateFoldedUnformatted = faker.date().past(50 * 365, TimeUnit.DAYS, startDate);
        LocalDate fakeDateFolded = fakeDateFoldedUnformatted.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return fakeDateFolded;
    }

    private LocalDate getBirthdate(int age) {
        int ageInDays = (age * 365);
        Date fakeUnformattedBirthdate = faker.date().past(ageInDays, TimeUnit.DAYS);
        LocalDate fakeBirthdate = fakeUnformattedBirthdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return fakeBirthdate;
    }

    private String getInstrument(String fakeSection) {

        if (fakeSection == "Brass") {
            return brassLibrary.get(random.nextInt(brassLibrary.size()));

        } else if (fakeSection == "Colorguard") {
            return colorguardLibrary.get(random.nextInt(colorguardLibrary.size()));

        } else if (fakeSection == "Front Ensemble") {
            return frontEnsembleLibrary.get(random.nextInt(frontEnsembleLibrary.size()));

        } else if (fakeSection == "Battery") {
            return batteryLibrary.get(random.nextInt(batteryLibrary.size()));

        } else {
            return "Drum Major";
        }
    }
}
