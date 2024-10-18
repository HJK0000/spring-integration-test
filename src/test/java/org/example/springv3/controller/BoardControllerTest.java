package org.example.springv3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springv3.board.BoardRequest;
import org.example.springv3.core.util.JwtUtil;
import org.example.springv3.user.User;
import org.example.springv3.user.UserRequest;
import org.hamcrest.Matchers;
import org.hibernate.validator.internal.IgnoreForbiddenApisErrors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

// 1. 메모리에 다 띄우기 (filter ~ )
//@DataJpaTest // 컨트롤러는 메모리에 안뜨고 db만 띄움
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc // Mock 를 IOC에 띄워줌
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BoardControllerTest {

        @Autowired
        private MockMvc mvc; // 톰캣입구부터 들어가게 해준다. 포스트맨 같은거
        private ObjectMapper om = new ObjectMapper(); // 그냥 NEW 하면 됨
        private String accessToken;

        @BeforeEach
        public void setUp(){
                System.out.println("나 실행됨?");
                User sessionUser = User.builder().id(1).username("ssar").build();
                accessToken = JwtUtil.create(sessionUser);
        }


        @Test
        public void save_test() throws Exception {

                //given
                BoardRequest.SaveDTO reqDTO = new BoardRequest.SaveDTO();
                reqDTO.setTitle("title 11");
                reqDTO.setContent("content 11");

                String requestBody = om.writeValueAsString(reqDTO);
                System.out.println(requestBody);

                //when

                ResultActions actions = mvc.perform(
                        MockMvcRequestBuilders.post("/api/board")
                                .header("Authorization", "Bearer " + accessToken)
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                ); // 실제 테스트 코드 동작 코드

                //eye

                String responseBody = actions.andReturn().getResponse().getContentAsString();
                System.out.println(responseBody);

                //then
/*                actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
                actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
                actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(4));*/

        }

        @Test
        public void delete_test() throws Exception {

                //given
                User sessionUser = User.builder().id(1).username("ssar").build();
                String accessToken = JwtUtil.create(sessionUser);
                System.out.println(accessToken);

                int id = 1;

                //when

                ResultActions actions = mvc.perform(
                        MockMvcRequestBuilders.delete("/api/board/" + id)
                                .header("Authorization", "Bearer " + accessToken)
                );

                //eye

                String responseBody = actions.andReturn().getResponse().getContentAsString();
                System.out.println(responseBody);

                //then
/*                actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
                actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
                actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(4));*/

        }



}
