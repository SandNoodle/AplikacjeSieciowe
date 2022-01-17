package com.noodle.todolist.api.list.element;

import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "elements")
@ToString
public class TodoElement {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "title", nullable = false)
	private String title;
	
	@Column(name = "description", nullable = true)
	private String description;
	
	@Column(name = "status", nullable = false)
	private String status;
}
