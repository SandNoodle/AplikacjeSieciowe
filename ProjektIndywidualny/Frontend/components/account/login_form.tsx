import React from 'react';

type LoginFormLocalizationData = {
	loginHeaderString: string;
	username: string;
	password: string;
	loginString: string;
	registerString: string;
	forgotPasswordString: string;
}

export const LoginForm = (props: LoginFormLocalizationData) =>
	<div className="flex flex-col justify-center items-center">
		<div className="p-5 xs:p-0 mx-auto md:w-full md:max-w-fit">
			<div className="px-10 py-5 bg-white backdrop-filter backdrop-blur-lg bg-opacity-60 rounded-md flex flex-col justify-center items-center shadow-xl divide-y divide-gray-400">
				<div className="py-5">
					{/* Login section */}
					<form className="">
						<p className="mb-5 text-3xl font-bold uppercase text-slate-600">
							{props.loginHeaderString}
						</p>
						<input type="text" name="username" autoComplete="off" placeholder={props.username} required className="mb-5 p-3 w-80 rounded-md text-gray-900 border-gray-400 focus:border-gray-600 border-2 outline-none transition duration-200 focus:shadow-md" />
						<input type="password" name="password" autoComplete="off" placeholder={props.password} required className="mb-5 p-3 w-80 rounded-md text-gray-900 border-gray-400 focus:border-gray-600 border-2 outline-none transition duration-200 focus:shadow-md" />
						<button type="submit" id="login" className="bg-sky-500 shadow hover:bg-sky-600 text-blue-100 hover:text-blue-50 uppercase font-bold p-2 rounded-md w-80 transition duration-200 hover:shadow-md">
							{props.loginString}
						</button>
					</form>
				</div>
				<div className="py-5">
					{/* Additional actions section */}
					<div className="flex flex-col">
						<div className="text-center whitespace-nowrap">
							<button className="transition duration-200 px-5 py-2 cursor-pointer font-bold text-sm rounded-lg text-neutral-500 hover:text-neutral-700 focus:outline-none focus:ring-2 focus:ring-gray-400 focus:ring-opacity-50 ring-inset">
								<span className="inline-block ml-1">
									{props.forgotPasswordString}
								</span>
							</button>
						</div>
						<div className="text-center whitespace-nowrap">
							<button className="transition duration-200 px-5 py-2 cursor-pointer font-bold text-sm rounded-lg text-neutral-500 hover:text-neutral-700 focus:outline-none focus:ring-2 focus:ring-gray-400 focus:ring-opacity-50 ring-inset">
								<span className="inline-block ml-1">
									{props.registerString}
								</span>
							</button>
						</div>
					</div>
				</div>
			</div>
		</div >
	</div >
