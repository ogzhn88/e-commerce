package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CartControllerTest {
    @Spy
    @InjectMocks
    CartController cartController;
    @Mock
    UserRepository userRepository;
    @Mock
    CartRepository cartRepository;
    @Mock
    ItemRepository itemRepository;


    @Before
    public void insert_data(){
        User user = new User();
        user.setUsername("test");
        user.setPassword("testPassword");
        Item item = new Item();
        item.setDescription("itemDesc");
        item.setId(1l);
        item.setPrice(BigDecimal.valueOf(10));
        Cart cart = new Cart();
        List<Item> items = new ArrayList<>();
        items.add(item);
        cart.setItems(items);
        cart.setTotal(BigDecimal.valueOf(10));
        cart.setUser(user);
        user.setCart(cart);
        when(userRepository.findByUsername("test")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));

    }

    @Test
    public void add_to_cart_happy_path(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(10);
        modifyCartRequest.setUsername("test");
        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(modifyCartRequest);
        assertEquals(HttpStatus.OK,cartResponseEntity.getStatusCode());
        Cart cart = cartResponseEntity.getBody();
        assertNotNull(cart);
        assertEquals(11,cart.getItems().size());
        assertEquals("test",cart.getUser().getUsername());
        assertEquals(BigDecimal.valueOf(110),cart.getTotal());

    }

    @Test
    public  void remove_from_cart_happy_path(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("test");
        ResponseEntity<Cart> cartResponseEntity = cartController.removeFromcart(modifyCartRequest);
        assertEquals(HttpStatus.OK,cartResponseEntity.getStatusCode());
        Cart cart = cartResponseEntity.getBody();
        assertNotNull(cart);
        assertEquals(0,cart.getItems().size());
        assertEquals("test",cart.getUser().getUsername());
        assertEquals(BigDecimal.valueOf(0),cart.getTotal());
    }
}
