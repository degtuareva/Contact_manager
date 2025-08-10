package org.example.contactmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.contactmanager.Dto.ContactDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.Optional.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ContactDto testContact;

    @BeforeEach
    void setup() throws Exception {
        testContact = new ContactDto(null, "Test", "User", "+123456789", "test@example.com");
        String json = objectMapper.writeValueAsString(testContact);

        String response = mockMvc.perform(post("/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ContactDto created = objectMapper.readValue(response, ContactDto.class);
        testContact.setId(created.getId());
    }

    @Test
    void testGetAllContacts() throws Exception {
        mockMvc.perform(get("/contacts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())))
                .andExpect(jsonPath("$[*].firstName", hasItem("Test")));
    }

    @Test
    void testGetContactById() throws Exception {
        mockMvc.perform(get("/contacts/{id}", testContact.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(testContact.getEmail())));
    }

    @Test
    void testUpdateContact() throws Exception {
        ContactDto updated = new ContactDto(null, "Updated", "User", "+987654321", "updated@example.com");
        String json = objectMapper.writeValueAsString(updated);

        mockMvc.perform(put("/contacts/{id}", testContact.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Updated")))
                .andExpect(jsonPath("$.phoneNumber", is("+987654321")));
    }

    @Test
    void testGetNonExistingContactReturnsNotFound() throws Exception {
        mockMvc.perform(get("/contacts/{id}", 99999))
                .andExpect(status().isNotFound());
    }
}