function isValidYouTubeUrl(url) {
    // 유효한 유튜브 URL을 검증하는 정규 표현식
    var youtubeRegex = /^(https?:\/\/)?(www\.)?(youtube|youtu|youtube-nocookie)\.(com|be)\/(watch\?v=|embed\/|v\/|.+\?v=)?([^&=%\?]{11})/;

    // 주어진 URL이 유효한 유튜브 URL인지 확인
    return youtubeRegex.test(url);
}

// 영상샘플 - 수정 버튼 클릭 시
// URL에서 youtubeUrl,title, id, selectedKeywords 매개변수 읽기
var params = new URLSearchParams(window.location.search);
var youtubeUrl = params.get('youtubeUrl');
var title = params.get('title');
var id = params.get('id');
var selectedKeywords = JSON.parse(params.get('selectedKeywords'));

// 불러온 요소들 복원
$("#youtubeUrl").val(youtubeUrl);
$("#title").val(title);
localStorage.setItem("selectedKeywords", JSON.stringify(selectedKeywords));

// 완료 버튼 클릭 시
document.getElementById("submitBtn").addEventListener("click", function(event) {
    event.preventDefault();
    // 영상샘플 게시물 데이터 가져오기
    var youtubeUrl = document.getElementById("youtubeUrl").value;
    var title = document.getElementById("title").value;
    var selectedKeywords = JSON.parse(localStorage.getItem("selectedKeywords")) || [];

    if (!isValidYouTubeUrl(youtubeUrl)) {
        alert("유효하지 않은 유튜브 URL입니다.");
        return;
    }

    if (selectedKeywords.length === 0) {
        alert("키워드를 선택하지 않았습니다. 최소 한 가지 이상의 키워드를 선택해주세요.");
        return;
    }

    if (title.trim() === "") { // trim() 함수: 문자열의 양 끝에 있는 공백을 제거해주는 역할
        alert("제목을 입력해주세요.");
        return;
    }

    var data = { // 컨트롤러에 전송되는(JAVA) 변수명 : js 변수명
        id : id, // 추가
        youtubeUrl: youtubeUrl,
        title: title,
        selectedKeyList: selectedKeywords
    };

    // JSON 형식으로 변환
    var jsonData = JSON.stringify(data);

    // AJAX 요청 보내기
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/popup/voiceport/vp-video-post-endpoint", true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                console.log("영상샘플이 성공적으로 등록되었습니다");
                // 요청이 성공적으로 처리됨
                // 팝업 창 닫기
                window.close();
                // 부모 페이지 새로고침 (부모 페이지의 URL을 정확하게 지정해야 함)
                window.opener.location.reload();
            } else {
                // 요청이 실패한 경우 에러를 출력하고 확인합니다.
                console.error("요청 실패: " + xhr.status);
            }
        }
    };
    xhr.send(jsonData);

    localStorage.removeItem("selectedKeywords");
    event.stopPropagation();
});











