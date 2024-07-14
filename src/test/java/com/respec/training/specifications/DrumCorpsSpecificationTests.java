package com.respec.training.specifications;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.DirtiesContext;

import com.respec.training.models.DrumCorps;
import com.respec.training.repositories.DrumCorpsRepository;
import com.respec.training.services.DrumCorpsService;

import static org.assertj.core.api.Assertions.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // Clears generated ID
@DataJpaTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class DrumCorpsSpecificationTests {

    @Autowired
    private DrumCorpsRepository repo;

    @MockBean
    private DrumCorpsService service;
    
    private DrumCorps.DrumCorpsBuilder createCorpsBuilder()  {

        return DrumCorps.builder()
            .corpsName("Corps")
            .numOfChamp(10)
            .dateFounded(LocalDate.of(1980, 1, 1))
            .folded(false)
            .dateFolded(null);
    }

    // // // Happy Path Tests \\ \\ \\

    // Page Size and Number
    @Test
    public void testPageSize() {

        final List<DrumCorps> mockDrumCorpsList = IntStream.range(0, 5)
            .mapToObj(i -> createCorpsBuilder()
                .corpsName("Corps" + i)
                .build())
            .collect(Collectors.toList());
        
        repo.saveAll(mockDrumCorpsList);

        PageRequest firstPage = PageRequest.of(0, 2);
        PageRequest secondPage = PageRequest.of(1, 2);

        Page<DrumCorps> firstPageDrumCorps = repo.findAll(firstPage);
        Page<DrumCorps> secondPageDrumCorps = repo.findAll(secondPage);

        assertThat(firstPageDrumCorps.getContent()).hasSize(2).contains(repo.getReferenceById(1L), atIndex(0));
        assertThat(firstPageDrumCorps.getContent()).hasSize(2).contains(repo.getReferenceById(2L), atIndex(1));
        assertThat(secondPageDrumCorps.getContent()).hasSize(2).contains(repo.getReferenceById(3L), atIndex(0));
        assertThat(secondPageDrumCorps.getContent()).hasSize(2).contains(repo.getReferenceById(4L), atIndex(1));
    }

    // Sort numOfChamp ASC
    @Test
    public void testSortASC() {

        Function<Integer, DrumCorps> createByNumOfChamp = (val) -> createCorpsBuilder()
            .numOfChamp(val)
            .build();
            
        DrumCorps corps1 = createByNumOfChamp.apply(1);
        DrumCorps corps2 = createByNumOfChamp.apply(2);
        DrumCorps corps3 = createByNumOfChamp.apply(3);
        DrumCorps corps4 = createByNumOfChamp.apply(4);
        DrumCorps corps5 = createByNumOfChamp.apply(5);

        List<DrumCorps> mockDrumCorpsList = Arrays.asList(corps5, corps2, corps4, corps1, corps3);

        repo.saveAll(mockDrumCorpsList);

        PageRequest page = PageRequest.of(0, 5, Sort.by("numOfChamp").ascending());

        Page<DrumCorps> pageDrumCorps  = repo.findAll(page);

        assertThat(pageDrumCorps.getContent()).hasSize(5).containsExactly(corps1, corps2, corps3, corps4, corps5);
    }

    // Sort numOfChamp DESC
    @Test
    public void testSortDESC() {
        
        Function<Integer, DrumCorps> createByChampNum = (val) -> createCorpsBuilder()
            .numOfChamp(val)
            .build();
            
        DrumCorps corps1 = createByChampNum.apply(1);
        DrumCorps corps2 = createByChampNum.apply(2);
        DrumCorps corps3 = createByChampNum.apply(3);
        DrumCorps corps4 = createByChampNum.apply(4);
        DrumCorps corps5 = createByChampNum.apply(5);

        List<DrumCorps> mockDrumCorpsList = Arrays.asList(corps5, corps2, corps4, corps1, corps3);

        repo.saveAll(mockDrumCorpsList);

        PageRequest page = PageRequest.of(0, 5, Sort.by("numOfChamp").descending());

        Page<DrumCorps> pageDrumCorps  = repo.findAll(page);

        assertThat(pageDrumCorps.getContent()).hasSize(5).containsExactly(corps5, corps4, corps3, corps2, corps1);
    }

    // Sort by name
    @Test
    public void testSortByCorpsName() {

        Function<String, DrumCorps> createByCorpsName = (val) -> createCorpsBuilder()
            .corpsName(val)
            .build();

        DrumCorps corpsA = createByCorpsName.apply("A");
        DrumCorps corpsBA = createByCorpsName.apply("BA");
        DrumCorps corpsBC = createByCorpsName.apply("BC");
        DrumCorps corpsD = createByCorpsName.apply("D");
        DrumCorps corpsE = createByCorpsName.apply("E");

        List<DrumCorps> mockDrumCorpsList = Arrays.asList(corpsBC, corpsA, corpsE, corpsBA, corpsD);

        repo.saveAll(mockDrumCorpsList);

        PageRequest page = PageRequest.of(0, 5, Sort.by("corpsName").ascending());

        Page<DrumCorps> pageDrumCorps  = repo.findAll(page);

        assertThat(pageDrumCorps.getContent()).hasSize(5).containsExactly(corpsA, corpsBA, corpsBC, corpsD, corpsE);
    }

    // Sort by Date Founded
    @Test
    public void testSortByDateFounded() {

        Function<LocalDate, DrumCorps> createByDateFounded = (val) -> createCorpsBuilder()
            .dateFounded(val)
            .build();

        DrumCorps corps1981 = createByDateFounded.apply(LocalDate.of(1981, 1, 1));
        DrumCorps corps1982 = createByDateFounded.apply(LocalDate.of(1982, 1, 1));
        DrumCorps corps1983 = createByDateFounded.apply(LocalDate.of(1983, 1, 1));
        DrumCorps corps1984 = createByDateFounded.apply(LocalDate.of(1984, 1, 1));
        DrumCorps corps1985 = createByDateFounded.apply(LocalDate.of(1985, 1, 1));

        List<DrumCorps> mockDrumCorpsList = Arrays.asList(corps1983, corps1981, corps1985, corps1982, corps1984);

        repo.saveAll(mockDrumCorpsList);

        PageRequest page = PageRequest.of(0, 5, Sort.by("dateFounded"));

        Page<DrumCorps> pageDrumCorps  = repo.findAll(page);

        assertThat(pageDrumCorps.getContent()).hasSize(5).containsExactly(corps1981, corps1982, corps1983, corps1984, corps1985);
    }

    // Sort by Folded
    @Test
    public void testSortByFolded() {

        Function<Boolean, DrumCorps> createByFolded = (val) -> createCorpsBuilder()
            .folded(val)
            .build();

        DrumCorps corpsTrue1 = createByFolded.apply(true);
        DrumCorps corpsTrue2 = createByFolded.apply(true);
        DrumCorps corpsTrue3 = createByFolded.apply(true);
        DrumCorps corpsFalse4 = createByFolded.apply(false);
        DrumCorps corpsFalse5 = createByFolded.apply(false);

        List<DrumCorps> mockDrumCorpsList = new ArrayList<DrumCorps>();

        mockDrumCorpsList.addAll(Arrays.asList(corpsTrue1, corpsTrue2, corpsTrue3, corpsFalse4, corpsFalse5));

        repo.saveAll(mockDrumCorpsList);

        PageRequest page = PageRequest.of(0, 5, Sort.by("folded"));
        Page<DrumCorps> pageDrumCorps  = repo.findAll(page);

        // false, then true
        assertThat(pageDrumCorps.getContent()).hasSize(5).containsExactly(corpsFalse4, corpsFalse5, corpsTrue1, corpsTrue2, corpsTrue3);
    }

    // Sort by Date Folded
    @Test
    public void testSortByDateFolded() {

        Function<LocalDate, DrumCorps> createByDateFolded = (val) -> createCorpsBuilder()
            .dateFolded(val)
            .build();

        DrumCorps corps1981 = createByDateFolded.apply(LocalDate.of(1981, 1, 1));
        DrumCorps corps1982 = createByDateFolded.apply(LocalDate.of(1982, 1, 1));
        DrumCorps corps1983 = createByDateFolded.apply(LocalDate.of(1983, 1, 1));
        DrumCorps corps1984 = createByDateFolded.apply(LocalDate.of(1984, 1, 1));
        DrumCorps corps1985 = createByDateFolded.apply(LocalDate.of(1985, 1, 1));

        List<DrumCorps> mockDrumCorpsList = Arrays.asList(corps1983, corps1981, corps1985, corps1982, corps1984);

        repo.saveAll(mockDrumCorpsList);

        PageRequest page = PageRequest.of(0, 5, Sort.by("dateFolded"));
        Page<DrumCorps> pageDrumCorps  = repo.findAll(page);

        assertThat(pageDrumCorps.getContent()).hasSize(5).containsExactly(corps1981, corps1982, corps1983, corps1984, corps1985);
    }

    // Search by name specification
    @Test
    public void testSearchByCorpsName() {

        DrumCorps corpsTroop = createCorpsBuilder()
            .corpsName("Troop")
            .build();

        DrumCorps corpsWrong = createCorpsBuilder()
            .corpsName("Wrong")
            .build();

        repo.save(corpsTroop);
        repo.save(corpsWrong);

        Specification<DrumCorps> Spec = DrumCorpsSpecifications.queryCorpsName("Troop");


        Iterable<DrumCorps> repoResults = repo.findAll(Spec);

        assertThat(repoResults).hasSize(1).contains(corpsTroop);
    }

    // Search by numOfChamp specification
    @Test
    public void testSearchByNumOfChamp() {

        DrumCorps corps10 = createCorpsBuilder()
            .numOfChamp(10)
            .build();

        DrumCorps corps9 = createCorpsBuilder()
            .numOfChamp(9)
            .build();

        repo.save(corps10);
        repo.save(corps9);

        Specification<DrumCorps> Spec = DrumCorpsSpecifications.queryNumOfChamps(10);

        Iterable<DrumCorps> repoResults = repo.findAll(Spec);

        assertThat(repoResults).hasSize(1).contains(corps10);
    }

    // Search by dateFounded specification
    @Test
    public void testSearchByDateFounded() {

        DrumCorps corps2000 = createCorpsBuilder()
            .dateFounded(LocalDate.of(2000, 1, 1))
            .build();

        DrumCorps corps1999 = createCorpsBuilder()
            .dateFounded(LocalDate.of(1999, 1, 1))
            .build();

        repo.save(corps2000);
        repo.save(corps1999);

        Specification<DrumCorps> Spec = DrumCorpsSpecifications.queryDateFounded(LocalDate.of(2000, 1, 1));

        Iterable<DrumCorps> repoResults = repo.findAll(Spec);

        assertThat(repoResults).hasSize(1).contains(corps2000);
    }

    // Search by folded specification
    @Test
    public void testSearchByFolded() {

        DrumCorps corpsTrue = createCorpsBuilder()
            .folded(true)
            .build();

        DrumCorps corpsFalse = createCorpsBuilder()
            .folded(false)
            .build();

        repo.save(corpsTrue);
        repo.save(corpsFalse);

        Specification<DrumCorps> Spec = DrumCorpsSpecifications.isFolded(true);

        Iterable<DrumCorps> repoResults = repo.findAll(Spec);

        assertThat(repoResults).hasSize(1).contains(corpsTrue);
    }

    // Search by dateFolded specification
    @Test
    public void testSearchByDateFolded() {

        DrumCorps corps2000 = createCorpsBuilder()
            .dateFolded(LocalDate.of(2000, 1, 1))
            .build();
        
        DrumCorps corps1999 = createCorpsBuilder()
            .dateFolded(LocalDate.of(1999, 1, 1))
            .build();

        repo.save(corps2000);
        repo.save(corps1999);

        Specification<DrumCorps> Spec = DrumCorpsSpecifications.queryDateFolded(LocalDate.of(2000, 1, 1));
    
        Iterable<DrumCorps> repoResults = repo.findAll(Spec);
    
        assertThat(repoResults).hasSize(1).contains(corps2000);
    }

    @Test
    public void testDateFoldedIsNull() {

        DrumCorps corps2000 = createCorpsBuilder()
            .dateFolded(LocalDate.of(2000, 1, 1))
            .build();
        
        DrumCorps corps1999 = createCorpsBuilder()
            .dateFolded(LocalDate.of(1999, 1, 1))
            .build();

        repo.save(corps2000);
        repo.save(corps1999);

        Specification<DrumCorps> spec = DrumCorpsSpecifications.queryDateFolded(null);

        Iterable<DrumCorps> repoResult = repo.findAll(spec);

        assertThat(repoResult).hasSize(2).containsExactlyInAnyOrder(corps1999, corps2000);
    }


    // // // Unhappy Path Tests \\ \\ \\
        // Search by name specification
        @Test
        public void unhappyTestSearchByCorpsName() {
    
            DrumCorps corpsExists1 = createCorpsBuilder()
                .corpsName("Troop")
                .build();
    
            DrumCorps corpsExists2 = createCorpsBuilder()
                .corpsName("")
                .build();
    
            repo.save(corpsExists1);
            repo.save(corpsExists2);
    
            Specification<DrumCorps> Spec = DrumCorpsSpecifications.queryCorpsName("NOT_TROOP");

            Iterable<DrumCorps> repoResults = repo.findAll(Spec);
    
            assertThat(repoResults).hasSize(0);
    }
    
        // Search by numOfChamp specification
        @Test
        public void unhappyTestSearchByNumOfChamp() {
    
            DrumCorps corps10 = createCorpsBuilder()
                .numOfChamp(10)
                .build();
    
            DrumCorps corps9 = createCorpsBuilder()
                .numOfChamp(9)
                .build();
    
            repo.save(corps10);
            repo.save(corps9);
    
            Specification<DrumCorps> Spec = DrumCorpsSpecifications.queryNumOfChamps(20);

    
            Iterable<DrumCorps> repoResults = repo.findAll(Spec);
    
            assertThat(repoResults).hasSize(0);
    }
    
        // Search by dateFounded specification
        @Test
        public void unhappyTestSearchByDateFounded() {
    
            DrumCorps corps2000 = createCorpsBuilder()
                .dateFounded(LocalDate.of(2000, 1, 1))
                .build();
    
            DrumCorps corps1999 = createCorpsBuilder()
                .dateFounded(LocalDate.of(1999, 1, 1))
                .build();
    
            repo.save(corps2000);
            repo.save(corps1999);
    
            Specification<DrumCorps> Spec = DrumCorpsSpecifications.queryDateFounded(LocalDate.of(2020, 12, 30));
    
            Iterable<DrumCorps> repoResults = repo.findAll(Spec);
    
            assertThat(repoResults).hasSize(0).contains();
    }
    
        // Search by folded specification
        @Test
        public void unhappyTestSearchByFolded() {
    
            DrumCorps corpsTrueA = createCorpsBuilder()
                .folded(true)
                .build();
    
            DrumCorps corpsTrueB = createCorpsBuilder()
                .folded(true)
                .build();
    
            repo.save(corpsTrueA);
            repo.save(corpsTrueB);
    
            Specification<DrumCorps> Spec = DrumCorpsSpecifications.isFolded(false);
    
            Iterable<DrumCorps> repoResults = repo.findAll(Spec);
    
            assertThat(repoResults).hasSize(0).contains();
    }
    
        // Search by dateFolded specification
        @Test
        public void unhappyTestSearchByDateFolded() {
    
            DrumCorps corps2000 = createCorpsBuilder()
                .dateFolded(LocalDate.of(2000, 1, 1))
                .build();
            
            DrumCorps corps1999 = createCorpsBuilder()
                .dateFolded(LocalDate.of(1999, 1, 1))
                .build();
    
            repo.save(corps2000);
            repo.save(corps1999);
    
            Specification<DrumCorps> Spec = DrumCorpsSpecifications.queryDateFolded(LocalDate.of(2020, 12, 30));
        
            Iterable<DrumCorps> repoResults = repo.findAll(Spec);
        
            assertThat(repoResults).hasSize(0).contains();
    }
}