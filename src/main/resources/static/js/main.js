import { checkAuth } from './auth.js';
import { loadMainPageData, renderBoards } from './board.js';
import { updateWelcomeMessage } from './util.js';
document.addEventListener('DOMContentLoaded', async function() {
    try {
        await checkAuth();
        console.log('Authentication checked');

        // 메인으로 넘어갈 때 board_id 삭제
        localStorage.removeItem("board_id");

        const data = await loadMainPageData();
        console.log('Main page data loaded:', data);

        if (data && data.username) {
            updateWelcomeMessage(data.username);
            console.log('Welcome message updated');
        } else {
            console.warn('Username not found in data');
        }

        if (data && data.boards) {
            renderBoards(data.boards);
            console.log('Boards rendered');
        } else {
            console.warn('Boards data not found');
        }
    } catch (error) {
        console.error('Error during main page initialization:', error);
        alert('페이지 초기화 중 오류가 발생했습니다: ' + error.message);
    }
});