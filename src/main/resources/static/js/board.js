import {getAccessToken, refreshTokenAndRetry} from './auth.js';
import {getRandomColor} from "./util.js";

export async function loadMainPageData() {
    try {
        const response = await fetch('http://localhost:8080/api/users', {
            method: 'GET', headers: {
                'Content-Type': 'application/json', 'Authorization': `Bearer ${getAccessToken()}`
            }
        });

        const result = await response.json();

        if (result.status === 200) {
            return result.data;  // 여기서 데이터를 반환합니다.
        } else if (result.status === 401) {
            console.log("초기화");
            return refreshTokenAndRetry(loadMainPageData);
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
    window.location.href = `/board?id=${boardId}`;
}

export function redirectToCreateBoard() {
    window.location.href = '/create_board';
}

export async function createBoard(title, content) {
    try {
        const response = await fetch('http://localhost:8080/api/boards', {
            method: 'POST', headers: {
                'Content-Type': 'application/json', 'Authorization': `Bearer ${getAccessToken()}`
            }, body: JSON.stringify({title, content})
        });

        console.log(response);

        const result = await response.json();
        console.log(result);
        if (result.status === 201) {
            return result.data;  // 여기서 데이터를 반환합니다.
        } else if (result.status === 401) {
            console.log("초기화");
            return refreshTokenAndRetry(createBoard);
        } else {
            throw new Error(result.message);
        }
    } catch (error) {
        console.error('Error loading main page data:', error);
        throw error;  // 에러를 다시 던져서 호출한 곳에서 처리할 수 있게 합니다.
    }
}

// 보드 단건 조회 API 호출하는 함수
export async function loadBoardData(boardId) {
    try {
        const response = await fetch(`http://localhost:8080/api/boards/${boardId}`, {
            method: 'GET', headers: {
                'Content-Type': 'application/json', 'Authorization': `Bearer ${getAccessToken()}`
            }
        });

        const result = await response.json();

        if (result.status === 200) {
            return result.data;  // 여기서 데이터를 반환합니다.
        } else if (result.status === 401) {
            console.log("초기화");
            return refreshTokenAndRetry(loadBoardData);
        } else {
            throw new Error(result.message);
        }
    } catch (error) {
        console.error('Error loading board page data:', error);
        throw error;  // 에러를 다시 던져서 호출한 곳에서 처리할 수 있게 합니다.
    }
}

// 보드 단건 조회 시, 보드 멤버 리스트의 객체에서 사용자 이름만 문자열 형식으로 붙이는 함수
export function renderBoardMemberList(memberList) {
    let result = "";

    memberList.forEach(member => {
        result += " [ " + member.userName + " ] ";
    });

    return result;
}

// 보드 단건 조회로 가져온 데이터들을 HTML 로 그려주는 함수
export async function updateBoardData(title, content, memberList) {
    const boardTitleElement = document.getElementById('boardTitle');
    const boardContentElement = document.getElementById('boardContent');
    const boardMemberElement = document.getElementById('boardMember');
    if (boardTitleElement) {
        boardTitleElement.textContent = `${title}`;
    }
    if (boardContentElement) {
        boardContentElement.textContent = `${content}`;
    }
    if (boardMemberElement) {
        boardMemberElement.textContent = `${memberList}`;
    }
}

// 보드 수정 API 호출하는 함수
export async function editBoard(boardId, title, content) {

    try {
        const response = await fetch(`http://localhost:8080/api/boards/${boardId}`, {
            method: 'PUT', headers: {
                'Content-Type': 'application/json', 'Authorization': `Bearer ${getAccessToken()}`
            }, body: JSON.stringify({
                title: title, content: content
            })
        });

        const result = await response;

        if (result.status === 200) {
            alert('보드 수정이 완료되었습니다.');
            window.location.href = '/board';  // 보드 페이지로 리다이렉트
        } else if (result.status === 401) {
            console.log("초기화");
            return refreshTokenAndRetry(editBoard);
        } else {
            throw new Error(result.message);
        }
    } catch (error) {
        console.error('Error loading board page data:', error);
        alert('보드 수정 중 오류가 발생했습니다: ' + error.message);
        throw error;  // 에러를 다시 던져서 호출한 곳에서 처리할 수 있게 합니다.
    }
}

// 보드 초대 API 호출하는 함수
export async function inviteBoard(boardId, email) {
    try {
        const response = await fetch(`http://localhost:8080/api/boards/${boardId}/invite`, {
            method: 'POST', headers: {
                'Authorization': `Bearer ${getAccessToken()}`, 'Content-Type': 'application/json'
            }, body: JSON.stringify({
                email: email
            })
        });

        const result = await response.json();

        if (result.status === 200) {
            let data = result.data;
            alert(`${data.boardTitle} 보드에 사용자${data.userName} 이 초대되었습니다.`);
            window.location.href = '/board';
        } else if (result.status === 401) {
            console.log("초기화");
            return refreshTokenAndRetry(inviteBoard);
        } else {
            throw new Error(result.message);
        }
    } catch (error) {
        console.error('보드 초대 오류:', error);
        alert('보드 초대 중 오류가 발생했습니다: ' + error.message);
    }
}

// 보드 삭제 API 호출하는 함수
export async function deleteBoard(boardId) {

    try {
        const response = await fetch(`http://localhost:8080/api/boards/${boardId}`, {
            method: 'DELETE', headers: {
                'Authorization': `Bearer ${getAccessToken()}`
            }
        });

        const result = await response.json();

        if (result.status === 200) {
            alert('보드 삭제가 완료되었습니다.');
            window.location.href = '/main';
        } else if (result.status === 401) {
            console.log("초기화");
            return refreshTokenAndRetry(deleteBoard);
        } else {
            throw new Error(result.message);
        }
    } catch (error) {
        console.error('보드 삭제 오류:', error);
        alert('보드 삭제 중 오류가 발생했습니다: ' + error.message);
    }
}

// 컬럼 데이터를 로드하는 함수
export async function loadColumns(boardId) {
    try {
        const response = await fetch(`http://localhost:8080/api/boards/${boardId}/columns`, {
            method: 'GET', headers: {
                'Authorization': `Bearer ${getAccessToken()}`
            }
        });

        const result = await response.json();

        if (result.status === 200) {
            return result.data; // 컬럼 데이터를 반환합니다.
        } else if (result.status === 401) {
            console.log("초기화");
            return refreshTokenAndRetry(loadColumns);
        } else {
            throw new Error(result.message);
        }
    } catch (error) {
        console.error('Error loading columns:', error);
        throw error;
    }
}

// 컬럼을 생성하는 함수
export async function createColumn(title, boardId) {
    try {
        const response = await fetch('http://localhost:8080/api/boards/columns', {
            method: 'POST', headers: {
                'Content-Type': 'application/json', 'Authorization': `Bearer ${getAccessToken()}`
            }, body: JSON.stringify({title, boardId, orders: 0})
        });

        const result = await response.json();
        if (result.status === 200) {
            alert('컬럼 생성에 성공하였습니다.'); // 알림창 띄우기
            closeModal('createColumnModal'); // 모달창 닫기
            window.location.href = '/board';
            return result.data; // 생성된 컬럼 데이터를 반환합니다.
        } else if (result.status === 401) {
            console.log("초기화");
            return refreshTokenAndRetry(createColumn);
        } else {
            throw new Error(result.message);
        }
    } catch (error) {
        console.error('Error creating column:', error);
        throw error;
    }
}

// UI에 컬럼 추가 함수
export function addColumnToUI(columnTitle, columnId) {
    const boardContainer = document.getElementById('boardContainer');
    const addColumnButton = boardContainer.querySelector('.add-column');
    const columnTemplate = `
        <div class="column" draggable="true" ondragstart="drag(event)" id="column${columnId}">
            <div style="display: flex; justify-content: space-between;">
                <h3>${columnTitle}</h3>
                <button class="delete-column-btn" data-column-id="${columnId}">X</button>
            </div>
            <div class="add-card" onclick="showModal('createCardModal')">+ Add a card</div>
        </div>
    `;
    if (addColumnButton) {
        addColumnButton.insertAdjacentHTML('beforebegin', columnTemplate);
    } else {
        boardContainer.insertAdjacentHTML('beforeend', columnTemplate);
    }

    // 이벤트 핸들러 추가
    document.querySelector(`.delete-column-btn[data-column-id="${columnId}"]`).addEventListener('click', function() {
        handleDeleteColumn(columnId);
    });
}

// 컬럼 삭제 API 호출하는 함수
export async function deleteColumn(columnId) {
    try {
        console.log(`Attempting to delete column with id: ${columnId}`); // 로그 추가
        const boardId = localStorage.getItem('board_id'); // boardId 가져오기
        const response = await fetch(`http://localhost:8080/api/boards/${boardId}/columns/${columnId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${getAccessToken()}`
            }
        });

        if (!response.ok) {
            // 서버 응답이 200이 아닌 경우, 오류 메시지를 로그에 출력합니다.
            const errorMessage = await response.text();
            console.error('Server response:', errorMessage);
            throw new Error('Failed to delete column');
        }

        alert('컬럼 삭제에 성공하였습니다.');
        document.getElementById(`column${columnId}`).remove(); // UI에서 컬럼 삭제
    } catch (error) {
        console.error('Error deleting column:', error);
        alert('Error deleting column: ' + error.message);
    }
}

// 삭제 버튼 핸들러
window.handleDeleteColumn = function (columnId) {
    deleteColumn(columnId);
};
