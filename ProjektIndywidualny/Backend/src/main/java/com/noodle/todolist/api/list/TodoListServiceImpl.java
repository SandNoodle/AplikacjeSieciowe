package com.noodle.todolist.api.list;

import com.noodle.todolist.api.list.element.TodoElement;
import com.noodle.todolist.api.list.element.TodoElementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
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
		return listRepository.findByTitle(listTitle);
	}
	
	@Override
	public void createElement(TodoElement element) {
		log.info("Added '" + element.getTitle() + "'.");
		elementRepository.save(element);
	}
	
	@Override
	public TodoElement getElement(String elementTitle) {
		return elementRepository.findByTitle(elementTitle);
	}
	
	@Override
	public void deleteElement(String elementTitle) {
		TodoElement element = elementRepository.findByTitle(elementTitle);
		if (element == null) {
			log.error("Unable to delete null element.");
			return;
		}
		
		log.info("Removed '" + elementTitle + "'.");
		elementRepository.delete(element);
	}
	
	@Override
	public void addElementToList(String elementTitle, String listTitle) {
		TodoElement element = elementRepository.findByTitle(elementTitle);
		TodoList list = listRepository.findByTitle(listTitle);
		
		if (element == null) {
			log.error("Cannot add null element to list");
			return;
		}
		
		if (list == null) {
			log.error("Cannot add element to null list.");
			return;
		}
		
		log.info("Element '" + elementTitle + "' added to list '" + listTitle + "'.");
		list.getTodoElements().add(element);
	}
	
	@Override
	public List<TodoElement> getElementsFromList(String listTitle) {
		TodoList list = getList(listTitle);
		return new ArrayList<>(list.getTodoElements());
	}
	
	@Override
	public void deleteElementFromList(String elementTitle, String listTitle) {
		TodoElement element = elementRepository.findByTitle(elementTitle);
		TodoList list = listRepository.findByTitle(listTitle);
		
		log.info("Deleted '" + elementTitle + "' element from '" + listTitle + "' list.");
		list.getTodoElements().remove(element);
		elementRepository.delete(element); // NOTE: Element is not needed. Delete.
	}
}
