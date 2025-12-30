// 버튼 클릭 이벤트 핸들러 함수
function handleButtonClick() {
    var button = document.querySelector('.selectKeywords-button');
    var buttonText = button.textContent || button.innerText; // 버튼의 텍스트를 가져옴 (크로스 브라우징을 위해 textContent와 innerText 모두 사용)

   if( buttonText.trim() === '선호키워드 선택') {
        // 버튼의 텍스트가 "선호키워드 선택"인 경우
        showPopupPrkw();
   }

   else if (buttonText.trim() === '선호키워드 변경') {
        // 버튼의 텍스트가 "선호키워드 변경"인 경우
        loadSelectKeywordRequest();
    }

}

// "선호키워드 변경" 또는 "선호키워드 선택" 버튼 클릭 시 handleButtonClick() 함수 호출
document.querySelector('.selectKeywords-button').addEventListener('click', handleButtonClick);

// 선택 키워드 복원 요청 함수
function loadSelectKeywordRequest() {
    // AJAX 요청을 보낼 URL 설정
    var url = "/board/ct/load-join-fav-checkbox";

    // AJAX 요청 보내기
    $.ajax({
        type: "GET",
        url: url,
        success: function (response) {
            // 성공적으로 데이터를 받았을 때의 동작
            console.log("(회원가입) 선호키워드 설정 - 게시물 데이터를 성공적으로 받았습니다:", response);
             if (response) {
                // 수정 데이터를 전시할 팝업창에 수정 데이터 전송
                var popupUrl = "/popup/etc/2.1_GV-F-ChKw-2.1" +
                               "?selectedKeyList=" + encodeURIComponent(JSON.stringify(response));
                window.open(popupUrl, "_blank", "width=520, height=565, left=100, top=50");

             } else {
                console.error("(회원가입) 선호키워드 설정 - 서버에서 받은 데이터가 유효하지 않습니다.");
             }
        },
        error: function (error) {
            // 오류 처리 로직
            console.error("(회원가입) 선호키워드 설정 - 데이터를 불러오는 중 오류가 발생했습니다:", error);
        }
    });
}