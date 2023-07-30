package com.suite.suite_suite_room_service.suiteRoom.controller;

import com.google.gson.Gson;
import com.suite.suite_suite_room_service.suiteRoom.dto.ReqSuiteRoomDto;
import com.suite.suite_suite_room_service.suiteRoom.dto.ResSuiteRoomDto;
import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.mockEntity.MockSuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;
import com.suite.suite_suite_room_service.suiteRoom.service.SuiteRoomService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@DataJpaTest
public class SuiteRoomControllerTest {

    @InjectMocks
    private SuiteRoomController suiteRoomController;
    @MockBean
    private SuiteRoomService suiteRoomService;

    private MockMvc mockMvc;
    private Gson gson;
    @BeforeEach
    public void init() {
        gson = new Gson();
        mockMvc = MockMvcBuilders.standaloneSetup(suiteRoomController)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }


    @Test
    @DisplayName("인증O - 스위트룸 그룹 목록 확인")
    @WithMockUser(username = "hwany", roles = "USER")
    public void listUpRooms() throws Exception {

        //given
        final String url = "/suite/suiteroom";

        suiteRoomService.createSuiteRoom(MockSuiteRoom.getMockSuiteRoom("test",true), getMockAuthorizer("hwany"));
        suiteRoomService.createSuiteRoom(MockSuiteRoom.getMockSuiteRoom("test1",true), getMockAuthorizer("mini"));
        List<ResSuiteRoomDto> suiteRooms = suiteRoomService.getAllSuiteRooms();

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );
        //then
        //org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(suiteRooms.size()))
                .andExpect(jsonPath("$[0]").value(suiteRooms.get(0)))
                .andExpect(jsonPath("$[1]").value(suiteRooms.get(1)));

    }

    // 이것도 분리하면 어떨까?
    private AuthorizerDto getMockAuthorizer(String name) {
        return AuthorizerDto.builder()
                .memberId(Long.parseLong("1"))
                .accountStatus("ACTIVIATE")
                .name(name)
                .nickName("Darren")
                .email("zxz4641@gmail.com")
                .role("ROLE_USER").build();
    }
}
