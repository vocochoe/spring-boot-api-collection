package est.todolist.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoRequest {
    private String title;
    private String description;       // 상세 내용
    private LocalDateTime dueDate;    // 마감일
}
