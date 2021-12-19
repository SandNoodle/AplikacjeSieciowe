import { RegisterForm } from "../components/account/register_form";

function Register() {
	return (
		<div className="flex justify-center items-center w-screen h-screen bg-gradient-to-br from-indigo-500 via-purple-500 to-pink-700">
			<RegisterForm
				registerHeader="Register an account"
				username="Username"
				password="Password"
				confirmPassword="Confirm password"
				registerButton="Register"
				accountExists="Already have an account?"
				login="Log in!"
			/>
		</div>
	);
}

export default Register;
