// 로그아웃 버튼 클릭 시
document.querySelectorAll('.logout-button').forEach(button => {
    button.addEventListener('click', function() {
        var username = this.id.replace('logoutBtn_', ''); // 로그아웃 버튼의 ID에서 ID 추출
        if (confirm("로그아웃 하시겠습니까?")) {
            sendLogoutRequest(username); // 삭제 요청 보내기 (sendLogoutRequest 함수 실행)
        }
    });
});

// 로그아웃 요청 함수
function sendLogoutRequest(username) {

var urls = ["/board/ct/logout-init-member", "/popup/voiceport/logout-init-vp"];

for (var i = 0; i < urls.length; i++) {
    var xhr = new XMLHttpRequest();
    xhr.open("POST", urls[i], true);
    xhr.setRequestHeader("Content-Type", "application/json");

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            // 로그아웃 요청 성공
            console.log("로그아웃 되었습니다: " + urls[i] + ": " + xhr.responseText);
        }
    };

    xhr.send();
}
    // 로그아웃 화면 업데이트
    window.location.href = "1.0_GV-F-Main-1.0";
}


