// 보이스포트 팝업 요청 함수
function sendVpPopupRequest(pid) {
    var popupUrl = "/popup/voiceport/10.0_GV-View-VP-10?pid=" + pid;

    window.open(popupUrl, "_blank", "width=1042, height=953, left=200, top=50");
}

$(document).ready(function() {
    // 수정 버튼 클릭 이벤트에 sendModifyRequest 함수 연결
    $(".vp-item").click(function() {
        // 클릭된 div의 ID 가져오기
        var divId = $(this).attr("id");
        // div ID에서 vpCtrlDTO.pid 추출
        var pid = divId.split("_")[1];
        // ID 값을 이용하여 sendVpPopupRequest 함수 호출
        sendVpPopupRequest(pid);
    });
});