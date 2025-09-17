package est.hospitalbooking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import est.hospitalbooking.dto.doctor.DoctorRequest;
import est.hospitalbooking.dto.doctor.DoctorResponse;
import est.hospitalbooking.dto.user.UserRequest;
import est.hospitalbooking.dto.user.UserResponse;
import org.junit.jupiter.api.BeforeEach;
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
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long adminId;
    private Long doctorUserId;

    @BeforeEach
    void setup() throws Exception {
        // 관리자 계정 생성
        UserRequest adminReq = UserRequest.builder()
                .username("admin02")
                .password("1234")
                .role("ADMIN")
                .build();

        String adminJson = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminReq)))
                .andReturn().getResponse().getContentAsString();

        adminId = objectMapper.readValue(adminJson, UserResponse.class).getId();

        // 의사 계정 생성
        UserRequest doctorReq = UserRequest.builder()
                .username("drlee01")
                .password("abcd")
                .role("DOCTOR")
                .build();

        String doctorJson = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorReq)))
                .andReturn().getResponse().getContentAsString();

        doctorUserId = objectMapper.readValue(doctorJson, UserResponse.class).getId();
    }

    @Test
    void 의사_등록_API_테스트() throws Exception {
        // Given
        DoctorRequest request = DoctorRequest.builder()
                .name("이영희")
                .specialty("소아과")
                .userId(doctorUserId)
                .build();

        // When
        String responseJson = mockMvc.perform(post("/api/doctors?adminUserId=" + adminId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        DoctorResponse response = objectMapper.readValue(responseJson, DoctorResponse.class);

        // Then
        assertThat(response.getName()).isEqualTo("이영희");
        assertThat(response.getSpecialty()).isEqualTo("소아과");
        assertThat(response.getUserId()).isEqualTo(doctorUserId);
    }
}
