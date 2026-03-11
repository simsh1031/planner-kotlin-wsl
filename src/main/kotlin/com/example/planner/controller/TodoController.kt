package com.example.planner.controller

import com.example.planner.dto.todo.*
import com.example.planner.service.TodoService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/todos")
class TodoController(
    private val todoService: TodoService
) {

    @PostMapping
    fun createTodo(
        @RequestBody request: CreateTodoRequest
    ): TodoResponse {

        val todo = todoService.createTodo(
            scheduleId = request.scheduleId,
            content = request.content
        )

        return TodoResponse(
            todoId = todo.todoId,
            scheduleId = todo.schedule.scheduleId!!,
            content = todo.content,
            completed = todo.completed
        )
    }

    @GetMapping
    fun getTodos(
        @RequestParam(required = false) completed: Boolean?
    ): List<TodoResponse> {

        val todos = if (completed != null) {
            todoService.getTodosByCompleted(completed)
        } else {
            todoService.getTodos()
        }

        return todos.map {
            TodoResponse(
                todoId = it.todoId,
                scheduleId = it.schedule.scheduleId!!,
                content = it.content,
                completed = it.completed
            )
        }
    }

    @PatchMapping("/{todoId}/complete")
    fun completeTodo(
        @PathVariable todoId: Long
    ): CompleteTodoResponse {

        val todo = todoService.completeTodo(todoId)

        return CompleteTodoResponse(
            todoId = todo.todoId,
            completed = todo.completed
        )
    }
}