import {getAccessToken} from './auth.js';
import {getRandomColor} from "./util.js";

export async function loadMainPageData() {
    try {
        const response = await fetch('http://localhost:8080/api/users', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${getAccessToken()}`
            }
        });

        if (!response.ok) {
            throw new Error('Failed to fetch main page data');
        }

        const result = await response.json();

        if (result.status === 200) {
            return result.data;  // 여기서 데이터를 반환합니다.
        } else {
            throw new Error(result.message);
        }
    } catch (error) {
        console.error('Error loading main page data:', error);
        throw error;  // 에러를 다시 던져서 호출한 곳에서 처리할 수 있게 합니다.
    }
}

export function renderBoards(boards) {
    const boardList = document.getElementById('boardList');
    boardList.innerHTML = '';

    const startElement = createBoardElement('+ New board', redirectToCreateBoard);
    boardList.appendChild(startElement);

    boards.forEach(board => {
        const boardElement = createBoardElement(board.title, () => openBoard(board.id));
        boardElement.style.backgroundColor = getRandomColor();
        boardList.appendChild(boardElement);
    });
}

function createBoardElement(text, onClick) {
    const element = document.createElement('div');
    element.className = 'board';
    element.textContent = text;
    element.onclick = onClick;
    return element;
}

export function openBoard(boardId) {
    console.log(`Opening board with id: ${boardId}`);
    // 추후 구현
}

export function redirectToCreateBoard() {
    window.location.href = '/create_board';
}

export async function createBoard(title, content) {
    try {
        const response = await fetch('http://localhost:8080/api/boards', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${getAccessToken()}`
            },
            body: JSON.stringify({ title, content })
        });

        console.log(response);
        if (!response.ok) {
            throw new Error('Failed to create board');
        }

        const result = await response.json();
        console.log(result);
        if (result.status === 201) {
            return result.data;  // 여기서 데이터를 반환합니다.
        } else {
            throw new Error(result.message);
        }
    } catch (error) {
        console.error('Error loading main page data:', error);
        throw error;  // 에러를 다시 던져서 호출한 곳에서 처리할 수 있게 합니다.
    }
}