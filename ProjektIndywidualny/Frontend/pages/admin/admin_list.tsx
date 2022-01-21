import type { GetServerSideProps, NextPage } from "next";
import { TodoElement } from "components/list/admin_todoelement";
import { useState, useEffect } from "react";

import { REST_API_IP } from "lib/server_requests";
import { TodoListType, TodoElementType } from "types/list_types";
import Cookies from "js-cookie";
import Router from "next/router";

const AdminPage: NextPage = (props) => {
	// TODO: Hardcoded.
	const listTitle = "test_list";

	// List
	const [isLoading, setLoading] = useState(false);
	const [listContent, setListContent] = useState<TodoListType>();
	const [elementContent, setElementContent] = useState([]);

	// Element
	const [elementTitle, setElementTitle] = useState("");
	const [elementDescription, setElementDescription] = useState("");
	const [elementStatus, setElementStatus] = useState(false);

	const createElement = async () => {
		fetch(`${REST_API_IP}/api/list/admin/element/add/`, {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
				Authorization: `Bearer ${Cookies.get("user_token")}`,
			},
			body: JSON.stringify({
				title: elementTitle,
				description: elementDescription,
				status: elementStatus ? true : false,
			}),
		})
			.then((res) => res.json())
			.finally(() => {
				// Refresh page
				window.location.reload();
			})
			.catch(() => {});
	};

	// Load list
	useEffect(() => {
		setLoading(true);
		fetch(`${REST_API_IP}/api/list/admin/get?listTitle=${listTitle}`, {
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

	// Fetch elements
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
					{elements !== undefined && elements.length > 0 ? (
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

					{/* Add new element */}
					<div className="w-full h-full flex align-middle items-center justify-center">
						<div className="w-1/2 h-1/2 flex gap-5 flex-col p-10 bg-white backdrop-filter backdrop-blur-lg bg-opacity-20 rounded-lg shadow-xl">
							<h1 className="font-bold uppercase">
								Add new element
							</h1>
							<form
								onSubmit={createElement}
								className="flex flex-col gap-2"
							>
								{/* Title */}
								<input
									className="w-full rounded-md shadow-sm p-1 text-gray-900 border-gray-400 focus:border-gray-600 border-2 outline-none transition duration-200 focus:shadow-md"
									id="title"
									placeholder="Title"
									value={elementTitle}
									onChange={(e) =>
										setElementTitle(e.target.value)
									}
								></input>

								{/* Desc */}
								<input
									className="w-full rounded-md shadow-sm p-1 text-gray-900 border-gray-400 focus:border-gray-600 border-2 outline-none transition duration-200 focus:shadow-md"
									id="description"
									placeholder="Description"
									value={elementDescription}
									onChange={(e) =>
										setElementDescription(e.target.value)
									}
								></input>

								{/* Status - checkbox */}

								<div className="form-check align-middle flex items-center">
									<input
										className="form-check-input appearance-none h-8	 w-8 border border-gray-300 rounded-md bg-white checked:bg-blue-600 checked:border-blue-600 focus:outline-none transition duration-200 mt-1 align-top bg-no-repeat bg-center bg-contain float-left mr-2 cursor-pointer"
										type="checkbox"
										id="status"
										checked={elementStatus}
										onChange={(e) =>
											setElementStatus(e.target.checked)
										}
									/>
									<label className="form-check-label inline-block text-gray-800">
										Status
									</label>
								</div>
								{/* Submit */}
								<button
									type="submit"
									id="login"
									className="bg-sky-500 shadow hover:bg-sky-600 text-blue-100 hover:text-blue-50 uppercase font-bold p-2 rounded-md w-full transition duration-200 hover:shadow-md"
								>
									Add
								</button>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	);
};

export default AdminPage;
