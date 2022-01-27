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

	// Filtering
	const [filterAll, setFilterAll] = useState(true);
	const [filterOn, setFilterOn] = useState(false);
	const [filterOff, setFilterOff] = useState(false);

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
	if (elements) elements.sort((a, b) => (a.id < b.id ? -1 : 1));

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

									{/* Filter */}
									<div className="">
						<h4 className="">Filter:</h4>
						<div className="">
							<div className="form-check form-check-inline">
								<input
									className="form-check-input form-check-input appearance-none rounded-full h-4 w-4 border border-black bg-white checked:bg-black checked:border-black focus:outline-none transition duration-200 mt-1 align-top bg-no-repeat bg-center bg-contain float-left mr-2 cursor-pointer"
									type="radio"
									name="filter"
									id="radio0"
									checked={filterAll}
									onChange={(e) => {
										setFilterAll(true);
										setFilterOn(false);
										setFilterOff(false);
									}}
								/>
								<label
									className="form-check-label inline-block text-gray-800"
									htmlFor="filter0"
								>
									All
								</label>
							</div>

							<div className="form-check form-check-inline">
								<input
									className="form-check-input form-check-input appearance-none rounded-full h-4 w-4 border border-black bg-white checked:bg-black checked:border-black focus:outline-none transition duration-200 mt-1 align-top bg-no-repeat bg-center bg-contain float-left mr-2 cursor-pointer"
									type="radio"
									id="radio1"
									name="filter"
									checked={filterOn}
									onChange={(e) => {
										setFilterAll(false);
										setFilterOn(true);
										setFilterOff(false);
									}}
								/>
								<label
									className="form-check-label inline-block text-gray-800"
									htmlFor="filter1"
								>
									Enabled
								</label>
							</div>

							<div className="form-check form-check-inline">
								<input
									className="form-check-input form-check-input appearance-none rounded-full h-4 w-4 border border-black bg-white checked:bg-black checked:border-black focus:outline-none transition duration-200 mt-1 align-top bg-no-repeat bg-center bg-contain float-left mr-2 cursor-pointer"
									type="radio"
									id="radio2"
									name="filter"
									checked={filterOff}
									onChange={(e) => {
										setFilterAll(false);
										setFilterOn(false);
										setFilterOff(true);
									}}
								/>
								<label
									className="form-check-label inline-block text-gray-800"
									htmlFor="filter2"
								>
									Disabled
								</label>
							</div>
						</div>
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
				</div>
			</div>
		</div>
	);
};

export default UserPage;
