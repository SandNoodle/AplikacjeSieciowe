import { useState } from "react";
import Link from "next/link";
import Cookies from "js-cookie";
import { CAPTCHA_SITE_KEY, REST_API_IP } from "lib/server_requests";
import Router from "next/router";
import ReCAPTCHA from "react-google-recaptcha";

function Login() {
	const [username, setUsername] = useState("");
	const [password, setPassword] = useState("");
	const [isVerified, setVerified] = useState(false);

	const onVerify = () => {
		setVerified(true);
		console.log(isVerified);
	};

	const loginFunction = async (e) => {
		e.preventDefault();
		if (!isVerified) {
			window.alert("Proszę zweryfikować CAPTCHA.");
			return;
		}

		fetch(
			`${REST_API_IP}/api/login?username=${username}&password=${password}`
		)
			.then((res) => res.json())
			.then((data) => {
				Cookies.set("user_token", data.access_token);
			})
			.finally(() => {
				Router.push("/");
			});
	};

	return (
		<div className="flex justify-center items-center w-screen h-screen bg-gradient-to-tl from-sky-700 via-indigo-500 to-purple-500">
			<div className="flex flex-col justify-center items-center">
				<div className="p-5 xs:p-0 mx-auto md:w-full md:max-w-fit">
					<div className="px-10 py-5 bg-white backdrop-filter backdrop-blur-lg bg-opacity-60 rounded-md flex flex-col justify-center items-center shadow-xl divide-y divide-gray-400">
						<div className="py-5">
							{/* Login section */}
							<form onSubmit={loginFunction} className="">
								<p className="mb-5 text-3xl font-bold uppercase text-slate-600">
									Login
								</p>
								<input
									type="text"
									name="username"
									value={username}
									onChange={(e) =>
										setUsername(e.target.value)
									}
									autoComplete="off"
									placeholder="Username"
									required
									className="mb-5 p-3 w-80 rounded-md text-gray-900 border-gray-400 focus:border-gray-600 border-2 outline-none transition duration-200 focus:shadow-md"
								/>
								<input
									type="password"
									name="password"
									value={password}
									onChange={(e) =>
										setPassword(e.target.value)
									}
									autoComplete="off"
									placeholder="Password"
									required
									className="mb-5 p-3 w-80 rounded-md text-gray-900 border-gray-400 focus:border-gray-600 border-2 outline-none transition duration-200 focus:shadow-md"
								/>
								<button
									type="submit"
									id="login"
									className="mb-5 p-3 bg-sky-500 shadow hover:bg-sky-600 text-blue-100 hover:text-blue-50 uppercase font-bold rounded-md w-80 transition duration-200 hover:shadow-md"
								>
									Log in
								</button>

								<div className="flex justify-center">
									<ReCAPTCHA
										className=""
										required
										sitekey={CAPTCHA_SITE_KEY}
										onChange={onVerify}
									/>
								</div>
							</form>
						</div>
						<div className="py-5">
							{/* Additional actions section */}
							<div className="justify-center items-center flex">
								<div className="text-center sm:text-left whitespace-nowrap">
									<p className="text-sm font-normal text-neutral-600">
										You don't have an account?
									</p>
								</div>
								<div className="text-center sm:text-left whitespace-nowrap">
									<Link href="/register">
										<button className="transition duration-200 mx-5 px-5 py-4 cursor-pointer font-bold text-sm rounded-lg text-neutral-500 hover:text-neutral-700 focus:outline-none focus:ring-2 focus:ring-gray-400 focus:ring-opacity-50 ring-inset">
											<span className="inline-block ml-1">
												Register!
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

export default Login;
