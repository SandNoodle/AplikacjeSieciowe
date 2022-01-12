import React from 'react';
import Link from 'next/link';

export const Navbar = () =>
	<div className="mx-auto justify-end bg-white backdrop-filter backdrop-blur-lg bg-opacity-30">
		<div className="flex justify-between">
			<h1>TODO LOGO</h1>
			<div className="flex">
				<Link href="/login">
					<button className="transition duration-200 px-5 py-2 cursor-pointer font-bold text-sm rounded-lg text-neutral-600 hover:text-neutral-900 focus:outline-none focus:ring-2 focus:ring-gray-400 focus:ring-opacity-50 ring-inset">
						Login
					</button>
				</Link>
				<Link href="/register">

					<button className="transition duration-200 px-5 py-2 cursor-pointer font-bold text-sm rounded-lg text-neutral-600 hover:text-neutral-900 focus:outline-none focus:ring-2 focus:ring-gray-400 focus:ring-opacity-50 ring-inset">
						Register
					</button>
				</Link>
			</div>
		</div>
	</div>
