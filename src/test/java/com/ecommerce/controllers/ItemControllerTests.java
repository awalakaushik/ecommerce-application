package com.ecommerce.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class ItemControllerTests {
    @Autowired
    private MockMvc mvc;

    @Test
    public void getItems() throws Exception {
        mvc.perform(get("/api/item"))
                .andExpect(status().isOk());
    }

    @Test
    public void getItemById() throws Exception {
        mvc.perform(get("/api/item/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getItemsByName() throws Exception {
        mvc.perform(get("/api/item/name/Round Widget"))
                .andExpect(status().isOk());
    }
}
