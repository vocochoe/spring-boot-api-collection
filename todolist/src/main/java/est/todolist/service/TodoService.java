package est.todolist.service;

import est.todolist.domain.Todo;
import est.todolist.dto.TodoRequest;
import est.todolist.dto.TodoResponse;
import est.todolist.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;

    /**
     * 전체 할 일 목록 조회
     */
    public List<TodoResponse> findAll() {

        return todoRepository.findAll().stream()
                .map(TodoResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * [추가] 제목으로 검색
     */
    public List<TodoResponse> searchByTitle(String keyword) {

        return todoRepository.findByTitleContaining(keyword).stream()
                .map(TodoResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * [추가] 완료 여부로 조회
     * true: 완료, false: 미완료
     */
    public List<TodoResponse> findByCompleted(boolean completed) {

        return todoRepository.findByCompleted(completed).stream()
                .map(TodoResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * [추가] 기한이 지난 todo 조회
     */
    public List<TodoResponse> findOverdueTodos() {

        return todoRepository.findByDueDateBefore(LocalDateTime.now()).stream()
                .map(TodoResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 할 일 생성
     * completed : 기본으로 false 설정
     */
    @Transactional
    public TodoResponse create(TodoRequest request) {
        Todo todo = Todo.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .completed(false)
                .build();

        Todo saved = todoRepository.save(todo);

        return new TodoResponse(saved);
    }

    /**
     * 완료 상태 변경
     */
    @Transactional
    public TodoResponse updateCompleted(Long id, boolean completed) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 Todo가 없습니다. id=" + id));

        todo = Todo.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .description(todo.getDescription())
                .dueDate(todo.getDueDate())
                .createdAt(todo.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .completed(completed)
                .build();

        Todo updated = todoRepository.save(todo);

        return new TodoResponse(updated);
    }

    /**
     * 삭제
     */
    @Transactional
    public void delete(Long id) {

        if (!todoRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 Todo가 존재하지 않습니다. id=" + id);
        }

        todoRepository.deleteById(id);
    }

}
