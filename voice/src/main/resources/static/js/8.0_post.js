// 폼 요소를 가져옵니다.
const form = document.querySelector('form');

// 폼 제출 이벤트를 처리합니다.
form.addEventListener('submit', function(event) {
    // 기본 제출 동작을 막습니다.
    event.preventDefault();

    // 폼 데이터를 FormData 객체로 가져옵니다.
    const formData = new FormData(form);

    // fetch를 사용하여 서버로 데이터를 전송합니다.
    fetch('/board/ct/9.0_GV-F-CTPost-9.0', {
        method: 'POST',
        body: formData, // 폼 데이터를 전송합니다.
    })
    .then(response => response.json()) // 서버 응답을 JSON으로 파싱합니다. (필요에 따라 변경 가능)
    .then(data => {
        // 서버로부터 받은 응답 데이터를 처리합니다.
        console.log('서버 응답 데이터:', data);

        // 원하는 작업을 수행하세요 (예: 페이지 리디렉션, 메시지 표시 등)
    })
    .catch(error => {
        // 오류 처리를 수행합니다.
        console.error('오류 발생:', error);
    });
});
