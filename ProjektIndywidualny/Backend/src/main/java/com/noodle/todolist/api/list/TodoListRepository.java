package com.noodle.todolist.api.list;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TodoListRepository extends JpaRepository<TodoList, Long> {
	Optional<TodoList> findByTitle(String title);
}
