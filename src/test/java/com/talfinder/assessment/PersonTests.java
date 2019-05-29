package com.talfinder.assessment;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonTests {

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Before
  public void setup() {
    jdbcTemplate.update("DELETE FROM person_phone");
    jdbcTemplate.update("DELETE FROM person_address");
    jdbcTemplate.update("DELETE FROM person");
    jdbcTemplate.execute("ALTER TABLE person AUTO_INCREMENT = 1");
    jdbcTemplate.execute("ALTER TABLE person_address AUTO_INCREMENT = 1");
    jdbcTemplate.execute("ALTER TABLE person_phone AUTO_INCREMENT = 1");
  }

  private void createTestPerson(){
    String requestJson = "{\"firstName\":\"Classie\",\"lastName\":\"Raub\",\"dateOfBirth\":\"01-05-1985\",\"emailId\":\"classie.raub@mailinator.com\",\"addressess\":[{\"houseNumber\":\"366\",\"street\":\"W.Sussex Street\",\"area\":\"Wappingers Falls\",\"city\":\"New York\",\"pincode\":12590},{\"houseNumber\":\"7\",\"street\":\"West Shub Farm Street\",\"area\":\"Statesville\",\"city\":\"North Carolina\",\"pincode\":28625}],\"phoneNumbers\":[{\"phoneNumber\":\"9945032765\"},{\"phoneNumber\":\"9945132765\"}]}";

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type","application/json");
    HttpEntity<String> request = new HttpEntity<>(requestJson, headers);
    ResponseEntity<String> responseEntity =
        testRestTemplate.postForEntity("/person", request, String.class);
  }

  @Test
  public void test_list_all_persons() throws JSONException {
    ResponseEntity<String> responseEntity = testRestTemplate
        .getForEntity("/persons/", String.class);

    String responseJson = "[]";
    Assert.assertEquals("input:Expected Response ", responseJson, responseEntity.getBody());
  }

  @Test
  public void test_create_person(){
    String requestJson = "{\"firstName\":\"Cruz\",\"lastName\":\"Christie\",\"dateOfBirth\":\"21-07-1998\",\"emailId\":\"cruz.christie21@mailinator.com\",\"addressess\":[{\"houseNumber\":\"7761\",\"street\":\"Mayflower Lane\",\"area\":\"Altamonte Springs\",\"city\":\"Florida\",\"pincode\":32714},{\"houseNumber\":\"8485\",\"street\":\"Peachtree Street\",\"area\":\"Augusta\",\"city\":\"Georgia\",\"pincode\":30906}],\"phoneNumbers\":[{\"phoneNumber\":\"9047032765\"},{\"phoneNumber\":\"8945132765\"}]}";

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type","application/json");
    HttpEntity<String> request = new HttpEntity<>(requestJson, headers);
    ResponseEntity<String> responseEntity =
        testRestTemplate.postForEntity("/person", request, String.class);
    Assert.assertEquals("input:Expected http response status code ", HttpStatus.CREATED, responseEntity.getStatusCode());
  }

  @Test
  public void test_findById_person() throws JSONException {
    createTestPerson();

    Map<String, String> urlVariables = new HashMap<>();
    urlVariables.put("id","1");
    ResponseEntity<String> getResponseEntity = testRestTemplate
        .getForEntity("/person/{id}", String.class, urlVariables);

    String jsonResponse = "{\"id\":1,\"firstName\":\"Classie\",\"lastName\":\"Raub\",\"dateOfBirth\":\"01-05-1985\",\"emailId\":\"classie.raub@mailinator.com\",\"addressess\":[{\"id\":1,\"houseNumber\":\"366\",\"street\":\"W.Sussex Street\",\"area\":\"Wappingers Falls\",\"city\":\"New York\",\"pincode\":12590},{\"id\":2,\"houseNumber\":\"7\",\"street\":\"West Shub Farm Street\",\"area\":\"Statesville\",\"city\":\"North Carolina\",\"pincode\":28625}],\"phoneNumbers\":[{\"id\":1,\"phoneNumber\":\"9945032765\"},{\"id\":2,\"phoneNumber\":\"9945132765\"}]}";
    Assert.assertEquals("input:Expected http response code ", HttpStatus.OK.value(), getResponseEntity.getStatusCodeValue());
    JSONAssert.assertEquals("input:Expected response ", jsonResponse, getResponseEntity.getBody(), false);
  }

  @Test
  public void test_findByPhone_person() throws JSONException {
    createTestPerson();

    Map<String, String> urlVariables = new HashMap<>();
    urlVariables.put("phoneNumber","9945032765");
    ResponseEntity<String> getResponseEntity = testRestTemplate
        .getForEntity("/person/findByPhone/{phoneNumber}", String.class, urlVariables);

    String jsonResponse = "{\"id\":1,\"firstName\":\"Classie\",\"lastName\":\"Raub\",\"dateOfBirth\":\"01-05-1985\",\"emailId\":\"classie.raub@mailinator.com\",\"addressess\":[{\"id\":1,\"houseNumber\":\"366\",\"street\":\"W.Sussex Street\",\"area\":\"Wappingers Falls\",\"city\":\"New York\",\"pincode\":12590},{\"id\":2,\"houseNumber\":\"7\",\"street\":\"West Shub Farm Street\",\"area\":\"Statesville\",\"city\":\"North Carolina\",\"pincode\":28625}],\"phoneNumbers\":[{\"id\":1,\"phoneNumber\":\"9945032765\"},{\"id\":2,\"phoneNumber\":\"9945132765\"}]}";
    Assert.assertEquals("input:Expected http response code ", HttpStatus.OK.value(), getResponseEntity.getStatusCodeValue());
    JSONAssert.assertEquals("input:Expected response ", jsonResponse, getResponseEntity.getBody(), false);
  }

  @Test
  public void test_findByEmail_person() throws JSONException {
    createTestPerson();

    Map<String, String> urlVariables = new HashMap<>();
    urlVariables.put("emailId","classie.raub@mailinator.com");
    ResponseEntity<String> getResponseEntity = testRestTemplate
        .getForEntity("/person/findByEmail/{emailId}", String.class, urlVariables);

    String jsonResponse = "{\"id\":1,\"firstName\":\"Classie\",\"lastName\":\"Raub\",\"dateOfBirth\":\"01-05-1985\",\"emailId\":\"classie.raub@mailinator.com\",\"addressess\":[{\"id\":1,\"houseNumber\":\"366\",\"street\":\"W.Sussex Street\",\"area\":\"Wappingers Falls\",\"city\":\"New York\",\"pincode\":12590},{\"id\":2,\"houseNumber\":\"7\",\"street\":\"West Shub Farm Street\",\"area\":\"Statesville\",\"city\":\"North Carolina\",\"pincode\":28625}],\"phoneNumbers\":[{\"id\":1,\"phoneNumber\":\"9945032765\"},{\"id\":2,\"phoneNumber\":\"9945132765\"}]}";
    Assert.assertEquals("input:Expected http response code ", HttpStatus.OK.value(), getResponseEntity.getStatusCodeValue());
    JSONAssert.assertEquals("input:Expected response ", jsonResponse, getResponseEntity.getBody(), false);
  }

  @Test
  public void test_update_person() throws JSONException {
    createTestPerson();

    String requestJson = "{\"id\":1,\"firstName\":\"Classie\",\"lastName\":\"Raub\",\"dateOfBirth\":\"01-05-1985\",\"emailId\":\"classie.raub@mailinator.com\",\"addressess\":[{\"id\":1,\"houseNumber\":\"8312\",\"street\":\"South Surrey Drive\",\"area\":\"Algonquin\",\"city\":\"Ilinois\",\"pincode\":60102},{\"id\":2,\"houseNumber\":\"7\",\"street\":\"West Shub Farm Street\",\"area\":\"Statesville\",\"city\":\"North Carolina\",\"pincode\":28625}],\"phoneNumbers\":[{\"id\":1,\"phoneNumber\":\"9945032765\"},{\"id\":2,\"phoneNumber\":\"9945132765\"}]}";
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type","application/json");
    HttpEntity<String> request = new HttpEntity<>(requestJson, headers);
    testRestTemplate.put("/person/update", request);

    Map<String, String> urlVariables = new HashMap<>();
    urlVariables.put("id","1");
    ResponseEntity<String> getResponseEntity = testRestTemplate
        .getForEntity("/person/{id}", String.class, urlVariables);

    String responseJson = "{\"id\":1,\"firstName\":\"Classie\",\"lastName\":\"Raub\",\"dateOfBirth\":\"01-05-1985\",\"emailId\":\"classie.raub@mailinator.com\",\"addressess\":[{\"id\":1,\"houseNumber\":\"8312\",\"street\":\"South Surrey Drive\",\"area\":\"Algonquin\",\"city\":\"Ilinois\",\"pincode\":60102},{\"id\":2,\"houseNumber\":\"7\",\"street\":\"West Shub Farm Street\",\"area\":\"Statesville\",\"city\":\"North Carolina\",\"pincode\":28625}],\"phoneNumbers\":[{\"id\":1,\"phoneNumber\":\"9945032765\"},{\"id\":2,\"phoneNumber\":\"9945132765\"}]}";
    Assert.assertEquals("input:Expected http response code ", HttpStatus.OK.value(), getResponseEntity.getStatusCodeValue());
    JSONAssert.assertEquals("input:Expected response ", responseJson, getResponseEntity.getBody(), false);

  }

  @Test
  public void test_create_person_existing_email() throws JSONException {
    String personOneRequestJson = "{\"firstName\":\"Cruz\",\"lastName\":\"Christie\",\"dateOfBirth\":\"21-07-1998\",\"emailId\":\"cruz.christie21@mailinator.com\",\"addressess\":[{\"houseNumber\":\"7761\",\"street\":\"Mayflower Lane\",\"area\":\"Altamonte Springs\",\"city\":\"Florida\",\"pincode\":32714},{\"houseNumber\":\"8485\",\"street\":\"Peachtree Street\",\"area\":\"Augusta\",\"city\":\"Georgia\",\"pincode\":30906}],\"phoneNumbers\":[{\"phoneNumber\":\"9047032765\"},{\"phoneNumber\":\"8945132765\"}]}";

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type","application/json");
    HttpEntity<String> personOneRequest = new HttpEntity<>(personOneRequestJson, headers);
    ResponseEntity<String> responseEntity =
        testRestTemplate.postForEntity("/person", personOneRequest, String.class);

    String personTwoRequestJson = "{\"firstName\":\"Oscar\",\"lastName\":\"Barrett\",\"dateOfBirth\":\"31-05-2001\",\"emailId\":\"cruz.christie21@mailinator.com\",\"addressess\":[{\"houseNumber\":\"3061\",\"street\":\"South Surrey Drive\",\"area\":\"Algonquin\",\"city\":\"Ilinois\",\"pincode\":60102}],\"phoneNumbers\":[{\"phoneNumber\":\"9047032765\"}]}";
    HttpEntity<String> personTwoRequest = new HttpEntity<>(personTwoRequestJson, headers);
    ResponseEntity<String> personTwoResponseEntity =
        testRestTemplate.postForEntity("/person", personTwoRequest, String.class);
    JSONObject jsonObject = new JSONObject(personTwoResponseEntity.getBody());
    int statusCode = jsonObject.getInt("status");
    Assert.assertEquals("input:Expected http response status code ", HttpStatus.INTERNAL_SERVER_ERROR.value(), statusCode);
  }
}
