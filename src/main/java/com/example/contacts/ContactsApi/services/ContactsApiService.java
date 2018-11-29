package com.example.contacts.ContactsApi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.contacts.ContactsApi.models.Contact;
import com.example.contacts.ContactsApi.repositories.ContactRepository;

@Service
public class ContactsApiService implements IContactsApiService {
	
	@Autowired
	private ContactRepository contactRepository;
	
	@Override
	public Contact save(Contact contact) {
		return contactRepository.save(contact);
	}

	@Override
	public Page<Contact> findAll(Pageable pageable) {
		return contactRepository.findAll(pageable);
	}
	
	public List<Contact> findAll() {
		return contactRepository.findAll();
	}

	@Override
	public void delete(Contact contact) {
		contactRepository.delete(contact);
	}

	@Override
	public Contact findById(String id) {
		return contactRepository.findByid(id);
	}
	
	@Override
	public Page<Contact> findContactByRegexpEmail(String email, Pageable pageable){
		return contactRepository.findContactByRegexpEmail(email, pageable);
	};
	
	@Override
	public Page<Contact> findContactByRegexpName(String name, Pageable pageable){
		return contactRepository.findContactByRegexpName(name, pageable);
	}

}