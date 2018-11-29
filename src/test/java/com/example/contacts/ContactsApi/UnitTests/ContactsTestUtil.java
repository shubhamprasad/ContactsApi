package com.example.contacts.ContactsApi.UnitTests;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.contacts.ContactsApi.models.Contact;
import com.fasterxml.jackson.annotation.JsonInclude;

public class ContactsTestUtil {

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),                        
            Charset.forName("utf8")                     
            );
	
	public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
 
    public static String createStringWithLength(int length) {
        StringBuilder builder = new StringBuilder();
 
        for (int index = 0; index < length; index++) {
            builder.append("a");
        }
 
        return builder.toString();
    }
	
	public static String asJsonString(final Object obj) {
	    try {
	    	final ObjectMapper mapper = new ObjectMapper();
	        final String jsonContent = mapper.writeValueAsString(obj);
	        return jsonContent;
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	
	public static List<Contact> createContactListForTesting() {
		List<Contact> contact = new ArrayList<>();
		contact.add(getContact1());
		contact.add(getContact2());
		return contact;
	}

	public static Contact getContact1() {
		Contact contact = new Contact();
		contact.setId("ID-1");
		contact.setName("Person 2");
		contact.setEmail("person1@email.com");
		
		return contact;
	}

	public static Contact getContact2() {
		Contact contact = new Contact();
		contact.setId("ID-2");
		contact.setName("Person 1");
		contact.setEmail("person2@email.com");
		
		return contact;
	}
}