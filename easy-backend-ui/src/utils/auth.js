export function getCurrentAuthority(){
	let authorities = JSON.parse(sessionStorage.getItem("authorities"));
	return authorities;
}

export function check(authority){
	const current = getCurrentAuthority();
	return current && current.some(item => authority.includes(item.authority));
}


export function isLogin(){
	const current = getCurrentAuthority();
	return current;
}

export function getToken(){
	let token = sessionStorage.getItem("token");
    return token;

}