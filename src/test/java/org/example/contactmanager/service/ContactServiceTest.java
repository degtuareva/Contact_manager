package org.example.contactmanager.service;

import org.example.contactmanager.Dto.ContactDto;
import org.example.contactmanager.mapper.ContactMapper;
import org.example.contactmanager.repository.ContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContactServiceTest {

    private ContactRepository repository;
    private ContactMapper mapper;
    private ContactService service;

    @BeforeEach
    void setup() {
        repository = new ContactRepository();
        mapper = new ContactMapper();
        service = new ContactService(repository, mapper);
    }

    @Test
    void testCreateAndFindById() {
        ContactDto dto = new ContactDto(null, "John", "Doe", "1234567890", "john@example.com");
        ContactDto created = service.create(dto);

        assertNotNull(created.getId());
        assertEquals("John", created.getFirstName());

        Optional<ContactDto> found = service.findById(created.getId());
        assertTrue(found.isPresent());
        assertEquals("Doe", found.get().getLastName());
    }

    @Test
    void testFindAll() {
        service.create(new ContactDto(null, "John", "Smith", "111", "jsmith@example.com"));
        service.create(new ContactDto(null, "Jane", "Doe", "222", "jane@example.com"));

        List<ContactDto> all = service.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void testUpdate() {
        ContactDto dto = new ContactDto(null, "John", "Doe", "123", "john@example.com");
        ContactDto created = service.create(dto);

        ContactDto updateDto = new ContactDto(null, "John", "Updated", "999", "updated@example.com");
        Optional<ContactDto> updated = service.update(created.getId(), updateDto);

        assertTrue(updated.isPresent());
        assertEquals("Updated", updated.get().getLastName());
        assertEquals("999", updated.get().getPhoneNumber());
    }

    @Test
    void updateNonExistingContactReturnsEmpty() {
        Optional<ContactDto> updated = service.update(999L, new ContactDto(null, "No", "Body", "000", "no@example.com"));
        assertTrue(updated.isEmpty());
    }
}