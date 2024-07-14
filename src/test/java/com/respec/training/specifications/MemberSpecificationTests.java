package com.respec.training.specifications;
import org.junit.jupiter.api.BeforeEach;
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
import com.respec.training.models.Member;
import com.respec.training.repositories.DrumCorpsRepository;
import com.respec.training.repositories.MemberRepository;
import com.respec.training.services.MemberService;

import static org.assertj.core.api.Assertions.*;
import java.time.LocalDate;
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
public class MemberSpecificationTests {

    @Autowired
    private MemberRepository repo;

    @Autowired
    private DrumCorpsRepository drumCorpsRepository;

    @MockBean
    private MemberService service;

    private DrumCorps sampleDrumCorps;

    @BeforeEach
    public void setUp() {
        sampleDrumCorps = new DrumCorps("Troopers", 10, LocalDate.of(2007, 7, 21), true, LocalDate.of(2008, 3, 15), null);
        sampleDrumCorps = drumCorpsRepository.save(sampleDrumCorps);
    }
    
    private Member.MemberBuilder createMemberBuilder()  {

        return Member.builder()
            .firstName("John")
            .lastName("Doe")
            .age(21)
            .birthDate(LocalDate.of(2000, 01, 1))
            .section("Brass")
            .instrument("Trumpet")
            .emailAddress("john.doe@gmail.com")
            .drumCorps(sampleDrumCorps);
    }

    // // // Happy Path Tests \\ \\ \\

    // Page Size and Number
    @Test
    public void testPageSize() {

        final List<Member> mockMemberList = IntStream.range(0, 5)
            .mapToObj(i -> createMemberBuilder()
                .firstName("John" + i)
                .build())
            .collect(Collectors.toList());
        
        repo.saveAll(mockMemberList);

        PageRequest firstPage = PageRequest.of(0, 2);
        PageRequest secondPage = PageRequest.of(1, 2);

        Page<Member> firstPageMember = repo.findAll(firstPage);
        Page<Member> secondPageMember = repo.findAll(secondPage);

        assertThat(firstPageMember.getContent()).hasSize(2).contains(repo.getReferenceById(1L), atIndex(0));
        assertThat(firstPageMember.getContent()).hasSize(2).contains(repo.getReferenceById(2L), atIndex(1));
        assertThat(secondPageMember.getContent()).hasSize(2).contains(repo.getReferenceById(3L), atIndex(0));
        assertThat(secondPageMember.getContent()).hasSize(2).contains(repo.getReferenceById(4L), atIndex(1));
    }

    // Sort by firstName
    @Test
    public void testSortByFirstName() {

        Function<String, Member> createByFirstName = (val) -> createMemberBuilder()
            .firstName(val)
            .build();

        Member memberA = createByFirstName.apply("A");
        Member memberBA = createByFirstName.apply("BA");
        Member memberBC = createByFirstName.apply("BC");
        Member memberD = createByFirstName.apply("D");
        Member memberE = createByFirstName.apply("E");

        List<Member> mockMemberList = Arrays.asList(memberBC, memberA, memberE, memberBA, memberD);

        repo.saveAll(mockMemberList);

        PageRequest page = PageRequest.of(0, 5, Sort.by("firstName").ascending());

        Page<Member> pageDrumCorps  = repo.findAll(page);

        assertThat(pageDrumCorps.getContent()).hasSize(5).containsExactly(memberA, memberBA, memberBC, memberD, memberE);
    }

    // Sort by lastName
    @Test
    public void testSortByLastName() {

        Function<String, Member> createByLastName = (val) -> createMemberBuilder()
            .lastName(val)
            .build();

        Member memberA = createByLastName.apply("A");
        Member memberBA = createByLastName.apply("BA");
        Member memberBC = createByLastName.apply("BC");
        Member memberD = createByLastName.apply("D");
        Member memberE = createByLastName.apply("E");

        List<Member> mockMemberList = Arrays.asList(memberBC, memberA, memberE, memberBA, memberD);

        repo.saveAll(mockMemberList);

        PageRequest page = PageRequest.of(0, 5, Sort.by("lastName").ascending());

        Page<Member> pageDrumCorps  = repo.findAll(page);

        assertThat(pageDrumCorps.getContent()).hasSize(5).containsExactly(memberA, memberBA, memberBC, memberD, memberE);
    }

    // Sort age ASC
    @Test
    public void testAgeASC() {

        Function<Integer, Member> createByAge = (val) -> createMemberBuilder()
            .age(val)
            .build();
            
        Member member1 = createByAge.apply(1);
        Member member2 = createByAge.apply(2);
        Member member3 = createByAge.apply(3);
        Member member4 = createByAge.apply(4);
        Member member5 = createByAge.apply(5);

        List<Member> mockMemberList = Arrays.asList(member5, member2, member4, member1, member3);

        repo.saveAll(mockMemberList);

        PageRequest page = PageRequest.of(0, 5, Sort.by("age").ascending());

        Page<Member> memberPage  = repo.findAll(page);

        assertThat(memberPage.getContent()).hasSize(5).containsExactly(member1, member2, member3, member4, member5);
    }

    // Sort by Birthdate
    @Test
    public void testSortByBirthdate() {

        Function<LocalDate, Member> createByBirthdate = (val) -> createMemberBuilder()
            .birthDate(val)
            .build();

        Member member1981 = createByBirthdate.apply(LocalDate.of(1981, 1, 1));
        Member member1982 = createByBirthdate.apply(LocalDate.of(1982, 1, 1));
        Member member1983 = createByBirthdate.apply(LocalDate.of(1983, 1, 1));
        Member member1984 = createByBirthdate.apply(LocalDate.of(1984, 1, 1));
        Member member1985 = createByBirthdate.apply(LocalDate.of(1985, 1, 1));

        List<Member> mockMemberList = Arrays.asList(member1983, member1981, member1985, member1982, member1984);

        repo.saveAll(mockMemberList);

        PageRequest page = PageRequest.of(0, 5, Sort.by("birthDate"));

        Page<Member> memberPage  = repo.findAll(page);

        assertThat(memberPage.getContent()).hasSize(5).containsExactly(member1981, member1982, member1983, member1984, member1985);
    }

    // Search by first name specification
    @Test
    public void testSearchByFirstName() {

        Member memberTroop = createMemberBuilder()
            .firstName("Troop")
            .build();

        Member memberWrong = createMemberBuilder()
            .firstName("Wrong")
            .build();

        repo.save(memberTroop);
        repo.save(memberWrong);

        Specification<Member> Spec = MemberSpecifications.queryFirstName("Troop");


        Iterable<Member> repoResults = repo.findAll(Spec);

        assertThat(repoResults).hasSize(1).contains(memberTroop);
    }

    // Search by last name specification
    @Test
    public void testSearchByLastName() {

        Member memberRight = createMemberBuilder()
            .lastName("Right")
            .build();

        Member memberWrong = createMemberBuilder()
            .lastName("Wrong")
            .build();

        repo.save(memberRight);
        repo.save(memberWrong);

        Specification<Member> Spec = MemberSpecifications.queryLastName("Right");


        Iterable<Member> repoResults = repo.findAll(Spec);

        assertThat(repoResults).hasSize(1).contains(memberRight);
    }

    // Search by Age specification
    @Test
    public void testSearchByAge() {

        Member member18 = createMemberBuilder()
            .age(18)
            .build();

        Member member21 = createMemberBuilder()
            .age(21)
            .build();

        repo.save(member18);
        repo.save(member21);

        Specification<Member> Spec = MemberSpecifications.queryAge(21);

        Iterable<Member> repoResults = repo.findAll(Spec);

        assertThat(repoResults).hasSize(1).contains(member21);
    }

    // Search by birthDate specification
    @Test
    public void testSearchByDateFounded() {

        Member member2000 = createMemberBuilder()
            .birthDate(LocalDate.of(2000, 1, 1))
            .build();

        Member member1999 = createMemberBuilder()
            .birthDate(LocalDate.of(1999, 1, 1))
            .build();

        repo.save(member2000);
        repo.save(member1999);

        Specification<Member> Spec = MemberSpecifications.queryBirthdate(LocalDate.of(2000, 1, 1));

        Iterable<Member> repoResults = repo.findAll(Spec);

        assertThat(repoResults).hasSize(1).contains(member2000);
    }


    // Search by section specification
    @Test
    public void testSearchBySection() {

        Member memberRight = createMemberBuilder()
            .section("Right")
            .build();

        Member memberWrong = createMemberBuilder()
            .section("Wrong")
            .build();

        repo.save(memberRight);
        repo.save(memberWrong);

        Specification<Member> Spec = MemberSpecifications.querySection("Right");


        Iterable<Member> repoResults = repo.findAll(Spec);

        assertThat(repoResults).hasSize(1).contains(memberRight);
    }

    // Search by instrument specification
    @Test
    public void testSearchByInstrument() {

        Member memberRight = createMemberBuilder()
            .instrument("Right")
            .build();

        Member memberWrong = createMemberBuilder()
            .instrument("Wrong")
            .build();

        repo.save(memberRight);
        repo.save(memberWrong);

        Specification<Member> Spec = MemberSpecifications.queryInstrument("Right");


        Iterable<Member> repoResults = repo.findAll(Spec);

        assertThat(repoResults).hasSize(1).contains(memberRight);
    }

    // Search by email specification
    @Test
    public void testSearchByEmail() {

        Member memberJohn = createMemberBuilder()
            .emailAddress("john.doe@gmail.com")
            .build();

        Member memberJane = createMemberBuilder()
            .emailAddress("mary.jane@gmail.com")
            .build();

        repo.save(memberJohn);
        repo.save(memberJane);

        Specification<Member> Spec = MemberSpecifications.queryEmailAddress("john.doe@gmail.com");


        Iterable<Member> repoResults = repo.findAll(Spec);

        assertThat(repoResults).hasSize(1).contains(memberJohn);
    }

    // Search by drumCorpsId specification
    @Test
    public void testSearchByDrumCorps() {

        Member memberJohn = createMemberBuilder()
            .build();

        Member memberJane = createMemberBuilder()
            .build();

        repo.save(memberJohn);
        repo.save(memberJane);

        Specification<Member> Spec = MemberSpecifications.queryDrumCorps(sampleDrumCorps);


        Iterable<Member> repoResults = repo.findAll(Spec);

        assertThat(repoResults).hasSize(2).contains(memberJohn, memberJane);
    }

    // // // Unhappy Path Tests \\ \\ \\
    // Search by name specification
    @Test
    public void unhappyTestSearchByFirstName() {

        Member memberTroop = createMemberBuilder()
            .firstName("AAA")
            .build();

        Member memberWrong = createMemberBuilder()
            .firstName("BBB")
            .build();

        repo.save(memberTroop);
        repo.save(memberWrong);

        Specification<Member> Spec = MemberSpecifications.queryFirstName("CCC");


        Iterable<Member> repoResults = repo.findAll(Spec);

        assertThat(repoResults).hasSize(0);
    }

    // Search by numOfChamp specification
    @Test
    public void unhappyTestSearchByLastName() {

        Member memberRight = createMemberBuilder()
            .lastName("AAA")
            .build();

        Member memberWrong = createMemberBuilder()
            .lastName("BBB")
            .build();

        repo.save(memberRight);
        repo.save(memberWrong);

        Specification<Member> Spec = MemberSpecifications.queryLastName("CCC");


        Iterable<Member> repoResults = repo.findAll(Spec);

        assertThat(repoResults).hasSize(0);
    }

    // Search by Age specification
    @Test
    public void unhappyTestSearchByAge() {

        Member member18 = createMemberBuilder()
            .age(18)
            .build();

        Member member21 = createMemberBuilder()
            .age(19)
            .build();

        repo.save(member18);
        repo.save(member21);

        Specification<Member> Spec = MemberSpecifications.queryAge(21);

        Iterable<Member> repoResults = repo.findAll(Spec);

        assertThat(repoResults).hasSize(0);
    }

    // Search by birthDate specification
    @Test
    public void unhappyTestSearchByDateFounded() {

        Member member2000 = createMemberBuilder()
            .birthDate(LocalDate.of(2000, 1, 1))
            .build();

        Member member1999 = createMemberBuilder()
            .birthDate(LocalDate.of(1999, 1, 1))
            .build();

        repo.save(member2000);
        repo.save(member1999);

        Specification<Member> Spec = MemberSpecifications.queryBirthdate(LocalDate.of(2020, 1, 1));

        Iterable<Member> repoResults = repo.findAll(Spec);

        assertThat(repoResults).hasSize(0);
    }

    // Search by section specification
    @Test
    public void unhappyTestSearchBySection() {

        Member memberRight = createMemberBuilder()
            .section("AAA")
            .build();

        Member memberWrong = createMemberBuilder()
            .section("BBB")
            .build();

        repo.save(memberRight);
        repo.save(memberWrong);

        Specification<Member> Spec = MemberSpecifications.querySection("CCC");


        Iterable<Member> repoResults = repo.findAll(Spec);

        assertThat(repoResults).hasSize(0);
    }
    
    // Search by instrument specification
    @Test
    public void unhappyTestSearchByInstrument() {

        Member memberRight = createMemberBuilder()
            .section("AAA")
            .build();

        Member memberWrong = createMemberBuilder()
            .section("BBB")
            .build();

        repo.save(memberRight);
        repo.save(memberWrong);

        Specification<Member> Spec = MemberSpecifications.queryInstrument("CCC");


        Iterable<Member> repoResults = repo.findAll(Spec);

        assertThat(repoResults).hasSize(0);
    }

    // Search by email specification
    @Test
    public void unhappyTestSearchByEmail() {

        Member memberJohn = createMemberBuilder()
            .emailAddress("john.doe@gmail.com")
            .build();

        Member memberJane = createMemberBuilder()
            .emailAddress("mary.jane@gmail.com")
            .build();

        repo.save(memberJohn);
        repo.save(memberJane);

        Specification<Member> Spec = MemberSpecifications.queryEmailAddress("foo.bar@gmail.com");


        Iterable<Member> repoResults = repo.findAll(Spec);

        assertThat(repoResults).hasSize(0);
    }
}