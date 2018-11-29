package com.example.contacts.ContactsApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.contacts.ContactsApi.models.Contact;
import com.example.contacts.ContactsApi.services.ContactsApiService;
import com.github.javafaker.Faker;

@SpringBootApplication
public class ContactsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContactsApiApplication.class, args);
		
//		seedData();
	}
	
//	public void seedData() {
//    	
//		ContactsApiService contactsApiService = new ContactsApiService();
//		
//    	for(int i = 0; i < 100; i++){
//	    	Faker faker = new Faker();
//	    	String firstName = faker.name().firstName();
//	    	String lastName = faker.name().lastName();
//	    	String email = firstName + "." + lastName + "@email.com";
//	    	contactsApiService.save(new Contact(firstName + " " + lastName, email));
//    	}
//    	
//      }
}
