package com.example.planner.service

import com.example.planner.domain.todo.Todo
import com.example.planner.repository.ScheduleRepository
import com.example.planner.repository.TodoRepository
import org.springframework.stereotype.Service

@Service
class TodoService(
    private val todoRepository: TodoRepository,
    private val scheduleRepository: ScheduleRepository
) {

    fun createTodo(scheduleId: Long, content: String): Todo {

        val schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow { IllegalArgumentException("Schedule not found") }

        val todo = Todo(
            content = content,
            schedule = schedule
        )

        return todoRepository.save(todo)
    }

    fun getTodos(): List<Todo> {
        return todoRepository.findAll()
    }

    fun completeTodo(todoId: Long) {

        val todo = todoRepository.findById(todoId)
            .orElseThrow { IllegalArgumentException("Todo not found") }

        val updated = Todo(
            todoId = todo.todoId,
            content = todo.content,
            completed = true,
            schedule = todo.schedule
        )

        todoRepository.save(updated)
    }
}