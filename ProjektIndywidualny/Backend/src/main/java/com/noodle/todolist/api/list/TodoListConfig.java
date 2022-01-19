package com.noodle.todolist.api.list;

import com.noodle.todolist.api.list.element.TodoElement;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;

import static com.noodle.todolist.api.list.element.TodoElement.DONE;
import static com.noodle.todolist.api.list.element.TodoElement.NOT_DONE;

@Configuration
public class TodoListConfig {
	
	@Bean
	CommandLineRunner listConfigRunner(TodoListService listService) {
		return args -> {
			
			// Create test list.
			TodoList testList = new TodoList(null, "test_list", "This is a test list.", new ArrayList<>());
			listService.createList(testList);
			
			// Add random elements to list
			TodoElement first = new TodoElement(0L, "First element", "This is a first element displayed", DONE);
			TodoElement second = new TodoElement(1L, "Second element", "This contains some comments", NOT_DONE);
			TodoElement third = new TodoElement(2L, "Third element", ":C", DONE);
			
			testList.setElements(Arrays.asList(first, second, third));
			
		};
	}
	
}
