package com.noodle.todolist.api.list;


import com.noodle.todolist.api.list.element.TodoElement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/list")
public class TodoListController {
	
	private final TodoListService listService;
	
	private final int MAX_ELEMENTS_PER_PAGE = 5;
	
	@GetMapping(value = {"user/get_paged/count", "admin/get_paged/count"})
	public ResponseEntity<Map<String, Long>> getPagesCount() {
		// TODO: Only list we modify in this demo.
		TodoList list = listService.getList("test_list");
		int elementCount = list.getTodoElements().size();
		long pageCount = (long) Math.ceil((double) elementCount / MAX_ELEMENTS_PER_PAGE);
		
		Map<String, Long> response = new HashMap<>();
		response.put("page_count", pageCount);
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping(value = {"user/get_paged/{pageNumber}", "admin/get_paged/{pageNumber}"})
	public ResponseEntity<TodoList> getPagedList(@PathVariable(name = "pageNumber") Long pageNumber) {
		// TODO: Only list we modify in this demo.
		TodoList list = listService.getList("test_list");
		
		// Leave only elements from range:
		// start = MAX_ELEMENTS_PER_PAGE * pageNumber
		// end = start + MAX_ELEMENTS_PER_PAGE;
		// [start, end)
		TodoList pagedList = new TodoList(list);
		int listSize = list.getTodoElements().size();
		int startIndex = Math.min((int) (pageNumber * MAX_ELEMENTS_PER_PAGE), listSize);
		int endIndex = Math.min(startIndex + MAX_ELEMENTS_PER_PAGE, listSize);
		pagedList.setTodoElements(pagedList.getTodoElements().subList(startIndex, endIndex));
		
		return ResponseEntity.ok(pagedList);
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
