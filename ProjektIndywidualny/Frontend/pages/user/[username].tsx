import type { GetServerSideProps, NextPage } from "next";
import { TodoList } from "components/list/todolist";
import { useState, useEffect } from "react";

const UserPage: NextPage = (props) => {
	//const [isLoadingLists, setIsLoadingLists] = useState(true);
	
	const [username, setUsername] = useState("Test_User");
	const [todoLists, setTodoLists] = useState([
		"Test",
		"Second",
		"Third",
		"420",
		"2137",
	]);

	// TODO: W przypadku działającego backendu jest możliwość 
	// zmiany poniższej funkcji na:
	// const res = await fetch("http://localhost:3000/api/user/get/")
	// const data = await res.json();
	// i odczytu danych użytkownika takich jak listy, wysyłanie zapytań o modyfikację etc.
	useEffect(async () => {
		const res = await fetch("http://localhost:3000/api/user/all/usernames");
		const data = await res.json();

		setUsername(data[0]);
	});

	// TODO: fetch lists

	return (
		<div className="w-screen h-screen justify-center items-center flex bg-gradient-to-br from-emerald-400 via-green-400 to-lime-300">
			<div className="w-4/5 h-screen m-auto p-10 justify-center items-center">
				<div className="flex gap-4 flex-col p-10 justify-center items-center bg-white backdrop-filter backdrop-blur-lg bg-opacity-50 rounded-lg shadow-xl">
					<div className="">
						<h1 className="text-2xl font-bold">{username}</h1>
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
