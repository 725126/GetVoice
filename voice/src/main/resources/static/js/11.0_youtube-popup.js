function showYoutubePopup(youtubeUrl) {
    // 유튜브 URL로 새 창 열기
    window.open(youtubeUrl, '_blank', 'width=600,height=400');
}


// 삭제 버튼 클릭 시
document.querySelectorAll('.delete-video-button').forEach(button => {
    button.addEventListener('click', function() {
        var itemId = this.id.replace('deleteBtn_', ''); // 삭제 버튼의 ID에서 ID 추출
        if (confirm("정말 삭제하시겠습니까?")) {
            sendDeleteRequest(itemId); // 삭제 요청 보내기 (sendDeleteRequest 함수 실행)
        }
    });
});

// 삭제 요청 함수
function sendDeleteRequest(itemId) {
    fetch('/popup/voiceport/vp-video-delete-endpoint/' + itemId, {
        method: 'DELETE', // DELETE 요청 설정
        headers: {
            'Content-Type': 'application/json',
        },
    })
    .then(response => {
        if (response.ok) {
            // 성공적으로 처리된 경우에 수행할 작업
            console.log('삭제 요청이 성공적으로 처리되었습니다.');
            // 삭제된 화면 업데이트
            location.reload();
        } else {
            // 오류 응답 처리
            console.error('삭제 요청이 실패하였습니다.');
        }
    })
    .catch(error => {
        // 네트워크 오류 등을 처리할 코드
        console.error('오류 발생: ' + error.message);
    });
}

// 수정 요청 함수
function sendModifyRequest(id) {
    // AJAX 요청을 보낼 URL 설정
    var url = "/popup/voiceport/vp-video-modify-endpoint?id=" + id;

    // AJAX 요청 보내기
    $.ajax({
        type: "GET",
        url: url,
        success: function (response) {
            // 성공적으로 데이터를 받았을 때의 동작
            console.log("게시물 데이터를 성공적으로 받았습니다:", response);
             if (response) {
                // 수정 데이터를 전시할 팝업창에 수정 데이터 전송
                var popupUrl = "/popup/voiceport/11.3_GV-F-VP-VideoUpload-11.3" +
                               "?youtubeUrl=" + encodeURIComponent(response.youtubeUrl) +
                               "&title=" + encodeURIComponent(response.title) +
                               "&id=" + encodeURIComponent(response.id) +
                               "&selectedKeywords=" + encodeURIComponent(JSON.stringify(response.selectedKeyList));
                window.open(popupUrl, "_blank", "width=500,height=560,left=600,top=200");

             } else {
                console.error("서버에서 받은 데이터가 유효하지 않습니다.");
             }
        },
        error: function (error) {
            // 오류 처리 로직
            console.error("데이터를 불러오는 중 오류가 발생했습니다:", error);
        }
    });
}

$(document).ready(function() {
    // 수정 버튼 클릭 이벤트에 sendModifyRequest 함수 연결
    $(".mod-video-button").click(function() {
        // 클릭된 버튼의 ID 가져오기
        var buttonId = $(this).attr("id");
        // 버튼의 ID에서 urlDTO.id 추출 (예: "modifyBtn_1"에서 "1" 추출)
        var id = buttonId.split("_")[1];
        // ID 값을 이용하여 sendModifyRequest 함수 호출
        sendModifyRequest(id);
    });
});









