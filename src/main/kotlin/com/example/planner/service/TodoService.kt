package com.example.planner.service

import com.example.planner.domain.todo.Todo
import com.example.planner.repository.ScheduleRepository
import com.example.planner.repository.TodoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TodoService(
    private val todoRepository: TodoRepository,
    private val scheduleRepository: ScheduleRepository
) {

    fun createTodo(scheduleId: Long, content: String): Todo {
        val schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow { IllegalArgumentException("Schedule not found") }
        return todoRepository.save(Todo(content = content, schedule = schedule))
    }

    fun getTodos(userId: Long): List<Todo> {
        return todoRepository.findByScheduleUserUserId(userId)  // ✅ userId로 필터링
    }

    fun getTodosByCompleted(userId: Long, completed: Boolean): List<Todo> {
        return todoRepository.findByScheduleUserUserIdAndCompleted(userId, completed)  // ✅
    }

    fun completeTodo(todoId: Long): Todo {
        val todo = todoRepository.findById(todoId)
            .orElseThrow { IllegalArgumentException("Todo not found") }
        return todoRepository.save(
            Todo(todoId = todo.todoId, content = todo.content, completed = true, dueDate = todo.dueDate, schedule = todo.schedule)
        )
    }
}