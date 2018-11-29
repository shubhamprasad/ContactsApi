package com.example.contacts.ContactsApi.UnitTests;

import static org.junit.Assert.*;

import java.util.List;

import org.hamcrest.Matchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.hamcrest.Matchers.*;

import com.example.contacts.ContactsApi.ContactsApiApplication;
import com.example.contacts.ContactsApi.controllers.ContactController;
import com.example.contacts.ContactsApi.models.Contact;
import com.example.contacts.ContactsApi.repositories.ContactRepository;
import com.example.contacts.ContactsApi.services.ContactsApiService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ContactsApiApplication.class)
public class ContactsControllerUnitTest {

	private MockMvc mockMvc;
	
	@Mock
	ContactsApiService contactsApiService;
	
	@InjectMocks
	private ContactController contactController;
	
	@Autowired
    private WebApplicationContext wac;
	
	@Before
	public void init(){
		MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(contactController).build();
		
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	@Test
	public void testGetAllContacts() throws Exception{
		
		List<Contact> contact = ContactsTestUtil.createContactListForTesting();
		Mockito.when(contactsApiService.findAll()).thenReturn(contact);
		
		RequestBuilder request = MockMvcRequestBuilders.get("/v1").header("authorization", "Basic dXNlcjpwYXNzd29yZA==").accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(request)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(11)))
				.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void testAddContact() throws Exception {
		Contact contact = ContactsTestUtil.getContact1();
		
		String json = ContactsTestUtil.asJsonString(contact);
		
		Mockito.when(contactsApiService.save(Mockito.any(Contact.class))).thenReturn(contact);
		RequestBuilder request = MockMvcRequestBuilders.post("/v1/new")
													   .header("authorization", "Basic dXNlcjpwYXNzd29yZA==")
													   .content(json)
													   .contentType(MediaType.APPLICATION_JSON)
													   .accept(MediaType.APPLICATION_JSON_UTF8);
		
		mockMvc.perform(request)
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(contact.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(contact.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(contact.getEmail())));
 
	}

	@Test
    public void testDeleteContactFailure() throws Exception {
		
		Contact contact = ContactsTestUtil.getContact1();
		
		Mockito.when(contactsApiService.findById(contact.getId())).thenReturn(contact);
		Mockito.doNothing().when(contactsApiService).delete(contact);

		RequestBuilder request = MockMvcRequestBuilders.delete("/v1/delete/{id}", contact.getId());
        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }
	
	@Test
	public void testInvalidHandle() throws Exception{
		
		Contact contact = ContactsTestUtil.getContact1();
		
		String json = ContactsTestUtil.asJsonString(contact);
		
		Mockito.when(contactsApiService.save(Mockito.any(Contact.class))).thenReturn(contact);
		RequestBuilder request = MockMvcRequestBuilders.post("/v1")
													   .header("authorization", "Basic dXNlcjpwYXNzd29yZA==")
													   .content(json)
													   .contentType(MediaType.APPLICATION_JSON)
													   .accept(MediaType.APPLICATION_JSON_UTF8);
		
		mockMvc.perform(request)
				.andExpect(MockMvcResultMatchers.status().is4xxClientError())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));
 
	}
	
}