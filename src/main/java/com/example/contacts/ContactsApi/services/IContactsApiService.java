package com.example.contacts.ContactsApi.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.contacts.ContactsApi.models.Contact;

public interface IContactsApiService {

	public Contact save(Contact bird);

	public Page<Contact> findAll(Pageable pageableRequest);

	public Contact findById(String id);
	
	public void delete(Contact contact);
	
	public Page<Contact> findContactByRegexpEmail(String email, Pageable pageable);
	
	public Page<Contact> findContactByRegexpName(String name, Pageable pageable);
	
}