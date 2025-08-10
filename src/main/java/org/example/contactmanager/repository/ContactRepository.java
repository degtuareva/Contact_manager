package org.example.contactmanager.repository;



import org.example.contactmanager.entity.Contact;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ContactRepository {

    private final ConcurrentHashMap<Long, Contact> storage = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    public Collection<Contact> findAll() {
        return storage.values();
    }

    public Contact findById(Long id) {
        return storage.get(id);
    }

    public Contact save(Contact contact) {
        Long id = idCounter.incrementAndGet();
        contact.setId(id);
        storage.put(id, contact);
        return contact;
    }

    public Contact update(Long id, Contact contact) {
        contact.setId(id);
        storage.put(id, contact);
        return contact;
    }
}