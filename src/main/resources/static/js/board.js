import {getAccessToken, refreshTokenAndRetry} from './auth.js';
import {getRandomColor} from "./util.js";
import * as cardAPI from './card.js';

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
            return refreshTokenAndRetry(() => loadMainPageData());
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

        const result = await response.json();
        if (result.status === 201) {
            return result.data;  // 여기서 데이터를 반환합니다.
        } else if (result.status === 401) {
            return refreshTokenAndRetry(() => createBoard(title, content));
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
            return refreshTokenAndRetry(() => loadBoardData(boardId));
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
            window.location.href = '/board?id=${boardId}';  // 보드 페이지로 리다이렉트
        } else if (result.status === 401) {
            return refreshTokenAndRetry(() => editBoard(boardId, title, content));
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
            window.location.href = '/board?id=${boardId}';
        } else if (result.status === 401) {
            return refreshTokenAndRetry(() => inviteBoard(boardId, email));
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
            return refreshTokenAndRetry(() => deleteBoard(boardId));
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
            // 각 컬럼의 카드를 로드
            return result.data;
        } else if (result.status === 401) {
            return refreshTokenAndRetry(() => loadColumns(boardId));
        } else {
            throw new Error(result.message);
        }
    } catch (error) {
        console.error('Error loading columns:', error);
        throw error;
    }
}

export async function createColumn(title, boardId) {
    try {
        const boardContainer = document.getElementById('boardContainer');
        const columns = boardContainer.querySelectorAll('.column');
        const newOrder = columns.length;

        const response = await fetch(`http://localhost:8080/api/boards/${boardId}/columns`, {
            method: 'POST', headers: {
                'Content-Type': 'application/json', 'Authorization': `Bearer ${getAccessToken()}`
            }, body: JSON.stringify({title, orders: newOrder})
        });

        const result = await response.json();

        if (result.status === 201) {
            return result;
        } else if (result.status === 401) {
            return refreshTokenAndRetry(() => createColumn(title, boardId));
        } else {
            throw new Error(result.message || '컬럼 생성에 실패했습니다.');
        }
    } catch (error) {
        alert('컬럼 생성 중 오류가 발생했습니다: ' + error.message);
        throw error;
    }
}


// UI에 컬럼 추가 함수
export function addColumnToUI(columnTitle, columnId) {
    const boardContainer = document.getElementById('boardContainer');
    const addColumnButton = boardContainer.querySelector('.add-column');
    const columnElement = document.createElement('div');
    columnElement.className = `column`;
    columnElement.draggable = true;
    columnElement.id = `column${columnId}`;
    columnElement.innerHTML = `
        <div style="display: flex; justify-content: space-between;">
            <h3>${columnTitle}</h3>
            <button class="delete-column-btn" data-column-id="${columnId}">X</button>
        </div>
        <div class="add-card" data-column-id="${columnId}">+ Add a card</div>
    `;

    columnElement.addEventListener('dragstart', drag);
    columnElement.querySelector('.delete-column-btn').addEventListener('click', function () {
        handleDeleteColumn(columnId);
    });
    columnElement.querySelector('.add-card').addEventListener('click', function () {
        showCreateCardModal(columnId);
    });


    if (addColumnButton) {
        addColumnButton.insertBefore(columnElement, addColumnButton);
    } else {
        boardContainer.appendChild(columnElement);
    }
}

// 컬럼 삭제 API 호출하는 함수
export async function deleteColumn(columnId) {
    try {
        console.log(`Attempting to delete column with id: ${columnId}`); // 로그 추가
        const boardId = localStorage.getItem('board_id'); // boardId 가져오기
        const response = await fetch(`http://localhost:8080/api/boards/${boardId}/columns/${columnId}`, {
            method: 'DELETE', headers: {
                'Authorization': `Bearer ${getAccessToken()}`
            }
        });

        const result = await response.json();

        if (result.status === 200) {
            alert('컬럼 삭제에 성공하였습니다.');
            document.getElementById(`column${columnId}`).remove(); // UI에서 컬럼 삭제
        } else if (result.status === 401) {
            return refreshTokenAndRetry(() => deleteColumn(columnId));
        } else {
            throw new Error(result.message || '컬럼 삭제에 실패했습니다.');
        }
    } catch (error) {
        console.error('Error deleting column:', error);
        alert('Error deleting column: ' + error.message);
    }
}

// 삭제 버튼 핸들러
window.handleDeleteColumn = function (columnId) {
    deleteColumn(columnId);
};

// Show Modal 함수 추가
function showModal(modalId) {
    document.getElementById(modalId).style.display = "flex";
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = "none";
}

// 순서확정 함수
export async function confirmOrder() {
    try {
        const boardContainer = document.getElementById('boardContainer');
        const columns = Array.from(boardContainer.querySelectorAll('.column'));
        const updatedOrders = columns.map((column, index) => ({
            columnId: column.id.replace('column', ''), orders: index
        }));

        const boardId = localStorage.getItem('board_id');

        const response = await fetch(`http://localhost:8080/api/boards/${boardId}/columns/order`, {
            method: 'PATCH', headers: {
                'Authorization': `Bearer ${getAccessToken()}`, 'Content-Type': 'application/json'
            }, body: JSON.stringify(updatedOrders)
        });

        const result = await response.json();


        if (result.status === 200) {
            alert('순서 수정이 완료되었습니다.');
            closeModal('confirmOrderModal');
            window.location.reload();
        } else if (result.status === 401) {
            return refreshTokenAndRetry(() => confirmOrder());
        } else {
            throw new Error(result.message);
        }
    } catch (error) {
        console.error('Error confirming order:', error);
        alert('Error confirming order: ' + error.message);
    }
}

// 보드의 모든 카드를 가져오는 함수
export async function loadAllCards(boardId) {
    try {
        const cards = await cardAPI.getCards(boardId, 'all');
        return cards;
    } catch (error) {
        console.error('Error loading cards:', error);
        throw error;
    }
}

// 카드를 column별로 분류하고 정렬하는 함수
export function organizeCardsByColumn(cards) {
    const cardsByColumn = {};
    cards.forEach(card => {
        if (!cardsByColumn[card.status]) {
            cardsByColumn[card.status] = [];
        }
        cardsByColumn[card.status].push(card);
    });

    // 각 column의 카드를 order에 따라 정렬
    for (let columnId in cardsByColumn) {
        cardsByColumn[columnId].sort((a, b) => a.orders - b.orders);
    }

    return cardsByColumn;
}

// 카드 생성 처리
export async function handleCreateCard(event) {
    event.preventDefault();
    const boardId = localStorage.getItem('board_id');
    const columnId = document.getElementById('createCardModal').dataset.columnId;
    const title = document.getElementById('newCardTitle').value;
    const content = document.getElementById('newCardContent').value;
    const assignee = document.getElementById('newCardAssignee').value;
    const dueDate = document.getElementById('newCardDueDate').value;

    const column = document.getElementById(`column${columnId}`);
    const cards = column.querySelectorAll('.card');
    const newOrder = cards.length;  // 새 카드는 현재 컬럼의 마지막에 추가됩니다.

    const cardData = {
        title, content, assignee, dueDate: dueDate ? new Date(dueDate).toISOString().split('T')[0] : null, orders: newOrder
    };

    try {
        const newCard = await cardAPI.createCard(boardId, columnId, cardData);
        addCardToUI(columnId, newCard);
        closeModal('createCardModal');
    } catch (error) {
        alert('카드 생성 실패: ' + error.message);
    }
}

// UI에 카드 추가
export function addCardToUI(columnId, card) {
    const column = document.getElementById(`column${columnId}`);
    const cardElement = document.createElement('div');
    cardElement.className = 'card';
    cardElement.draggable = true;
    cardElement.id = `card${card.id}`;
    cardElement.innerHTML = `
        <div>${card.title}</div>
        <div>Assignee: ${card.assignee}</div>
        <div>Due Date: ${card.dueDate}</div>
    `;
    cardElement.addEventListener('dragstart', drag);
    cardElement.addEventListener('click', () => showModal('cardDetailModal'));

    // 'Add a card' 버튼 앞에 새 카드를 삽입
    const addCardButton = column.querySelector('.add-card');
    column.insertBefore(cardElement, addCardButton);
}

// 카드 생성 모달 표시
export function showCreateCardModal(columnId) {
    const modal = document.getElementById('createCardModal');
    modal.style.display = 'flex';  // 'block' 대신 'flex' 사용
    modal.style.alignItems = 'center';  // 세로 중앙 정렬
    modal.style.justifyContent = 'center';  // 가로 중앙 정렬
    modal.dataset.columnId = columnId;
}

// 드래그 시작 시 호출되는 함수
function drag(ev) {
    ev.dataTransfer.setData("text", ev.target.id);
}