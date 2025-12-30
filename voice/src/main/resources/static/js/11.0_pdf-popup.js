// 버튼 클릭 이벤트 핸들러 함수
function handlePdfButtonClick() {
    var button = document.querySelector('.pdf.upload-button');
    var buttonText = button.textContent || button.innerText; // 버튼의 텍스트를 가져옴 (크로스 브라우징을 위해 textContent와 innerText 모두 사용)

   if (buttonText.trim() === '이력서 PDF 업로드') {
        // 버튼의 텍스트가 "썸네일 업로드"인 경우
        showpopup_upload2();
    }
}

// "이력서 PDF 변경" 또는 "이력서 PDF 업로드" 버튼 클릭 시 handleButtonClick() 함수 호출
document.querySelector('.pdf.upload-button').addEventListener('click', handlePdfButtonClick);


// 수정 요청 함수
function sendPdfModifyRequest(id) {
    // AJAX 요청을 보낼 URL 설정
    var url = "/popup/voiceport/vp-pdf-modify-endpoint?id=" + id;

    // AJAX 요청 보내기
    $.ajax({
        type: "GET",
        url: url,
        success: function (response) {
            // 성공적으로 데이터를 받았을 때의 동작
            console.log("게시물 데이터를 성공적으로 받았습니다:", response);
             if (response) {
                // 수정 데이터를 전시할 팝업창에 수정 데이터 전송
                var popupUrl = "/popup/voiceport/11.5_pdf-upload-11.5" +
                               "?originalFileName=" + encodeURIComponent(response.originalFileName) +
                               "&id=" + encodeURIComponent(response.id) +
                               "&saveFileName=" + encodeURIComponent(response.saveFileName);
                window.open(popupUrl, "_blank", "width=470, height=320, left=600, top=200");

             } else {
                console.error("(이력서) 서버에서 받은 데이터가 유효하지 않습니다.");
             }
        },
        error: function (error) {
            // 오류 처리 로직
            console.error("(이력서) 데이터를 불러오는 중 오류가 발생했습니다:", error);
        }
    });
}

$(document).ready(function() {
    $(".mod-pdf").click(function() {
       // 클릭된 버튼의 ID 가져오기
        var buttonId = $(this).attr("id");
        // 버튼의 ID에서 audioDTO.id 추출 (예: "modifyBtn_1"에서 "1" 추출)
        var id = buttonId.split("_")[1];
        // ID 값을 이용하여 sendModifyRequest 함수 호출
        sendPdfModifyRequest(id);
    });
});