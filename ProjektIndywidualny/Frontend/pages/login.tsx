import { LoginForm } from "../components/account/login_form";

function Login() {
	return (
		<div className="flex justify-center items-center w-screen h-screen bg-gradient-to-tl from-sky-700 via-indigo-500 to-purple-500">
			<LoginForm
				loginHeaderString="Login"
				username="Username"
				password="Password"
				loginString="Login"
				forgotPasswordString="Forgot password?"
				registerString="Register an account."
			/>
		</div>
	);
}

export default Login;
