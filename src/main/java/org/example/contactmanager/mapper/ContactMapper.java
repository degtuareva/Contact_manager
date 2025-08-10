package org.example.contactmanager.mapper;

import org.example.contactmanager.Dto.ContactDto;
import org.example.contactmanager.entity.Contact;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {

    public ContactDto toDto(Contact entity) {
        if (entity == null) return null;
        return new ContactDto(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getPhoneNumber(),
                entity.getEmail()
        );
    }

    public Contact toEntity(ContactDto dto) {
        if (dto == null) return null;
        return new Contact(
                dto.getId(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getPhoneNumber(),
                dto.getEmail()
        );
    }
}