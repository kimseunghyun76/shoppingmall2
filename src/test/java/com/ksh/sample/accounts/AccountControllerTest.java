package com.ksh.sample.accounts;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksh.sample.Application;


/**
 * Created by jooyoung on 2015-09-12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Transactional
public class AccountControllerTest {

    @Autowired
    WebApplicationContext wac;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AccountService service;

    MockMvc mockMvc;

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void createAccount() throws Exception{
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUserName("sdbs2308");
        createDto.setPassword("password");

        ResultActions result= mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));

        result.andDo(print());
        result.andExpect(status().isCreated());

    }

    @Test
    public void createAccount_BadRequest() throws Exception {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUserName("   ");
        createDto.setPassword("1234");

        ResultActions result = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));

        result.andDo(print());
        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.code", is("bad.request")));
    }

    @Test
    public void createAccount_DuplicatedException() throws Exception{

        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUserName("sdbs2308");
        createDto.setPassword("password");

        ResultActions result= mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));

        result.andDo(print());
        result.andExpect(status().isCreated());

        //TODO JSON PATH
        //{"id":1,"userName":"sdbs2308","fullName":null,"joined":1442023337356,"updated":1442023337356}
        result.andExpect(jsonPath("$.userName", is("sdbs2308")));


        result= mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));

        result.andDo(print());
        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.code", is("duplicated.username.exception")));
    }

    @Test
    public void getAccounts() throws Exception{
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUserName("sdbs2308");
        createDto.setPassword("password");
        service.createAccount(createDto);

        ResultActions result = mockMvc.perform(get("/accounts"));

        result.andDo(print());
        result.andExpect(status().isOk());

    }
}
