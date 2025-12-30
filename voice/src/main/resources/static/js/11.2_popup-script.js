// 음성샘플 - 수정 버튼 클릭 시
// URL에서 youtubeUrl,title, id, selectedKeywords 매개변수 읽기
var params = new URLSearchParams(window.location.search);
var originalFileName = params.get('originalFileName');
var title = params.get('title');
var id = params.get('id');
var selectedKeywords = JSON.parse(params.get('selectedKeywords'));
var saveFileName = params.get('saveFileName');

var iconPlusOriginalFileName = '<i class="fas fa-download"></i> ' + originalFileName;

// 불러온 요소들 복원
if( originalFileName != null ){
    $("#downloadButton").html(iconPlusOriginalFileName);
}
$("#title").val(title);
localStorage.setItem("selectedKeywords", JSON.stringify(selectedKeywords));

var element = document.getElementById("downloadButton");
element.title = originalFileName;

console.log("title: ", title);
console.log("saveFileName: ", saveFileName);

// 서버로부터 다운로드 URL을 가져옴.
$("#downloadButton").click(function() {
        // var fileName = "example.txt"; // 실제 파일 이름으로 대체
        var downloadUrl = "/downloadFile/" + saveFileName;

        // AJAX 요청을 통해 파일 다운로드
        $.ajax({
            url: downloadUrl,
            type: 'GET',
            success: function(data) {
                console.log('파일 다운 성공');

                var link = document.createElement('a');
                link.href = "http://localhost:8080/downloadFile/" + saveFileName;
                link.download = originalFileName;
                link.click();

                console.log('다운 링크: ', link);
            }
        });
});


// 완료 버튼 클릭 시
document.getElementById("submitBtn").addEventListener("click", function(event) {
    event.preventDefault();
    // 음성셈플 게시물 데이터 가져오기
    var title = document.getElementById("title").value;
    var fileInput = document.getElementById('fileInput');
    var selectedKeywords = JSON.parse(localStorage.getItem("selectedKeywords")) || [];
    var file = fileInput.files[0];

    // 파일이 비어있는지 확인
    if (!file && !saveFileName) {
        alert('파일을 선택해주세요.');
        return;
    }

     if (file) {
            var fileSizeInMB = file.size / (1024 * 1024); // 파일 크기를 MB로 계산
            console.log("업로드 파일 용량(음성샘플): ", fileSizeInMB);
            if (fileSizeInMB > 5) {
                        alert('파일 크기가 5Mb를 초과했습니다.\n' + '5Mb 이하의 파일만 업로드 할 수 있습니다.');
                        return;
            }
     }

    if (selectedKeywords.length === 0) {
            alert("키워드를 선택하지 않았습니다. 최소 한 가지 이상의 키워드를 선택해주세요.");
            return;
    }

    if (title.trim() === "") { // trim() 함수: 문자열의 양 끝에 있는 공백을 제거해주는 역할
        alert("제목을 입력해주세요.");
        return;
    }

    // 수정 데이터의 경우, 파일 수정(재업로드)을 하지 않는다면 확장자 확인 및 파일명 저장 불필요

    if(!saveFileName || file!=null){ // 'file' 파라미터에 파일을 첨부하여 요청을 보낼 경우, (업로드 데이터이거나, 파일 수정의 경우)
        // 파일 확장자 확인
        var allowedExtensions = /(\.wav|\.mp3)$/i;
        var fileName = file.name;

        if (!allowedExtensions.exec(fileName)) {
            alert('올바른 파일 형식이 아닙니다. wav 또는 mp3 파일을 업로드해주세요.');
            return;
        }

        // FormData 객체 생성
        var formData = new FormData();

        // 데이터 추가
        formData.append("id", id);
        formData.append("title", title);
        formData.append("file", file);
        formData.append("selectedKeywords", JSON.stringify(selectedKeywords));

        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/popup/voiceport/vp-audio-post-endpoint", true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    console.log("(파일 첨부) 요청이 성공적으로 처리됨");
                    // 팝업 창 닫기
                    window.close();
                    // 부모 페이지 새로고침
                    window.opener.location.reload();
                } else {
                    // 요청이 실패한 경우 에러를 출력하고 확인
                    console.error("(파일 첨부) 요청 실패: " + xhr.status);
                }
            }
        };

        xhr.send(formData);
    }

    if( saveFileName!=null && !file) {
        // FormData 객체 생성
        var formData = new FormData();

        // 데이터 추가
        formData.append("id", id);
        formData.append("title", title);
        formData.append("selectedKeywords", JSON.stringify(selectedKeywords));

        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/popup/voiceport/vp-audio-no-file-modify", true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    console.log("(파일 비첨부) 요청이 성공적으로 처리됨")
                    // 팝업 창 닫기
                    window.close();
                    // 부모 페이지 새로고침
                    window.opener.location.reload();
                } else {
                    // 요청이 실패한 경우 에러를 출력하고 확인
                    console.error("(파일 비첨부) 요청 실패: " + xhr.status);
                }
            }
        };

        xhr.send(formData);
    }

    localStorage.removeItem("selectedKeywords");
    event.stopPropagation();
});