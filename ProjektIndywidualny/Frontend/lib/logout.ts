import Cookies from "js-cookie";

export	const logoutFunction = () => {
		Cookies.remove('user_token');
	};
