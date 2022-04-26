import { useState, useEffect } from "react";
import Link from "next/link";

import { REST_API_IP } from "lib/server_requests";

function Register() {

	const [ username, setUsername ] = useState("");
	const [ password, setPassword ] = useState("");

	const registerUser = async (e) => {
		e.preventDefault();
		fetch(`${REST_API_IP}/api/user/create/`, {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify({
				username: username,
				password: password
			}),
		})
			.then((res) => res.json())
			.finally(() => {
				// Refresh page
				window.location.reload();
			})
			.catch(() => {});
	};

	return (
		<div className="flex justify-center items-center w-screen h-screen bg-gradient-to-br from-indigo-500 via-purple-500 to-pink-700">
			<div className="flex flex-col justify-center items-center">
				<div className="p-5 xs:p-0 mx-auto md:w-full md:max-w-fit">
					<div className="px-10 py-5 bg-white backdrop-filter backdrop-blur-lg bg-opacity-60 rounded-lg flex flex-col justify-center items-center shadow-xl divide-y divide-gray-400">
						<div className="py-5">
							{/* Register section */}
							<form 
							onSubmit={registerUser}
							className="">
								<p className="mb-5 text-3xl font-bold uppercase text-slate-600">
									Register an account
								</p>
								<input
									type="text"
									name="username"
									autoComplete="off"
									placeholder="Username"
									value={username}
									onChange={(e) => setUsername(e.target.value)}
									required
									className="mb-5 p-3 w-80 rounded-md text-gray-900 border-gray-400 focus:border-gray-600 border-2 outline-none transition duration-200 focus:shadow-md"
								/>
								<input
									type="password"
									name="password"
									autoComplete="off"
									placeholder="Password"
									value={password}
									onChange={(e) => setPassword(e.target.value)}
									required
									className="mb-5 p-3 w-80 rounded-md text-gray-900 border-gray-400 focus:border-gray-600 border-2 outline-none transition duration-200 focus:shadow-md"
								/>
								<button
									type="submit"
									id="login"
									className="bg-rose-500 shadow hover:bg-rose-600 text-red-100 hover:text-red-50 uppercase font-bold p-2 rounded-md w-80 transition duration-200 hover:shadow-md"
								>
									Register
								</button>
							</form>
						</div>
						<div className="py-5">
							{/* Additional actions section */}
							<div className="justify-center items-center flex">
								<div className="text-center sm:text-left whitespace-nowrap">
									<p className="text-sm font-normal text-neutral-600">
										You already have an account?
									</p>
								</div>
								<div className="text-center sm:text-left whitespace-nowrap">
									<Link href="/login">
										<button className="transition duration-200 mx-5 px-5 py-4 cursor-pointer font-bold text-sm rounded-lg text-neutral-500 hover:text-neutral-700 focus:outline-none focus:ring-2 focus:ring-gray-400 focus:ring-opacity-50 ring-inset">
											<span className="inline-block ml-1">
												Login
											</span>
										</button>
									</Link>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	);
}

export default Register;
