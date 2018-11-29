package com.example.contacts.ContactsApi.repositories;

import com.example.contacts.ContactsApi.models.Contact;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ContactRepository extends MongoRepository<Contact, String> {

	Contact findByid(String id);
	
	@Query("{ 'name' : { $regex: ?0, $options: \"i\"  } }")
	List<Contact> findContactByRegexpName(String regexp);
	
	@Query("{ 'email' : { $regex: ?0, $options: \"i\" } }")
	List<Contact> findContactByRegexpEmail(String regexp);
	
	@Query("{ 'name' : { $regex: ?0, $options: \"i\"  } }")
	Page<Contact> findContactByRegexpName(String name, Pageable pageable);
	
	@Query("{ 'email' : { $regex: ?0, $options: \"i\" } }")
	Page<Contact> findContactByRegexpEmail(String email, Pageable pageable);
	
}