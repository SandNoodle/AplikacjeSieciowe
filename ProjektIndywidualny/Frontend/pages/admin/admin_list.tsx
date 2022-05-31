import React from "react";
import type { GetServerSideProps, NextPage } from "next";
import { TodoElement } from "components/list/admin_todoelement";
import { useState, useEffect } from "react";

import { REST_API_IP } from "lib/server_requests";
import { TodoListType, TodoElementType } from "types/list_types";
import Cookies from "js-cookie";
import Router from "next/router";

const AdminPage: NextPage = (props) => {
	const [pageNumber, setPageNumber] = useState(0);
	const [pageCount, setPageCount] = useState(0);

	// List
	const [listContent, setListContent] = useState<TodoListType>();

	// Element Create
	const [elementTitle, setElementTitle] = useState("");
	const [elementDescription, setElementDescription] = useState("");
	const [elementStatus, setElementStatus] = useState(false);

	// Element change
	const [elementId, setElementId] = useState("");
	const [changeTitle, setChangeTitle] = useState("");
	const [changeDescription, setChangeDescription] = useState("");
	const [changeStatus, setChangeStatus] = useState(false);

	// Filtering
	const [filterAll, setFilterAll] = useState(true);
	const [filterOn, setFilterOn] = useState(false);
	const [filterOff, setFilterOff] = useState(false);

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

	const changeElement = async () => {
		fetch(
			`${REST_API_IP}/api/list/user/element/update/${elementId}?title=${changeTitle}&description=${changeDescription}&status=${
				changeStatus ? true : false
			}`,
			{
				method: "PUT",
				headers: {
					"Content-Type": "application/json",
					Authorization: `Bearer ${Cookies.get("user_token")}`,
				},
			}
		)
			.then((res) => res.json())
			.finally(() => {
				// Refresh page
				window.location.reload();
			})
			.catch(() => {});
	};

	// Load list
	useEffect(() => {
		// Page count
		fetch(`${REST_API_IP}/api/list/user/get_paged/count`, {
			method: "GET",
			headers: {
				Authorization: `Bearer ${Cookies.get("user_token")}`,
			},
		})
			.then((res) => {
				return res.json();
			})
			.then((data) => {
				setPageCount(data.page_count);
			})
			.catch((err) => {
				console.log(err);
			});

		// Page elements
		fetch(`${REST_API_IP}/api/list/admin/get_paged/${pageNumber}`, {
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
			});
	}, [pageNumber]);

	// Return empty page when loading
	if (!listContent) {
		return (
			<div className="w-screen h-screen justify-center items-center flex bg-gradient-to-br from-indigo-500 via-purple-500 to-pink-700"></div>
		);
	}

	// Fetch elements
	const elements: [TodoElementType] = listContent.todoElements;

	// Sort them
	if (elements) elements.sort((a, b) => (a.id < b.id ? -1 : 1));

	return (
		<div className="w-screen h-screen justify-center items-center flex bg-gradient-to-br from-indigo-500 via-purple-500 to-pink-700 overflow-x-hidden">
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
							// TODO: BAD HACK
							if ((e.status == true && filterOn) || filterAll) {
								return (
									<TodoElement
										key={e.id}
										elementId={e.id}
										elementTitle={e.title}
										elementDescription={e.description}
										elementStatus={e.status}
									/>
								);
							}
							if ((e.status == false && filterOff) || filterAll) {
								return (
									<TodoElement
										key={e.id}
										elementId={e.id}
										elementTitle={e.title}
										elementDescription={e.description}
										elementStatus={e.status}
									/>
								);
							}
						})
					) : (
						<div className="text-md italic">
							This list has no elements :c
						</div>
					)}

					{/* Page */}
					<div className="flex w-full h-full">
						<div className="w-full h-1/2 flex gap-5 flex-col p-5 bg-white backdrop-filter backdrop-blur-lg bg-opacity-20 rounded-lg shadow-xl">
							<div className="flex flex-wrap gap-5 justify-center">
								{[...Array(pageCount).keys()].map(
									(index: number) => (
										<button
											key={index}
											value={index}
											onClick={() => {
												setPageNumber(index);
											}}
										>
											<div className="w-12 h-12 p-2 bg-white jusitfy-center border-2 border-neutral-500 border-opacity-30 hover:border-opacity-80 align-middle backdrop-blur-lg bg-opacity-20 rounded-md shadow-md hover:shadow-lg hover:bg-opacity-80 transition duration-200">
												<div className="flex justify-center items-center m-auto">
													<h4 className="font-bold text-black text-center">
														{index}
													</h4>
												</div>
											</div>
										</button>
									)
								)}
							</div>
						</div>
					</div>

					<div className="flex w-full h-full gap-12">
						{/* Add new element */}
						<div className="w-full h-full flex align-middle items-center justify-center">
							<div className="w-full h-1/2 flex gap-5 flex-col p-10 bg-white backdrop-filter backdrop-blur-lg bg-opacity-20 rounded-lg shadow-xl">
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
											setElementDescription(
												e.target.value
											)
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
												setElementStatus(
													e.target.checked
												)
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

						{/* Change element */}
						<div className="w-full h-full flex align-middle items-center justify-center">
							<div className="w-full h-1/2 flex gap-5 flex-col p-10 bg-white backdrop-filter backdrop-blur-lg bg-opacity-20 rounded-lg shadow-xl">
								<h1 className="font-bold uppercase">
									Change existing element
								</h1>
								<form
									onSubmit={changeElement}
									className="flex flex-col gap-2"
								>
									{/* ID */}
									<input
										className="w-full rounded-md shadow-sm p-1 text-gray-900 border-gray-400 focus:border-gray-600 border-2 outline-none transition duration-200 focus:shadow-md"
										id="id"
										placeholder="Element ID"
										value={elementId}
										onChange={(e) =>
											setElementId(e.target.value)
										}
									></input>

									{/* Title */}
									<input
										className="w-full rounded-md shadow-sm p-1 text-gray-900 border-gray-400 focus:border-gray-600 border-2 outline-none transition duration-200 focus:shadow-md"
										id="title"
										placeholder="Title"
										value={changeTitle}
										onChange={(e) =>
											setChangeTitle(e.target.value)
										}
									></input>

									{/* Desc */}
									<input
										className="w-full rounded-md shadow-sm p-1 text-gray-900 border-gray-400 focus:border-gray-600 border-2 outline-none transition duration-200 focus:shadow-md"
										id="description"
										placeholder="Description"
										value={changeDescription}
										onChange={(e) =>
											setChangeDescription(e.target.value)
										}
									></input>

									{/* Status - checkbox */}

									<div className="form-check align-middle flex items-center">
										<input
											className="form-check-input appearance-none h-8	 w-8 border border-gray-300 rounded-md bg-white checked:bg-blue-600 checked:border-blue-600 focus:outline-none transition duration-200 mt-1 align-top bg-no-repeat bg-center bg-contain float-left mr-2 cursor-pointer"
											type="checkbox"
											id="status"
											checked={changeStatus}
											onChange={(e) =>
												setChangeStatus(
													e.target.checked
												)
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
										Change
									</button>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	);
};

export default AdminPage;
