package com.noodle.todolist.api.list.element;

import lombok.ToString;

import javax.persistence.*;

@Entity
@Table
@ToString
public class TodoElementStatus {

	@Id
	@GeneratedValue()
	private Long id;
	private String status;
}
