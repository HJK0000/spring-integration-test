package org.example.springv3.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.swing.interop.SwingInterOpUtils;
import org.example.springv3.user.UserController;
import org.example.springv3.user.UserRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 1. 메모리에 다 띄우기 (filter ~ )
//@DataJpaTest // 컨트롤러는 메모리에 안뜨고 db만 띄움
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc // Mock 를 IOC에 띄워줌
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserControllerTest {

        @Autowired
        private MockMvc mvc; // 톰캣입구부터 들어가게 해준다. 포스트맨 같은거
        private ObjectMapper om = new ObjectMapper(); // 그냥 NEW 하면 됨


        @Test
        public void join_test() throws Exception {

                //given
                UserRequest.JoinDTO joinDTO = new UserRequest.JoinDTO();
                joinDTO.setUsername("haha");
                joinDTO.setPassword("1234");
                joinDTO.setEmail("haha@nate.com");

                String requestBody = om.writeValueAsString(joinDTO);
                //System.out.println(requestBody);
                //when

                ResultActions actions = mvc.perform(
                        MockMvcRequestBuilders.post("/join")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                ); // 실제 테스트 코드 동작 코드

                //eye

                String responseBody = actions.andReturn().getResponse().getContentAsString();
                //System.out.println(responseBody);

                //then
                actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
                actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
                actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(4));
                actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.username").value("haha"));
                actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.email").value("haha@nate.com"));
                actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.profile").isEmpty());

        }

        @Test
        public void login_test() throws Exception {

                //given
                UserRequest.LoginDTO loginDTO = new UserRequest.LoginDTO();
                loginDTO.setUsername("ssar");
                loginDTO.setPassword("1234");

                String requestBody = om.writeValueAsString(loginDTO);
                System.out.println(requestBody);
                //when

                ResultActions actions = mvc.perform(
                        MockMvcRequestBuilders.post("/login")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                ); // 실제 테스트 코드 동작 코드

                //eye

                String responseBody = actions.andReturn().getResponse().getContentAsString();
                System.out.println(responseBody);

                String responseJwt = actions.andReturn().getResponse().getHeader("Authorization");
                System.out.println(responseJwt);

                // then
                actions.andExpect(header().string("Authorization", Matchers.notNullValue()));
                actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
                actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
                actions.andExpect(MockMvcResultMatchers.jsonPath("$.body").isEmpty());


        }

}
