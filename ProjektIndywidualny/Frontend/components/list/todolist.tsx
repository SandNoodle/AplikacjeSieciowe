import UserPage from "pages/userpage";

type TodoListType = {
	listTitle: string;
};

export const TodoList = (props: TodoListType) => {
	return (
		<div className="flex justify-center items-center flex-row gap-4 min-w-full h-auto mx-auto p-4 rounded-md bg-white backdrop-filter backdrop-blur-lg bg-opacity-5 hover:bg-opacity-50 transition duration-200 shadow-md hover:shadow-lg">
			<div className="mr-auto">
				<h1 className="font-bold uppercase">{props.listTitle}</h1>
			</div>

			<div className="flex gap-4 pr-4">
				<button className="font-medium bg-transparent rounded-md px-2 py-1 hover:shadow-md transition duration-200 hover:bg-slate-500 hover:text-neutral-200">Edit</button>
				<button className="font-medium bg-transparent rounded-md px-2 py-1 hover:shadow-md transition duration-200 hover:bg-red-500 hover:text-neutral-50">Delete</button>
			</div>
		</div>
	);
};
