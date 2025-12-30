const checkboxes = document.querySelectorAll('input[type="checkbox"]');
checkboxes.forEach(checkbox => {
    checkbox.addEventListener('change', () => {
        checkboxes.forEach(cb => {
            if (cb !== checkbox) {
                cb.checked = false;
            }
        });
    });
});

// 계산하기 버튼 클릭 시
document.getElementById("submitBtn").addEventListener("click", function(event) {
    event.preventDefault();
    // 견적 계산 게시물 데이터 가져오기
    var fileInput = document.getElementById('fileInput');
    var file = fileInput.files[0];

    // 사용범위(영리, 비영리) 선택창 결과 가져오기
    var forProfitPriceCheckbox = document.getElementById("forProfitPrice");
    var nonProfitPriceCheckbox = document.getElementById("nonProfitPrice");

    // 파일이 비어있는지 확인
    if (!file) {
        alert('파일을 선택해주세요.');
        return;
    }

     if (file) {
            var fileSizeInMB = file.size / (1024 * 1024); // 파일 크기를 MB로 계산
            console.log("업로드 파일 용량(견적 계산): ", fileSizeInMB);
            if (fileSizeInMB > 10) {
                alert('파일 크기가 10Mb를 초과했습니다.\n' + '10Mb 이하의 파일만 업로드 할 수 있습니다.');
                return;
            }
     }

    if (!forProfitPriceCheckbox.checked && !nonProfitPriceCheckbox.checked) {
        alert('사용 범위(영리, 비영리)를 선택해주세요.');
        return;
    }

    // 현재 영리 비영리 선택 결과 저장하는 변수
    var profit = null;

    // 현재 영리 비영리 선택값 가져옴
    var forProfitValue = forProfitPriceCheckbox.checked ? "for-profit" : null;
    var nonProfitValue = nonProfitPriceCheckbox.checked ? "non-profit" : null;

    if( forProfitValue != null ){
            profit = forProfitValue;
        } else if(nonProfitValue != null ){
            profit = nonProfitValue;
    }

    if(file!=null){
        // 파일 확장자 확인
        var allowedExtensions = /(\.pdf|\.txt|\.hwp)$/i;
        var fileName = file.name;

        if (!allowedExtensions.exec(fileName)) {
            alert('올바른 파일 형식이 아닙니다. pdf, txt, hwp 파일을 업로드 해주세요.');
            return;
        }

        // FormData 객체 생성
        var formData = new FormData();

        // 데이터 추가
        formData.append("file", file);
        formData.append("profit", profit);

        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/popup/chat/chat-payment-endpoint", true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    console.log("(결제하기 - 견적계산 파일 첨부) 요청이 성공적으로 처리됨");
                    // 페이지 새로고침
                    window.location.reload();
                } else {
                    // 요청이 실패한 경우 에러를 출력하고 확인
                    console.error("(결제하기 - 견적계산 파일 첨부) 요청 실패: " + xhr.status);
                }
            }
        };

        xhr.send(formData);
    }

    event.stopPropagation();
});

// 결제하기 버튼 클릭 시
document.getElementById("paymentBtn").addEventListener("click", function(event) {

            // FormData 객체 생성
            var formData = new FormData();

            var xhr = new XMLHttpRequest();
            xhr.open("POST", "/popup/chat/chat-payment-submit-endpoint", true);
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) {
                    if (xhr.status === 200) {
                        console.log("결제 요청이 성공적으로 처리됨");
                        // 팝업 창 닫기
                        window.close();
                        // 부모 페이지 새로고침
                        window.opener.location.reload();
                    } else {
                        // 요청이 실패한 경우 에러를 출력하고 확인
                        console.error("결제 요청 실패: " + xhr.status);
                    }
                }
            };

            formData.append("timestamp", getCurrentDateTime());

            xhr.send(formData);
});

// 시각을 가져오는 함수 2 : ex) 2023. 10. 30. 오전 08:13
function getCurrentDateTime() {
    const currentDate = new Date();
    const formattedDate = currentDate.toLocaleString('ko-KR', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' });
    return formattedDate;
}

function extractNumber(inputString) {
    // 정규 표현식을 사용하여 숫자만 추출
    var extractedNumber = parseInt(inputString.replace(/\D/g, ''), 10);
    return extractedNumber;
}