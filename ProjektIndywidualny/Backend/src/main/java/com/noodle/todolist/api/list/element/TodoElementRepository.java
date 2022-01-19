package com.noodle.todolist.api.list.element;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TodoElementRepository extends JpaRepository<TodoElement, Long> {
	Optional<TodoElement> findByTitle(String title);
}
