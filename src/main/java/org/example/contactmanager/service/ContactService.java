package org.example.contactmanager.service;

import org.example.contactmanager.Dto.ContactDto;
import org.example.contactmanager.entity.Contact;
import org.example.contactmanager.mapper.ContactMapper;
import org.example.contactmanager.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContactService {

    private final ContactRepository repository;
    private final ContactMapper mapper;

    public ContactService(ContactRepository repository, ContactMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<ContactDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<ContactDto> findById(Long id) {
        Contact contact = repository.findById(id);
        return contact != null ? Optional.of(mapper.toDto(contact)) : Optional.empty();
    }

    public ContactDto create(ContactDto dto) {
        Contact contact = mapper.toEntity(dto);
        Contact saved = repository.save(contact);
        return mapper.toDto(saved);
    }

    public Optional<ContactDto> update(Long id, ContactDto dto) {
        if (repository.findById(id) == null) {
            return Optional.empty();
        }
        Contact contact = mapper.toEntity(dto);
        contact.setId(id);
        Contact updated = repository.update(id, contact);
        return Optional.of(mapper.toDto(updated));
    }
}