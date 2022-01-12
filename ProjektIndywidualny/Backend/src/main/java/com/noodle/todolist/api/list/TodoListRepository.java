package com.noodle.todolist.api.list;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoListRepository extends JpaRepository<TodoList, Long> {
	TodoList findByTitle(String title);
}
