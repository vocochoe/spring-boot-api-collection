package est.hospitalbooking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import est.hospitalbooking.dto.appointment.AppointmentRequest;
import est.hospitalbooking.dto.appointment.AppointmentResponse;
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

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long patientId;
    private Long doctorId;

    @BeforeEach
    void setup() throws Exception {
        // 환자 계정 생성
        UserRequest patientReq = UserRequest.builder()
                .username("patient02")
                .password("qwer")
                .role("PATIENT")
                .build();

        String patientJson = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientReq)))
                .andReturn().getResponse().getContentAsString();

        patientId = objectMapper.readValue(patientJson, UserResponse.class).getId();

        // 의사 계정 + Doctor 등록
        UserRequest doctorUserReq = UserRequest.builder()
                .username("drpark01")
                .password("abcd")
                .role("DOCTOR")
                .build();

        String doctorUserJson = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorUserReq)))
                .andReturn().getResponse().getContentAsString();

        Long doctorUserId = objectMapper.readValue(doctorUserJson, UserResponse.class).getId();

        DoctorRequest doctorReq = DoctorRequest.builder()
                .name("박민수")
                .specialty("정형외과")
                .userId(doctorUserId)
                .build();

        String doctorJson = mockMvc.perform(post("/api/doctors?adminUserId=1") // 미리 ADMIN 계정 data.sql에 있어야 함
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorReq)))
                .andReturn().getResponse().getContentAsString();

        doctorId = objectMapper.readValue(doctorJson, DoctorResponse.class).getId();
    }

    @Test
    void 예약_생성_API_테스트() throws Exception {
        // Given
        AppointmentRequest request = AppointmentRequest.builder()
                .patientName("홍길동")
                .appointmentTime(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0))
                .patientId(patientId)
                .build();

        // When
        String responseJson = mockMvc.perform(post("/api/doctors/" + doctorId + "/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AppointmentResponse response = objectMapper.readValue(responseJson, AppointmentResponse.class);

        // Then
        assertThat(response.getPatientName()).isEqualTo("홍길동");
        assertThat(response.getDoctorId()).isEqualTo(doctorId);
        assertThat(response.getPatientId()).isEqualTo(patientId);
    }
}
