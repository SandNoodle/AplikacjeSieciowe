import { Router } from 'next/router';
import { NextRequest, NextResponse } from 'next/server'

export function middleware(req: NextRequest, res: NextResponse) {
	if(res.status == 403) {
		return NextResponse.redirect('/login')
	}
}