package com.noodle.todolist.api.list.element;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoElementRepository extends JpaRepository<TodoElement, Long> {
	TodoElement findByTitle(String title);
}
