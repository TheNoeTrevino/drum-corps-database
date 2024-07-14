package com.respec.training.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.respec.training.DTO.CreateUpdateMemberDTO;
import com.respec.training.exceptions.NotFoundException;
import com.respec.training.mappers.MemberMapper;
import com.respec.training.models.DrumCorps;
import com.respec.training.models.Member;
import com.respec.training.repositories.MemberRepository;
import com.respec.training.services.MemberService;

@WebMvcTest(controllers = { MemberController.class })
public class MemberControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private MemberService service;

	@MockBean
	private MemberRepository repo;

	@MockBean
	private MemberMapper mockMapper;

	private static String baseUrl = "/members";

	// // // 'unhappy path' tests // // //
	// null content test
	@Test
	public void shouldReturn400BadRequestForNullContent() throws Exception {
		CreateUpdateMemberDTO req = new CreateUpdateMemberDTO(99L, null, null, 0,
				null, null, null, null);

		this.mockMvc.perform(post(baseUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isBadRequest())
				.andExpect(content().json(
						"{'status': 'BAD_REQUEST','validationErrors':{'firstName': 'First Name can not be null.','lastName': 'Last Name can not be null.', 'birthDate':'Birthdate can not be null.', 'section':'Section can not be null.', 'instrument':'instrument can not be null.', 'emailAddress':'Email can not be null.'}}"));
	}

	// size test
	@Test
	public void shouldReturn400BadRequestForSize() throws Exception {
		CreateUpdateMemberDTO req = new CreateUpdateMemberDTO(99L, "John".repeat(100), "Doe".repeat(100), 0,
				null, "Brass".repeat(100), "Trumpet".repeat(100), null);

		this.mockMvc.perform(post(baseUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isBadRequest())
				.andExpect(content().json(
						"{'status': 'BAD_REQUEST','validationErrors':{'firstName': 'The First Name of this member is too long. Please submit a First Name under 255 characters.','lastName': 'The Last Name of this member is too long. Please submit a Last Name under 255 characters.', 'section':'The name of this section is too long. Please submit an entry under 255 characters.', 'instrument':'The name of this instrument is too long. Please submit an entry under 255 characters.'}}"));
	}

	// PastOrPresent test
	@Test
	public void shouldReturn400BadRequestForPastOrPresent() throws Exception {

		LocalDate futureDate = LocalDate.now().plusDays(1);

		CreateUpdateMemberDTO req = new CreateUpdateMemberDTO(99L, null, null, 0,
		futureDate, null, null, null);

		this.mockMvc.perform(post(baseUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isBadRequest())
				.andExpect(content().json(
						"{'status': 'BAD_REQUEST','validationErrors':{'birthDate': 'Birthdate must be in the past, or the current day.'}}"));
	}

	// Testing happy delete by id
	@Test
	public void shouldReturn200DeleteById() {

		DrumCorps drumCorps = new DrumCorps("Troopers", 9,
				LocalDate.of(2000, 7, 21), false,
				LocalDate.of(2008, 7, 21), null);

		Member member = new Member("John", "Doe", 19, LocalDate.of(2007, 7, 21),
							"brass", "trumpet", "john.doe@gmail.com", drumCorps);

		when(repo.getReferenceById(member.getId())).thenReturn(member);

		service.deleteMemberById(member.getId());

		verify(service, times(1)).deleteMemberById(member.getId());
	}

	// // // not found tests // // //
	// delete 404 test
	@Test
	public void shouldReturn404DeleteById() throws Exception {
		Long memberId = (long) 1;

		when(service.deleteMemberById(memberId)).thenThrow(new NotFoundException("member", memberId));
		when(repo.findById(memberId)).thenReturn(Optional.empty());

		this.mockMvc.perform(MockMvcRequestBuilders.delete("/members/{id}", memberId))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.notFoundError").value("Could not find member with id:1"));
	}

	// get 404 test
	@Test
	public void shouldReturn404GetById() throws Exception {
		Long memberId = (long) 1;

		when(service.getMemberById(memberId)).thenThrow(new NotFoundException("member", memberId));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/members/{id}", memberId))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.notFoundError").value("Could not find member with id:1"));
	}

	// update 404 test
	@Test
	public void shouldReturn404UpdateById() throws Exception {
		Long memberId = (long) 99;

		CreateUpdateMemberDTO newDTO = new CreateUpdateMemberDTO(99L, "John", "Doe", 19,
					LocalDate.of(2007, 7, 21),"brass", "trumpet", "john.doe@gmail.com");

		String memberJson = objectMapper.writeValueAsString(newDTO);

		when(service.updateMember(memberId, newDTO)).thenThrow(new NotFoundException("members", memberId));
		when(repo.findById(memberId)).thenReturn(Optional.empty());

		this.mockMvc.perform(MockMvcRequestBuilders.patch("/members/{id}", memberId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(memberJson))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.notFoundError")
						.value("Could not find members with id:99"));
	}

	// Unhappy Path page size test
	@Test
	public void shouldReturn404PageableError() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/members?size=500"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.pageableError")
						.value("Page size must be less than or equal to 50"));
	}
}
