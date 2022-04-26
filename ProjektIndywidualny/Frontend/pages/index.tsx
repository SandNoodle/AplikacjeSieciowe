import type { NextPage } from "next";
import Link from "next/link";
import { logoutFunction } from "lib/logout";

const Home: NextPage = () => {

	return (
		<div className="h-screen w-screen overflow-hidden bg-gradient-to-br from-teal-400 via-sky-400 to-blue-500">
			<div className="flex h-screen justify-center items-center">
				<div className="m-auto flex justify-center">
					<div className="flex gap-4 flex-col p-10 justify-center items-center bg-white backdrop-filter backdrop-blur-lg bg-opacity-50 rounded-lg shadow-xl">
						<h1 className="text-5xl font-bold">
							Welcome to Todo-Lister!
						</h1>
						<h4 className="text-2xl font-semibold">
							Best todo-list app ever made.
						</h4>
						<p className="text-sm font-medium italic">Probably.</p>

						<div className="py-5 flex flex-col gap-2">
							<Link href="/register">
								<button className="uppercase transition duration-200 px-20 py-4 shadow-md hover:shadow-lg cursor-pointer font-bold text-xl rounded-lg bg-sky-500 hover:bg-sky-600 text-blue-100 hover:text-blue-50 focus:outline-none focus:ring-2 focus:ring-gray-400 focus:ring-opacity-50 ring-inset">
									Register
								</button>
							</Link>
							<Link href="/login">
								<button className="uppercase transition duration-200 px-20 py-4 shadow-md hover:shadow-lg cursor-pointer font-bold text-xl rounded-lg bg-sky-500 hover:bg-sky-600 text-blue-100 hover:text-blue-50 focus:outline-none focus:ring-2 focus:ring-gray-400 focus:ring-opacity-50 ring-inset">
									Login
								</button>
							</Link>

							<Link href="/admin/admin_list">
								<button className="uppercase transition duration-200 px-20 py-4 shadow-md hover:shadow-lg cursor-pointer font-bold text-xl rounded-lg bg-sky-500 hover:bg-sky-600 text-blue-100 hover:text-blue-50 focus:outline-none focus:ring-2 focus:ring-gray-400 focus:ring-opacity-50 ring-inset">
									Admin panel
								</button>
							</Link>

							<Link href="/user/user_list">
								<button className="uppercase transition duration-200 px-20 py-4 shadow-md hover:shadow-lg cursor-pointer font-bold text-xl rounded-lg bg-sky-500 hover:bg-sky-600 text-blue-100 hover:text-blue-50 focus:outline-none focus:ring-2 focus:ring-gray-400 focus:ring-opacity-50 ring-inset">
									User panel
								</button>
							</Link>

							<button 
							onClick={logoutFunction}
							className="uppercase transition duration-200 px-20 py-4 shadow-md hover:shadow-lg cursor-pointer font-bold text-xl rounded-lg bg-sky-500 hover:bg-sky-600 text-blue-100 hover:text-blue-50 focus:outline-none focus:ring-2 focus:ring-gray-400 focus:ring-opacity-50 ring-inset">
								Logout
							</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	);
};

export default Home;
