import { getAccessToken, getUserId } from './auth.js';

// 댓글을 가져오는 함수
export async function fetchComments(boardId, columnId, cardId) {
    try {
        const response = await fetch(`http://localhost:8080/api/boards/${boardId}/columns/${columnId}/cards/${cardId}/comments`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${getAccessToken()}`
            }
        });

        if (response.ok) {
            const result = await response.json();
            return result.data;
        } else {
            throw new Error('Failed to fetch comments');
        }
    } catch (error) {
        console.error('Error fetching comments:', error);
        return [];
    }
}

// 댓글을 렌더링하는 함수
export function renderComments(comments) {
    const commentContainer = document.getElementById('commentContainer');
    commentContainer.innerHTML = ''; // 기존 댓글 삭제

    comments.forEach(comment => {
        const commentElement = document.createElement('div');
        commentElement.className = 'comment';
        const createdAt = new Date(comment.createdAt).toLocaleString(); // 시간 포함
        commentElement.innerHTML = `
            <img src="https://emoji.slack-edge.com/T06B9PCLY1E/bear_twerk/d8658b5c911625a1.gif" alt="User Image">
            <div class="comment-text">
                <div class="comment-author-date">
                    <strong class="comment-author">작성자: ${comment.username ? comment.username : 'Unknown'}</strong>
                    <div class="comment-content">${comment.content}</div>
                </div>
                <span class="comment-date">${createdAt}</span>
            </div>
        `;
        commentContainer.appendChild(commentElement);
    });
}

// 댓글을 생성하는 함수
export async function createComment(boardId, columnId, cardId) {
    const newComment = document.getElementById('newComment').value;
    const userId = getUserId(); // userId를 로컬 스토리지에서 가져옴

    if (!newComment) {
        alert('댓글 내용을 입력해주세요.');
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/api/boards/${boardId}/columns/${columnId}/cards/${cardId}/comments`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${getAccessToken()}`
            },
            body: JSON.stringify({
                content: newComment,
                userId: userId, // userId 사용
                createdAt: new Date().toISOString() // 현재 시간으로 설정
            })
        });

        if (response.ok) {
            const result = await response.json();
            document.getElementById('newComment').value = ''; // 텍스트 영역 비우기
            const comments = await fetchComments(boardId, columnId, cardId);
            renderComments(comments); // 댓글 갱신
        } else {
            throw new Error('Failed to create comment');
        }
    } catch (error) {
        console.error('Error creating comment:', error);
    }
}
