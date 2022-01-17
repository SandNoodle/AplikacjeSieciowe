import type { NextPage } from "next";
import { TodoList } from "components/list/todolist";
import { useState } from "react";

const UserPage: NextPage = () => {
	const userName = "TEST USER";
	const [todoLists, setTodoLists] = useState(["Test","Second","Third","420","2137"]);

	// TODO: Display logged in user
	// TODO: fetch lists

	return (
		<div className="w-screen h-screen justify-center items-center flex bg-gradient-to-br from-emerald-400 via-green-400 to-lime-300">
			<div className="w-4/5 h-screen m-auto p-10 justify-center items-center">
				<div className="flex gap-4 flex-col p-10 justify-center items-center bg-white backdrop-filter backdrop-blur-lg bg-opacity-50 rounded-lg shadow-xl">
					<div className="">
						<h1 className="text-2xl font-bold">{userName}</h1>
					</div>

					{todoLists.length >= 1 ? (
						todoLists.map((list) => {
							return <TodoList listTitle={list} />;
						})
					) : (
						<div className="text-md italic">
							This user has no lists :c
						</div>
					)}
				</div>
			</div>
		</div>
	);
};

export default UserPage;
