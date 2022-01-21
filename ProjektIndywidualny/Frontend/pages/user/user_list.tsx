import type { GetServerSideProps, NextPage } from "next";
import { TodoElement } from "components/list/user_todoelement";
import { useState, useEffect } from "react";

import { TodoListType, TodoElementType } from "types/list_types";
import { REST_API_IP } from "lib/server_requests";
import Cookies from "js-cookie";
import Router from "next/router";

const UserPage: NextPage = (props) => {
	// TODO: Hardcoded.
	const listTitle = "test_list";

	// List
	const [isLoading, setLoading] = useState(false);
	const [listContent, setListContent] = useState<TodoListType>();

	// Element
	const [elementTitle, setElementTitle] = useState("");
	const [elementDescription, setElementDescription] = useState("");
	const [elementStatus, setElementStatus] = useState(false);

	// Load list
	useEffect(() => {
		setLoading(true);
		fetch(`${REST_API_IP}/api/list/user/get?listTitle=${listTitle}`, {
			headers: {
				Authorization: `Bearer ${Cookies.get("user_token")}`,
			},
		})
			.then(function (res) {
				if (!res.ok) {
					Router.push("/");
				}

				return res.json();
			})
			.then((data) => {
				setListContent(data);
				setLoading(false);
			});
	}, []);

	// Return empty page when loading
	if (!listContent) {
		return (
			<div className="w-screen h-screen justify-center items-center flex bg-gradient-to-br from-emerald-400 via-green-400 to-lime-300"></div>
		);
	}

	// Fetch elements from listContent
	const elements: [TodoElementType] = listContent.todoElements;

	// Sort them
	if(elements) elements.sort((a, b) => (a.id < b.id ? -1 : 1));

	return (
		<div className="w-screen h-screen justify-center items-center flex bg-gradient-to-br from-emerald-400 via-green-400 to-lime-300 overflow-x-hidden">
			<div className="w-4/5 h-screen m-auto p-10 justify-center items-center">
				<div className="flex gap-4 flex-col p-10 justify-center items-center bg-white backdrop-filter backdrop-blur-lg bg-opacity-50 rounded-lg shadow-xl">
					<div className="">
						<h1 className="text-2xl font-bold uppercase">
							{listContent.title}
						</h1>
						<h2>{listContent.description}</h2>
					</div>

					{/* List elements */}
					{elements !== undefined  && elements.length > 0 ? (
						elements.map((e) => {
							return (
								<TodoElement
									key={e.id}
									elementId={e.id}
									elementTitle={e.title}
									elementDescription={e.description}
									elementStatus={e.status}
								/>
							);
						})
					) : (
						<div className="text-md italic">
							This list has no elements :c
						</div>
					)}
				</div>
			</div>
		</div>
	);
};

export default UserPage;
