package com.noodle.todolist.api.list;

import com.noodle.todolist.api.list.element.TodoElement;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

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
			
			// Create elements
			TodoElement first = new TodoElement(null, "First element", "This is a first element displayed", DONE);
			TodoElement second = new TodoElement(null, "Second element", "This contains some comments", NOT_DONE);
			TodoElement third = new TodoElement(null, "Third element", ":C", DONE);
	
			// All elements to repository
			listService.createElement(first);
			listService.createElement(second);
			listService.createElement(third);
			
			// Add elements to list
			listService.addElementToList(first.getTitle(), testList.getTitle());
			listService.addElementToList(second.getTitle(), testList.getTitle());
			listService.addElementToList(third.getTitle(), testList.getTitle());
			
			// Create random N elements
			for(int i = 0; i < 23; i++) {
				String tempTitle = "TEMP ELEMENT: " + i;
				boolean status = ThreadLocalRandom.current().nextBoolean();
				TodoElement tempElement = new TodoElement(null, tempTitle, "SOME RANDOM DESCRIPTION", status);
				listService.createElement(tempElement);
				listService.addElementToList(tempElement.getTitle(), testList.getTitle());
			}
			
		};
	}
	
}
