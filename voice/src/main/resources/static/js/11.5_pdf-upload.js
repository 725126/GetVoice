// 썸네일 버튼 클릭 시
var params = new URLSearchParams(window.location.search);
var originalFileName = params.get('originalFileName');
var saveFileName = params.get('saveFileName');
var id = params.get('id');

var iconPlusOriginalFileName = '<i class="fas fa-download"></i> ' + originalFileName;

// 불러온 요소들 복원
if( originalFileName != null ){
    $("#downloadButton").html(iconPlusOriginalFileName);
}


var element = document.getElementById("downloadButton");
element.title = originalFileName;

console.log("saveFileName: ", saveFileName);

//// 서버로부터 다운로드 URL을 가져옴.
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

// 올리기 버튼 클릭 시
document.getElementById("submitBtn").addEventListener("click", function(event) {
    event.preventDefault();
    // 썸네일 파일 데이터 가져오기
    var fileInput = document.getElementById('fileInput');
    var file = fileInput.files[0];

    // 파일이 비어있는지 확인
    if (!file && !saveFileName) {
        alert('파일을 선택해주세요.');
        return;
    }

     if (file) {
            var fileSizeInMB = file.size / (1024 * 1024); // 파일 크기를 MB로 계산
            console.log("업로드 파일 용량(이력서 PDF): ", fileSizeInMB);
            if (fileSizeInMB > 3) {
                    alert('파일 크기가 3Mb를 초과했습니다.\n' + '3Mb 이하의 파일만 업로드 할 수 있습니다.');
                    return;
            }
     }

    if(!saveFileName || file!=null){
        // 파일 확장자 확인
        var allowedExtensions = /(\.pdf)$/i;
        var fileName = file.name;

        if (!allowedExtensions.exec(fileName)) {
            alert('올바른 파일 형식이 아닙니다. pdf 파일을 업로드해주세요.');
            return;
        }

        // FormData 객체 생성
        var formData = new FormData();

        // 데이터 추가
        formData.append("file", file);

        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/popup/voiceport/vp-pdf-endpoint", true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    console.log("(이력서) 요청이 성공적으로 처리됨");
                    // 팝업 창 닫기
                    if(id != null){
                        alert('이력서 파일이 수정되었습니다.');
                    }
                    window.close();
                    window.opener.location.reload();
                } else {
                    // 요청이 실패한 경우 에러를 출력하고 확인
                    console.error("(이력서) 요청 실패: " + xhr.status);
                }
            }
        };
        xhr.send(formData);
    }

    event.stopPropagation();
});