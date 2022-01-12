import type { NextPage } from 'next'
import { Navbar } from "../components/common/navbar";

const Home: NextPage = () => {
	return (
		<div className="h-screen w-screen bg-gradient-to-br from-teal-400 via-sky-400 to-blue-500">
			<Navbar />
		</div>
	)
}

export default Home
