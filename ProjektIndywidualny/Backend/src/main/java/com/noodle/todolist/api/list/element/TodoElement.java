package com.noodle.todolist.api.list.element;

import lombok.ToString;

import javax.persistence.*;

@Entity
@Table
@ToString
public class TodoElement {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private String title;
	private String description; // OPTIONAL - TODO: Find if this can be annotated as such
	private TodoElementStatus status;
}
