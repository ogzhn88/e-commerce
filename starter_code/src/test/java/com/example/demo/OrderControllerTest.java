package com.example.demo;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
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

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class OrderControllerTest {

    @Spy
    @InjectMocks
    OrderController orderController;
    @Mock
    OrderRepository orderRepository;
    @Mock
    UserRepository userRepository;

    @Before
    public void insert_data(){
        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        BigDecimal price = BigDecimal.valueOf(10);
        item.setPrice(price);
        item.setDescription("A widget that is round");
        List<Item> items = new ArrayList<Item>();
        items.add(item);

        User user = new User();
        Cart cart = new Cart();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("testPassword");
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(items);
        BigDecimal total = BigDecimal.valueOf(10);
        cart.setTotal(total);
        user.setCart(cart);
        UserOrder userOrder = new UserOrder();
        userOrder.setUser(user);
        userOrder.setItems(items);
        userOrder.setUser(user);
        userOrder.setTotal(total);
        UserOrder dummyOrder = new UserOrder();
        List<UserOrder> userOrders = new ArrayList<>();
        userOrders.add(userOrder);
        userOrders.add(dummyOrder);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(userOrders);


    }

    @Test
    public void submit_happy_path(){
        ResponseEntity<UserOrder> userOrderResponseEntity = orderController.submit("test");
        assertNotNull(userOrderResponseEntity);
        assertEquals(HttpStatus.OK,userOrderResponseEntity.getStatusCode());
        UserOrder userOrder = userOrderResponseEntity.getBody();
        assertNotNull(userOrder.getUser());
        assertEquals(1L, userOrder.getItems().size());

     }

     @Test
     public void get_orders_for_user_happy_path(){
       ResponseEntity<List<UserOrder>> userOrdersResponseEntity = orderController.getOrdersForUser("test");
       assertNotNull(userOrdersResponseEntity);
       assertEquals(HttpStatus.OK,userOrdersResponseEntity.getStatusCode());
       List<UserOrder> myOrders = userOrdersResponseEntity.getBody();
       assertNotNull(myOrders);
       assertEquals(2,myOrders.size());
     }

}
