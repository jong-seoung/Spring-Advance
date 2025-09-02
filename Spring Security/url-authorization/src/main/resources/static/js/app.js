// API 테스트 함수들
function testPublicAPI() {
    fetch('/api/posts')
        .then(response => response.json())
        .then(data => {
            document.getElementById('api-result').innerHTML =
                `<strong>공개 API 호출 성공:</strong><pre>${JSON.stringify(data, null, 2)}</pre>`;
        })
        .catch(error => {
            document.getElementById('api-result').innerHTML =
                `<strong>오류:</strong> ${error.message}`;
        });
}

function testUserAPI() {
    fetch('/api/posts', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            title: '새 게시글',
            content: '사용자가 작성한 게시글입니다.'
        })
    })
    .then(response => response.json())
    .then(data => {
        document.getElementById('api-result').innerHTML =
            `<strong>사용자 API 호출 성공:</strong><pre>${JSON.stringify(data, null, 2)}</pre>`;
    })
    .catch(error => {
        document.getElementById('api-result').innerHTML =
            `<strong>오류:</strong> ${error.message}`;
    });
}

function testAdminAPI() {
    fetch('/api/admin/stats')
        .then(response => response.json())
        .then(data => {
            document.getElementById('api-result').innerHTML =
                `<strong>관리자 API 호출 성공:</strong><pre>${JSON.stringify(data, null, 2)}</pre>`;
        })
        .catch(error => {
            document.getElementById('api-result').innerHTML =
                `<strong>오류:</strong> ${error.message}`;
        });
}

// 사용자 대시보드용 함수들
function createPost() {
    fetch('/api/posts', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            title: '테스트 게시글',
            content: '사용자가 작성한 게시글입니다.'
        })
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error(`HTTP ${response.status}: ${response.statusText}`);
        }
    })
    .then(data => {
        document.getElementById('result').innerHTML =
            `<div class="alert success"><strong>성공:</strong><pre>${JSON.stringify(data, null, 2)}</pre></div>`;
    })
    .catch(error => {
        document.getElementById('result').innerHTML =
            `<div class="alert error"><strong>실패:</strong> ${error.message}</div>`;
    });
}

function updatePost() {
    fetch('/api/posts/1', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            title: '수정된 게시글',
            content: '사용자가 수정한 게시글입니다.'
        })
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error(`HTTP ${response.status}: ${response.statusText}`);
        }
    })
    .then(data => {
        document.getElementById('result').innerHTML =
            `<div class="alert success"><strong>성공:</strong><pre>${JSON.stringify(data, null, 2)}</pre></div>`;
    })
    .catch(error => {
        document.getElementById('result').innerHTML =
            `<div class="alert error"><strong>실패:</strong> ${error.message}</div>`;
    });
}

function deletePost() {
    const resultElement = document.getElementById('result') || document.getElementById('admin-result');

    fetch('/api/posts/1', {
        method: 'DELETE'
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error(`HTTP ${response.status}: 권한이 없습니다 (ADMIN 권한 필요)`);
        }
    })
    .then(data => {
        resultElement.innerHTML =
            `<div class="alert success"><strong>성공:</strong><pre>${JSON.stringify(data, null, 2)}</pre></div>`;
    })
    .catch(error => {
        resultElement.innerHTML =
            `<div class="alert error"><strong>실패:</strong> ${error.message}</div>`;
    });
}

// 관리자 대시보드용 함수들
function getAdminStats() {
    fetch('/api/admin/stats')
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error(`HTTP ${response.status}: ${response.statusText}`);
            }
        })
        .then(data => {
            document.getElementById('admin-result').innerHTML =
                `<div class="alert success"><strong>관리자 통계 조회 성공:</strong><pre>${JSON.stringify(data, null, 2)}</pre></div>`;
        })
        .catch(error => {
            document.getElementById('admin-result').innerHTML =
                `<div class="alert error"><strong>실패:</strong> ${error.message}</div>`;
        });
}