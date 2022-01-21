
export type TodoElementType = {
	id: number;
	title: string;
	description: string;
	status: boolean;
};

export type TodoListType = {
	title: string;
	description: string;
	todoElements: [TodoElementType];
};
