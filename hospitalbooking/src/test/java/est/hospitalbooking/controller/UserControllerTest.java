package est.hospitalbooking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import est.hospitalbooking.dto.user.UserRequest;
import est.hospitalbooking.dto.user.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 사용자_생성_API_테스트() throws Exception {
        // Given
        UserRequest request = UserRequest.builder()
                .username("testUser01")
                .password("1234")
                .role("PATIENT")
                .build();

        // When
        String responseJson = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserResponse response = objectMapper.readValue(responseJson, UserResponse.class);

        // Then
        assertThat(response.getUsername()).isEqualTo("testUser01");
        assertThat(response.getRole()).isEqualTo("PATIENT");
    }
}
