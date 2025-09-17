package est.todolist.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import est.todolist.dto.TodoRequest;
import est.todolist.dto.TodoResponse;
import est.todolist.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    void cleanUp() {
        todoRepository.deleteAll();
    }

    @Test
    @DisplayName("전체 Todo 목록 조회")
    void testGetAllTodos() throws Exception {
        // Given
        TodoRequest request = TodoRequest.builder()
                .title("공부하기")
                .description("Spring Boot 복습")
                .dueDate(LocalDateTime.now().plusDays(1))
                .build();

        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        // When
        MvcResult result = mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        List<TodoResponse> responses =
                objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getTitle()).isEqualTo("공부하기");
    }

    @Test
    @DisplayName("Todo 생성")
    void testCreateTodo() throws Exception {
        // Given
        TodoRequest request = TodoRequest.builder()
                .title("JUnit 테스트 할 일")
                .description("테스트 코드 작성")
                .dueDate(LocalDateTime.now().plusDays(1))
                .build();

        String requestBody = objectMapper.writeValueAsString(request);

        // When
        MvcResult result = mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        TodoResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), TodoResponse.class);

        // Then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getTitle()).isEqualTo("JUnit 테스트 할 일");
        assertThat(response.isCompleted()).isFalse();
    }

    @Test
    @DisplayName("Todo 완료 상태 변경")
    void testUpdateCompleted() throws Exception {
        // Given
        TodoRequest request = TodoRequest.builder()
                .title("완료 상태 테스트")
                .description("상태 변경 확인")
                .dueDate(LocalDateTime.now().plusDays(1))
                .build();

        String requestBody = objectMapper.writeValueAsString(request);

        MvcResult postResult = mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        TodoResponse created = objectMapper.readValue(postResult.getResponse().getContentAsString(), TodoResponse.class);

        // When
        MvcResult result = mockMvc.perform(put("/api/todos/" + created.getId() + "?completed=true"))
                .andExpect(status().isOk())
                .andReturn();

        TodoResponse updated = objectMapper.readValue(result.getResponse().getContentAsString(), TodoResponse.class);

        // Then
        assertThat(updated.isCompleted()).isTrue();
    }

    @Test
    @DisplayName("Todo 삭제")
    void testDeleteTodo() throws Exception {
        // Given
        TodoRequest request = TodoRequest.builder()
                .title("삭제 테스트")
                .description("삭제용 데이터")
                .dueDate(LocalDateTime.now().plusDays(1))
                .build();

        String requestBody = objectMapper.writeValueAsString(request);

        MvcResult postResult = mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        TodoResponse created = objectMapper.readValue(postResult.getResponse().getContentAsString(), TodoResponse.class);

        // When
        mockMvc.perform(delete("/api/todos/" + created.getId()))
                .andExpect(status().isNoContent());

        // Then
        assertThat(todoRepository.findById(created.getId())).isEmpty();
    }
}
