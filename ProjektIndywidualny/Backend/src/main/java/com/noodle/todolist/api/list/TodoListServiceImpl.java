package com.noodle.todolist.api.list;

import com.noodle.todolist.api.list.element.TodoElement;
import com.noodle.todolist.api.list.element.TodoElementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodoListServiceImpl implements TodoListService {
	
	private final TodoListRepository listRepository;
	private final TodoElementRepository elementRepository;
	
	@Override
	public void createList(TodoList todoList) {
		log.info("List '" + todoList.getTitle() + "' created.");
		listRepository.save(todoList);
	}
	
	@Override
	public TodoList getList(String listTitle) {
		Optional<TodoList> list = listRepository.findByTitle(listTitle);
		return list.orElseThrow();
	}
	
	@Override
	public void createElement(TodoElement element) {
		log.info("Added '" + element.getTitle() + "'.");
		elementRepository.save(element);
	}
	
	@Override
	public TodoElement getElement(String elementTitle) {
		return elementRepository.findByTitle(elementTitle).orElseThrow();
	}
	
	@Override
	public void deleteElement(String elementTitle) {
		Optional<TodoElement> element = elementRepository.findByTitle(elementTitle);
		if (element.isEmpty()) {
			log.info("Unable to delete null element.");
			return;
		}
		
		elementRepository.delete(element.get());
	}
	
	@Override
	public void addElementToList(String elementTitle, String listTitle) {
		Optional<TodoElement> elementOptional = elementRepository.findByTitle(elementTitle);
		Optional<TodoList> listOptional = listRepository.findByTitle(listTitle);
		
		TodoElement element = elementOptional.orElseThrow();
		TodoList list = listOptional.orElseThrow();
		
		log.info("Element '" + elementTitle + "' added to list '" + listTitle + "'.");
		list.getElements().add(element);
	}
	
	@Override
	public List<TodoElement> getElementsFromList(String listTitle) {
		TodoList list = getList(listTitle);
		return new ArrayList<>(list.getElements());
	}
	
	@Override
	public void deleteElementFromList(String elementTitle, String listTitle) {
		Optional<TodoElement> elementOptional = elementRepository.findByTitle(elementTitle);
		Optional<TodoList> listOptional = listRepository.findByTitle(listTitle);
		
		TodoList list = listOptional.orElseThrow();
		TodoElement element = elementOptional.orElseThrow();
		
		log.info("Deleted '" + elementTitle + "' element from '" + listTitle + "' list.");
		list.getElements().remove(element);
		elementRepository.delete(element); // NOTE: Element is not needed. Delete.
	}
}
