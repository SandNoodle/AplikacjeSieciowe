import { REST_API_IP } from "lib/server_requests";
import Cookies from "js-cookie";
import Router from "next/router";

type TodoListType = {
	key: number;
	elementId: number;
	elementTitle: string;
	elementDescription: string;
	elementStatus: boolean;
};

export const TodoElement = (props: TodoListType) => {
	const deleteFunction = async () => {
		fetch(
			`${REST_API_IP}/api/list/admin/element/remove/${props.elementId}`,
			{
				method: "DELETE",
				headers: {
					Authorization: `Bearer ${Cookies.get("user_token")}`,
				},
			}
		);
		Router.reload();
	};

	return (
		<div className="flex justify-center items-center flex-row gap-4 min-w-full h-auto mx-auto p-4 rounded-md bg-white backdrop-filter backdrop-blur-lg bg-opacity-5 hover:bg-opacity-50 transition duration-200 shadow-md hover:shadow-lg">
			<div className="flex flex-row gap-5 mr-auto items-center">
				<div
					className={`p-3 rounded-full shadow-md border-4 border-black ${
						props.elementStatus ? "bg-black" : "bg-white"
					}`}
				></div>

				<div className="font-bold text-black">
					ID: {props.elementId}
				</div>

				<div className="flex flex-col">
					<h1 className="font-bold uppercase">
						{props.elementTitle}
					</h1>
					<h4 className="font-thin">{props.elementDescription}</h4>
				</div>
			</div>

			<div className="flex gap-4 pr-4">
				<button
					onClick={() => deleteFunction()}
					className="font-medium bg-transparent rounded-md px-2 py-1 hover:shadow-md transition duration-200 hover:bg-red-500 hover:text-neutral-50"
				>
					Delete
				</button>
			</div>
		</div>
	);
};
