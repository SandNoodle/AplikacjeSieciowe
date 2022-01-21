import { NextRequest, NextResponse } from 'next/server'
import Cookies from "js-cookie";

export function middleware(req: NextRequest) {
	// Add cookie to request
	req.headers.set('user_token', `Bearer ${Cookies.get('user_token')}`);

	const res = NextResponse.next();

	if(res.status == 403) {
		return NextResponse.redirect('/login');
	}
}