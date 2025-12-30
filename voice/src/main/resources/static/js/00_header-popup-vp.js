// 나의 보이스포트 버튼 클릭 시
document.querySelectorAll('.my-vp-button').forEach(button => {
    button.addEventListener('click', function() {
        var username = this.id.replace('vaMyVpBtn_', ''); // 나의 보이스포트 버튼의 ID에서 회원 아이디 추출
        sendMyVpRequest(username);
    });
});

// 나의 보이스포트 요청 함수
function sendMyVpRequest(username) {
    // AJAX 요청을 보낼 URL 설정
    var url = "/board/ct/my-vp-endpoint?username=" + username;

    // AJAX 요청 보내기
    $.ajax({
        type: "GET",
        url: url,
        success: function (response) {
            // 성공적으로 데이터를 받았을 때의 동작
            console.log("(나의 보이스포트) 회원 데이터를 성공적으로 받았습니다:", response);
            console.log("pid: ", response.pid);
            console.log("pId: ", response.pId);

             if (response) {
                // 첫 작성 여부를 판단하여 생성할 페이지 결정

                var popupUrl;
                var urlOption;

                console.log("response: ", response);

                if(response.title == null){
                    urlOption = "width=1042, height=953, left=200, top=50";
                    popupUrl = "../../popup/voiceport/11.0_GV-F-VPCtrl-11.0" + "?username=" + encodeURIComponent(response.username) +
                                                                               "&nickname=" + encodeURIComponent(response.nickname);

                    console.log("나의 보이스포트- 첫 작성: ", popupUrl);

                    window.open(popupUrl, "_blank", urlOption);
                }
                else if(response.title != null ){

                        var xhr = new XMLHttpRequest();
                        xhr.open("POST", "/popup/voiceport/vp-data-call", true);
                        xhr.setRequestHeader("Content-Type", "application/json");

                        xhr.onreadystatechange = function () {
                            if (xhr.readyState === 4 && xhr.status === 200) {
                                // 서버 응답을 받은 후의 로직
                                 urlOption = "width=1042, height=953, left=200, top=50";
                                 popupUrl = "../../popup/voiceport/10.0_GV-F-VP-10" + "?username=" + encodeURIComponent(response.username) +
                                                                                      "&nickname=" + encodeURIComponent(response.nickname);

                                 window.open(popupUrl, "_blank", urlOption);

                                console.log("서버 응답:", xhr.responseText);
                            } else if (xhr.readyState === 4 && xhr.status !== 200) {
                                // 오류 처리
                                console.error("오류 발생:", xhr.status);
                            }
                        };

                        var data = { // 컨트롤러에 전송되는(JAVA) 변수명 : js 변수명
                                sampleCount : response.sampleCount,
                                title : response.title,

                                serviceKeyList : response.serviceKeyList,
                                voiceKeyList : response.voiceKeyList,
                                recordingKeyList : response.recordingKeyList,

                                availability: response.availability,

                                introduceContent: response.introduceContent,
                                modifyContent: response.modifyContent,
                                refundContent: response.refundContent,

                                forProfitPrice: response.forProfitPrice,
                                nonProfitPrice: response.nonProfitPrice,

                                pid: response.pid
                        };

                        xhr.send(JSON.stringify(data));
                }

             } else {
                console.error("(나의 보이스포트) 서버에서 받은 데이터가 유효하지 않습니다.");
             }
        },
        error: function (error) {
            // 오류 처리 로직
            console.error("(나의 보이스포트) 데이터를 불러오는 중 오류가 발생했습니다:", error);
        }
    });
}