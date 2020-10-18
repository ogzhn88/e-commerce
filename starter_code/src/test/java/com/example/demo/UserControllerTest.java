package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;


import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
public class UserControllerTest {

    @Spy
    @InjectMocks
    private UserController userController;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder ;

    @Before
    public void insert_user(){
        when(bCryptPasswordEncoder.encode("dummyPassword")).thenReturn("dummyHashedPassword");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("dummyUser");
        r.setPassword("dummyPassword");
        r.setConfirmPassword("dummyPassword");
        ResponseEntity<User> user = userController.createUser(r);
        User u = user.getBody();
        u.setId(1L);
        when(userRepository.findById(1l)).thenReturn(java.util.Optional.of(u));
        when(userRepository.findByUsername("dummyUser")).thenReturn(u);

    }


    @Test
    public void create_user_happy_path() {
       when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");
        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());

    }

    @Test
    public void get_user_by_id_happy_path(){
        ResponseEntity<User> userResponse = userController.findById(1L);
        assertNotNull(userResponse);
        assertEquals(200, userResponse.getStatusCodeValue());
        assertEquals("dummyUser",userResponse.getBody().getUsername());
        assertEquals(1L,userResponse.getBody().getId());
    }
    @Test
    public void  get_user_by_username_happy_path(){
        ResponseEntity<User> userResponse = userController.findByUserName("dummyUser");
        assertNotNull(userResponse);
        assertEquals(HttpStatus.OK,userResponse.getStatusCode());
        assertEquals(1L , userResponse.getBody().getId());
        assertEquals("dummyUser",userResponse.getBody().getUsername());
        System.out.println(userResponse.getBody().toString());
        assertEquals("dummyHashedPassword",userResponse.getBody().getPassword());
    }

    @Test
    public  void  check_password(){
        CreateUserRequest r1 = new CreateUserRequest();
        r1.setUsername("test");
        r1.setPassword("short");
        r1.setConfirmPassword("short");
        final ResponseEntity<User> response1 = userController.createUser(r1);
        assertNotNull(response1);
        assertNotEquals(HttpStatus.OK,response1.getStatusCode());
        CreateUserRequest r2 = new CreateUserRequest();
        r2.setUsername("test");
        r2.setPassword("password");
        r2.setConfirmPassword("somethingElse");
        final ResponseEntity<User> response2 = userController.createUser(r2);
        assertNotNull(response1);
        assertNotEquals(HttpStatus.OK,response2.getStatusCode());
    }
}