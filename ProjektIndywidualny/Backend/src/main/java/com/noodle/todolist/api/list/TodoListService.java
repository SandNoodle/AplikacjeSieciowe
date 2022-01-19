package com.noodle.todolist.api.list;

import com.noodle.todolist.api.list.element.TodoElement;

import java.util.List;

public interface TodoListService {
	
	// Lists
	void createList(TodoList todoList);
	
	TodoList getList(String listTitle);
	
//	void deleteList(String listTitle);
	
	// Elements
	
	void createElement(TodoElement element);
	
	TodoElement getElement(String elementTitle);
	
	TodoElement getElementById(Long id);
	
	void deleteElement(String elementTitle);
	
	
	// Elements and Lists
	
	void addElementToList(String elementTitle, String listTitle);
	
	List<TodoElement> getElementsFromList(String listTitle);
	
	void deleteElementFromList(String elementTitle, String listTitle);
	
}
