package est.todolist.repository;

import est.todolist.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    // 제목 검색 기능
    List<Todo> findByTitleContaining(String keyword);

    // 완료 여부로 조회
    List<Todo> findByCompleted(boolean completed);

    // 마감일 기준 조회
    List<Todo> findByDueDateBefore(java.time.LocalDateTime now);
}

