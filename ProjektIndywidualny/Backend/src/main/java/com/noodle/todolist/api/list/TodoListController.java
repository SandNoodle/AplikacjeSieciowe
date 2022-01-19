package com.noodle.todolist.api.list;


import com.noodle.todolist.api.list.element.TodoElement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/list")
public class TodoListController {

	private final TodoListService listService;
	
	@GetMapping("get")
	public ResponseEntity<TodoList> getList(@RequestParam String listTitle) {
		return ResponseEntity.ok(listService.getList(listTitle));
	}
	
	// TODO: Delete when testing is complete.
	@GetMapping("test")
	public ResponseEntity<List<TodoElement>> elementTest() {
		return ResponseEntity.ok(listService.getElementsFromList("test_list"));
	}
	
	@PostMapping("element/add/")
	public ResponseEntity<TodoElement> addElementToList(@RequestBody TodoElement element) {
		if(element == null) {
			log.error("Element is null. Bad request");
			return ResponseEntity.badRequest().build();
		}

		// TODO: Only list we modify in this demo.
		TodoList list = listService.getList("test_list");
		listService.createElement(element);
		listService.addElementToList(element.getTitle(), list.getTitle());
		
		// Successful
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("element/remove/{elementId}")
	public ResponseEntity<Void> removeElementFromList(@PathVariable(name = "elementId") Long elementId) {
		if(elementId == null) {
			log.error("Cannot delete null element from list.");
			return ResponseEntity.badRequest().build();
		}
		
		// TODO: Only list we modify in this demo.
		TodoList list = listService.getList("test_list");
		TodoElement element = listService.getElementById(elementId);
		listService.deleteElementFromList(element.getTitle(), list.getTitle());
		
		return ResponseEntity.ok().build();
	}
		
	

}
