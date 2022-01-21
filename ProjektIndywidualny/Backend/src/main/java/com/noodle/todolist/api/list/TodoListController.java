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
	
	@GetMapping("user/get")
	public ResponseEntity<TodoList> getListForUser(@RequestParam String listTitle) {
		return ResponseEntity.ok(listService.getList(listTitle));
	}
	
	@GetMapping("admin/get")
	public ResponseEntity<TodoList> getListForAdmin(@RequestParam String listTitle) {
		return ResponseEntity.ok(listService.getList(listTitle));
	}
	
	@PostMapping("admin/element/add/")
	public ResponseEntity<TodoElement> addElementToList(@RequestBody TodoElement element) {
		if (element == null) {
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
	
	@DeleteMapping("admin/element/remove/{elementId}")
	public ResponseEntity<Void> removeElementFromList(@PathVariable(name = "elementId") Long elementId) {
		if (elementId == null) {
			log.error("Cannot delete null element from list.");
			return ResponseEntity.badRequest().build();
		}
		
		// TODO: Only list we modify in this demo.
		TodoList list = listService.getList("test_list");
		TodoElement element = listService.getElementById(elementId);
		listService.deleteElementFromList(element.getTitle(), list.getTitle());
		
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("user/element/update/{elementId}")
	public ResponseEntity<Void> updateElement(@PathVariable(name = "elementId") Long elementId,
											  @RequestParam(required = false) String title,
											  @RequestParam(required = false) String description,
											  @RequestParam(required = false) boolean status) {
		
		listService.updateElement(elementId, title, description, status);
		
		return ResponseEntity.ok().build();
	}
	
	
}
