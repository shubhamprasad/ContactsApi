package com.example.contacts.ContactsApi.controllers;

import com.example.contacts.ContactsApi.exceptions.InvalidRequestException;
import com.example.contacts.ContactsApi.models.ApiErrorResponse;
import com.example.contacts.ContactsApi.models.Contact;
import com.example.contacts.ContactsApi.repositories.ContactRepository;
import com.example.contacts.ContactsApi.services.ContactsApiService;
import com.example.contacts.ContactsApi.services.IContactsApiService;
import com.github.javafaker.Faker;
import com.mongodb.MongoException;
import java.text.SimpleDateFormat;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ContactController {
	
	private final int pageSize = 10;
	public static String getTimeStamp(){
		String timeStamp = new SimpleDateFormat("dd-mm-yyyy-HH-mm-ss.SSS").format(new java.util.Date());
		return timeStamp;
	}

	
	@Autowired
    IContactsApiService contactsApiService;
	ContactRepository contactrepository; 
	
    @RequestMapping(value = "/v1", method = RequestMethod.GET)
    public ResponseEntity<Page<Contact>> getAllContacts(@RequestParam(required = false, name = "page") String page) {
    	
    	int pageNumber = 0;
    	if(page != null){
			try {
				pageNumber = Integer.parseInt(page);
			} catch (NumberFormatException e) {
				pageNumber = 0;
				e.printStackTrace();
			}
    	}
    	
    	final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(Sort.Direction.ASC, "created"));
    	Page<Contact> contacts = contactsApiService.findAll(pageableRequest);
    	return new ResponseEntity<Page<Contact>>(contacts, HttpStatus.OK);
    }
    
    @RequestMapping( value="/v1/new", method=RequestMethod.POST)
    public ResponseEntity<Contact> save(@Valid @RequestBody Contact contact, BindingResult bindingResult) {
    	
    	if(bindingResult.hasErrors()){
    		throw new InvalidRequestException("Invalid Fields", bindingResult);
    	}
    	
    	else{
        
	    	try {
				contactsApiService.save(contact);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new MongoException("Email already exists");
			}
    	}
        
        return new ResponseEntity<Contact>(contact, HttpStatus.CREATED);
    }
    
    
    @RequestMapping(method=RequestMethod.GET, value="/v1/contact/{id}")
    public Contact show(@PathVariable String id) throws Exception {
    	Contact contact = contactsApiService.findById(id);
    	if(contact == null){
    		throw new Exception("Invalid Contact Id");
		}
    	return contact;
    }
    
    @RequestMapping(value = "/v1/edit/{id}", method = RequestMethod.PUT)
    public Contact updateContactByid(@PathVariable("id") String id, @Valid @RequestBody Contact contact, BindingResult bindingResult) throws Exception {
    	
		if(bindingResult.hasErrors()){
			throw new InvalidRequestException("Invalid Fields", bindingResult);
		}
		else{
	      Contact c = contactsApiService.findById(id);
	      if(c != null){
		      contact.setId(id);
		      contactsApiService.save(contact);
		      return contact;
	      }
	      else
	    	  throw new Exception("Contact does not Exist");
	    }
    }
    
    @RequestMapping(value = "/v1/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteContact(@PathVariable String id) {
		if(contactsApiService.findById(id) == null){
    		ApiErrorResponse apiError = new ApiErrorResponse(404, "Invalid Contact Id", "Contact does not exist", getTimeStamp());
	    	return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
    	else{
    		contactsApiService.delete(contactsApiService.findById(id));
    		ApiErrorResponse apiMessage = new ApiErrorResponse(200, "Deletion Succesful", "Contact deleted", getTimeStamp());
    		return new ResponseEntity<Object>(apiMessage, new HttpHeaders(), HttpStatus.OK);
    	}
    }
    
    @RequestMapping(value = "/v1/searchByName", method = RequestMethod.GET)
    public Page<Contact> searchByName(@RequestParam("name") String name, @RequestParam(required = false, name = "page") String page) {
    	
    	int pageNumber = 0;
    	if(page != null){
			try {
				pageNumber = Integer.parseInt(page);
			} catch (NumberFormatException e) {
				pageNumber = 0;
				e.printStackTrace();
			}
    	}
    	
    	
    	final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(Sort.Direction.ASC, "created"));
    	Page<Contact> contacts = contactsApiService.findContactByRegexpName(name, pageableRequest);
    	return contacts;
      }
    
    @RequestMapping(value = "/v1/searchByEmail", method = RequestMethod.GET)
    public Page<Contact> searchByEmail(@RequestParam("email") String email, @RequestParam(required = false, name = "page") String page) {
    
    	int pageNumber = 0;
    	if(page != null){
			try {
				pageNumber = Integer.parseInt(page);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				pageNumber = 0;
				e.printStackTrace();
			}
    	}
    	
    	final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(Sort.Direction.ASC, "created"));
    	Page<Contact> contacts = contactsApiService.findContactByRegexpName(email, pageableRequest);
        return contacts;
      }
    
    @RequestMapping(value = "/v1/seedTestData", method = RequestMethod.GET)
    public void seedData() {
    	
    	for(int i = 0; i < 100; i++){
	    	Faker faker = new Faker();
	    	String firstName = faker.name().firstName();
	    	String lastName = faker.name().lastName();
	    	String email = firstName + "." + lastName + "@email.com";
	    	contactsApiService.save(new Contact(firstName + " " + lastName, email));
    	}
    	
      }
    
}