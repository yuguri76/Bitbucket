export function login(email, password) {
    return fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(result => {
            if (result.status === 200) {
                localStorage.setItem('token', result.data);
                window.location.href = '/main';
            } else {
                throw new Error(result.message);
            }
        });
}

export function logout() {
    const token = getAccessToken();
    return fetch('http://localhost:8080/api/auth/logout', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    })
        .then(result => {
            if (result.status === 200) {
                localStorage.removeItem('token');
                window.location.href = '/login';
            } else if (result.status === 401) {
                return refreshTokenAndRetry(logout);
            }
        });
}

export function refreshTokenAndRetry(originalFunction) {

    console.log("access token 재발급");

    return fetch('/api/auth/refresh', {
        method: 'POST',
        credentials: 'include'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Token refresh failed');
            }
            return response.json();
        })
        .then(data => {
            localStorage.setItem('token', data.data);
            return originalFunction();
        });
}

export function signup(userId, userPassword, username, managerKey) {


    fetch('http://localhost:8080/api/users', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            email: userId,
            password: userPassword,
            name: username,
            secretKey: managerKey
        })
    })
        .then(response => {
            console.log(response);
            if (!response.ok) {
                throw new Error('회원가입 실패');
            }
            return response.json();
        })
        .then(data => {
            alert('회원가입이 완료되었습니다.');
            window.location.href = '/login';  // 로그인 페이지로 리다이렉트
        })
        .catch(error => {
            console.error('회원가입 오류:', error);
            alert('회원가입 중 오류가 발생했습니다: ' + error.message);
        });
}

export function getAccessToken() {
    return localStorage.getItem('token');
}

export async function checkAuth() {
    const token = getAccessToken();
    if (!token) {
        window.location.href = '/login';
    }
}

function redirectToLogin() {
    window.location.href = '/login';
}