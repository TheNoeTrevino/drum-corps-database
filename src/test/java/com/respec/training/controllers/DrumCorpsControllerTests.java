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
import com.respec.training.DTO.CreateUpdateDrumCorpsDTO;
import com.respec.training.DTO.DrumCorpsDTO;
import com.respec.training.exceptions.NotFoundException;
import com.respec.training.mappers.DrumCorpsMapper;
import com.respec.training.models.DrumCorps;
import com.respec.training.repositories.DrumCorpsRepository;
import com.respec.training.services.DrumCorpsService;

@WebMvcTest(controllers = { DrumCorpsController.class })
public class DrumCorpsControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private DrumCorpsService service;

	@MockBean
	private DrumCorpsRepository repo;

	@MockBean
	private DrumCorpsMapper mockMapper;

	private static String baseUrl = "/drum-corps";

	// // // 'unhappy path' tests // // //
	// null content test
	@Test
	public void shouldReturn400BadRequestForNullContent() throws Exception {
		CreateUpdateDrumCorpsDTO req = new CreateUpdateDrumCorpsDTO(null, 0,
				null, null, null);

		this.mockMvc.perform(post(baseUrl)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isBadRequest())
				.andExpect(content().json(
						"{'status': 'BAD_REQUEST','validationErrors':{'corpsName': 'Corps Name can not be null.','dateFounded': 'Date founded can not be null.', 'folded':'Folded can not be null.'}}"));
	}

	// size test
	@Test
	public void shouldReturn400BadRequestForSize() throws Exception {
		CreateUpdateDrumCorpsDTO req = new CreateUpdateDrumCorpsDTO("Troopers".repeat(100), 4,
				LocalDate.of(2007, 07, 21), false,
				LocalDate.of(2008, 07, 21));

		this.mockMvc.perform(post(baseUrl)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isBadRequest())
				.andExpect(content().json(
						"{'status': 'BAD_REQUEST', 'validationErrors': {'corpsName': 'The name of this Drum Corps is too long. Please submit a name under 255 characters.'}}"));
	}

	// PastOrPresent test
	@Test
	public void shouldReturn400BadRequestForPastOrPresent() throws Exception {

		LocalDate futureDate = LocalDate.now().plusDays(1);

		CreateUpdateDrumCorpsDTO req = new CreateUpdateDrumCorpsDTO("Troopers", 4,
				futureDate, false, null);

		this.mockMvc.perform(post(baseUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isBadRequest())
				.andExpect(content().json(
						"{'status': 'BAD_REQUEST', 'validationErrors': {'dateFounded': 'Date Founded must be in the past, or the current day.'}}"));
	}

	// Max value test
	@Test
	public void shouldReturn400BadRequestForMaxValue() throws Exception {
		CreateUpdateDrumCorpsDTO req = new CreateUpdateDrumCorpsDTO("Troopers",
				90,
				LocalDate.of(2007, 7, 21), false,
				LocalDate.of(2008, 7, 21));

		this.mockMvc.perform(post(baseUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isBadRequest())
				.andExpect(content().json(
						"{'status': 'BAD_REQUEST', 'validationErrors': {'numOfChamp': 'The Blue Devils have the most championships, at 21. This can not be true.'}}",
						true));
	}

	// // // happy path tests // // //
	// Testing happy get by id
	@Test
	public void shouldReturn200GetById() throws Exception {
		DrumCorpsDTO req = new DrumCorpsDTO(1, "Troopers",
				90,
				LocalDate.of(2007, 7, 21), false,
				LocalDate.of(2008, 7, 21), null);

		Long corpsId = (long) 1;

		when(service.getDrumCorpsById(corpsId)).thenReturn(req);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/drum-corps/{id}", corpsId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.corpsId").value(corpsId))
				.andExpect(MockMvcResultMatchers.jsonPath("$.corpsName").value("Troopers"));
	}

	// Testing happy delete by id
	@Test
	public void shouldReturn200DeleteById() {
		DrumCorps drumCorps = new DrumCorps("Troopers", 9,
				LocalDate.of(2000, 7, 21), false,
				LocalDate.of(2008, 7, 21), null);

		when(repo.getReferenceById(drumCorps.getId())).thenReturn(drumCorps);

		service.deleteDrumCorpsById(drumCorps.getId());

		verify(service, times(1)).deleteDrumCorpsById(drumCorps.getId());
	}

	// // // not found tests // // //
	// delete 404 test
	@Test
	public void shouldReturn404DeleteById() throws Exception {
		Long corpsId = (long) 1;

		when(service.deleteDrumCorpsById(corpsId)).thenThrow(new NotFoundException("Drum Corps", corpsId));
		when(repo.findById(corpsId)).thenReturn(Optional.empty());

		this.mockMvc.perform(MockMvcRequestBuilders.delete("/drum-corps/{id}", corpsId))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.notFoundError").value("Could not find Drum Corps with id:1"));
	}

	// get 404 test
	@Test
	public void shouldReturn404GetById() throws Exception {
		Long corpsId = (long) 1;

		when(service.getDrumCorpsById(corpsId)).thenThrow(new NotFoundException("Drum Corps", corpsId));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/drum-corps/{id}", corpsId))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.notFoundError").value("Could not find Drum Corps with id:1"));
	}

	// update 404 test
	@Test
	public void shouldReturn404UpdateById() throws Exception {
		Long corpsId = (long) 99;

		CreateUpdateDrumCorpsDTO newDTO = new CreateUpdateDrumCorpsDTO("Troopers", 1,
				LocalDate.of(1998, 7, 1), false, null);

		String drumCorpsJson = objectMapper.writeValueAsString(newDTO);

		when(service.updateDrumCorps(corpsId, newDTO)).thenThrow(new NotFoundException("Drum Corps", corpsId));
		when(repo.findById(corpsId)).thenReturn(Optional.empty());

		this.mockMvc.perform(MockMvcRequestBuilders.patch("/drum-corps/{id}", corpsId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(drumCorpsJson))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.notFoundError")
						.value("Could not find Drum Corps with id:99"));
	}

	// Unhappy Path page size test
	@Test
	public void shouldReturn404PageableError() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/drum-corps?size=500"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.pageableError")
						.value("Page size must be less than or equal to 50"));
	}
}