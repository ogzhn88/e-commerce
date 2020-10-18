package com.example.demo;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ItemControllerTest {

    @Spy
    @InjectMocks
    ItemController itemController;
    @Mock
    ItemRepository itemRepository;

    @Before
    public void insert_data(){
        Item item1 = new Item();
        List<Item> itemList = new ArrayList<>();
        item1.setPrice(BigDecimal.valueOf(10));
        item1.setId(1L);
        item1.setDescription("item1Desc");
        item1.setName("item1");
        Item item2 = new Item();
        item2.setPrice(BigDecimal.valueOf(20));
        item2.setId(2L);
        item2.setDescription("item2Desc");
        item2.setName("item2");
        itemList.add(item1);
        itemList.add(item2);
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item1));
        when(itemRepository.findAll()).thenReturn(itemList);

    }


    @Test
    public void find_all_items_happy_path(){
        ResponseEntity<List<Item>> responseEntity = itemController.getItems();
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        List<Item> itemList = responseEntity.getBody();
        assertEquals(2,itemList.size());
    }

    @Test
    public void find_item_happy_path(){
        ResponseEntity<Item> responseEntity = itemController.getItemById(1L);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        Item myItem = responseEntity.getBody();
        assertNotNull(myItem);
        assertEquals("item1",myItem.getName());
        assertEquals("1",myItem.getId().toString());
        assertEquals("item1Desc",myItem.getDescription());
    }

}
