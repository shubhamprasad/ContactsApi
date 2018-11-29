package com.example.contacts.ContactsApi.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "contacts")
public class Contact {

	@Id
	private String id;
	
	@NotNull(message="Name should not be null")
	@Size(min=1, message="Name should have atleast 1 character")
	@Indexed(name = "name_index")
	private String name;
	
	@NotNull(message="Email should not be null") 
	@Email(message="Email should be valid") 
	@Size(min=1, message="Email should have atleast 1 character")
	@Indexed(name = "email_index", unique = true)
	private String email;
	
	public Contact() {
		
	}
	
	public Contact(String name, String email){
		this.name = name;
		this.email = email;
	}
	
	public String getId() {
	    return id;
	}
	
	public void setId(String id) {
	    this.id = id;
	}
	
	public String getName() {
	    return name;
	}
	
	public void setName(String name) {
	    this.name = name;
	}
	
	public String getEmail() {
	    return email;
	}
	
	public void setEmail(String email) {
	    this.email = email;
	}

}