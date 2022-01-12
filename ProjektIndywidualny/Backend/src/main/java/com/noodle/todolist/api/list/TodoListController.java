package com.noodle.todolist.api.list;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/api/list")
public class TodoListController {
	
	private final TodoListService todoListService;

	
}
