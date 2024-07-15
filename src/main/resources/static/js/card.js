// card.js
import {getAccessToken, refreshTokenAndRetry} from './auth.js';

export async function loadCards(boardId, columnId) {
    try {
        const response = await fetch(`http://localhost:8080/api/boards/${boardId}/columns/${columnId}/cards`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${getAccessToken()}`
            }
        });

        const result = await response.json();

        if (result.status === 200) {
            return result.data;
        } else if (result.status === 401) {
            return refreshTokenAndRetry(() => loadCards(boardId, columnId));
        } else {
            throw new Error(result.message);
        }
    } catch (error) {
        console.error('Error loading cards:', error);
        throw error;
    }
}

export async function createCard(boardId, columnId, cardData) {
    try {
        const response = await fetch(`http://localhost:8080/api/boards/${boardId}/columns/${columnId}/cards`, {
            method: 'POST', headers: {
                'Content-Type': 'application/json', 'Authorization': `Bearer ${getAccessToken()}`
            }, body: JSON.stringify(cardData)
        });
        const result = await response.json();
        if (result.status === 201) {
            return result.data;
        } else if (result.status === 401) {
            return refreshTokenAndRetry(() => createCard(boardId, columnId, cardData));
        } else {
            throw new Error(result.message);
        }
    } catch (error) {
        console.error('Error creating card:', error);
        throw error;
    }
}

export async function updateCard(boardId, columnId, cardId, cardData) {
    try {
        const response = await fetch(`http://localhost:8080/api/boards/${boardId}/columns/${columnId}/cards/${cardId}`, {
            method: 'PUT', headers: {
                'Content-Type': 'application/json', 'Authorization': `Bearer ${getAccessToken()}`
            }, body: JSON.stringify(cardData)
        });
        const result = await response.json();
        if (result.status === 200) {
            return result.data;
        } else if (result.status === 401) {
            return refreshTokenAndRetry(() => updateCard(boardId, columnId, cardId, cardData));
        } else {
            throw new Error(result.message);
        }
    } catch (error) {
        console.error('Error updating card:', error);
        throw error;
    }
}

export async function deleteCard(boardId, columnId, cardId) {
    try {
        const response = await fetch(`http://localhost:8080/api/boards/${boardId}/columns/${columnId}/cards/${cardId}`, {
            method: 'DELETE', headers: {
                'Content-Type': 'application/json', 'Authorization': `Bearer ${getAccessToken()}`
            }
        });
        if (response.status === 204) {
            return true;
        } else if (response.status === 401) {
            return refreshTokenAndRetry(() => deleteCard(boardId, columnId, cardId));
        } else {
            throw new Error('Failed to delete card');
        }
    } catch (error) {
        console.error('Error deleting card:', error);
        throw error;
    }
}

export async function getCards(boardId, condition = 'all', conditionDetail = '') {
    try {
        const response = await fetch(`http://localhost:8080/api/boards/${boardId}/cards?condition=${condition}&conditionDetail=${conditionDetail}`, {
            method: 'GET', headers: {
                'Content-Type': 'application/json', 'Authorization': `Bearer ${getAccessToken()}`
            }
        });
        const result = await response.json();
        if (result.status === 200) {
            return result.data;
        } else if (result.status === 401) {
            return refreshTokenAndRetry(() => getCards(boardId, condition, conditionDetail));
        } else {
            throw new Error(result.message);
        }
    } catch (error) {
        console.error('Error fetching cards:', error);
        throw error;
    }
}

export async function moveCard(boardId, columnId, cardId, newColumnId, newOrder) {
    try {
        const response = await fetch(`http://localhost:8080/api/boards/${boardId}/columns/${columnId}/cards/${cardId}/move`, {
            method: 'PUT', headers: {
                'Content-Type': 'application/json', 'Authorization': `Bearer ${getAccessToken()}`
            }, body: JSON.stringify({
                toColumnId: newColumnId, toOrder: newOrder
            })
        });
        const result = await response.json();
        if (result.status === 200) {
            return result.data;
        } else if (result.status === 401) {
            return refreshTokenAndRetry(() => moveCard(boardId, columnId, cardId, newColumnId, newOrder));
        } else {
            throw new Error(result.message);
        }
    } catch (error) {
        console.error('Error moving card:', error);
        throw error;
    }
}