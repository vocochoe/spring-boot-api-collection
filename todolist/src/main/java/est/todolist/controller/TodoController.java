package est.todolist.controller;

import est.todolist.dto.TodoRequest;
import est.todolist.dto.TodoResponse;
import est.todolist.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    /**
     * 전체 조회
     * GET /api/todos
     */
    @GetMapping
    public ResponseEntity<List<TodoResponse>> getAllTodos(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "completed", required = false) Boolean completed
    ) {
        // 제목 검색
        if (title != null) {
            return ResponseEntity.ok(todoService.searchByTitle(title));
        }
        // 완료 여부 검색
        if (completed != null) {
            return ResponseEntity.ok(todoService.findByCompleted(completed));
        }
        // 전체 조회
        return ResponseEntity.ok(todoService.findAll());
    }

    /**
     * 마감일 지난 Todo 조회
     * GET /api/todos/overdue
     */
    @GetMapping("/overdue")
    public ResponseEntity<List<TodoResponse>> getOverdueTodos() {
        return ResponseEntity.ok(todoService.findOverdueTodos());
    }

    /**
     * 생성
     * POST /api/todos
     */
    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(@RequestBody TodoRequest request) {
        return ResponseEntity.ok(todoService.create(request));
    }

    /**
     * 완료 상태 변경
     * PUT /api/todos/{id}?completed=true
     */
    @PutMapping("/{id}")
    public ResponseEntity<TodoResponse> updateCompleted(
            @PathVariable Long id,
            @RequestParam boolean completed
    ) {
        return ResponseEntity.ok(todoService.updateCompleted(id, completed));
    }

    /**
     * 삭제
     * DELETE /api/todos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
