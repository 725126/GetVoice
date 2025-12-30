window.onload = function() {

    var params = new URLSearchParams(window.location.search);
    var selectedKeyList = JSON.parse(params.get('selectedKeyList'));

    console.log(selectedKeyList);

    if( selectedKeyList != null ){
        // 선택한 체크박스 복원
        loadCheckbox(selectedKeyList);
    }

    // 저장 버튼 클릭 시 이벤트 핸들러
    var saveButton = document.querySelector('.btn');
    saveButton.addEventListener('click', function(event) {
        event.preventDefault();

        // 체크된 항목을 저장하는 배열
        var voiceTypes = [];

        // 체크된 항목을 몇 개 선택했는지 확인
        var voiceTypeSelected = 0;

        // 음성 유형 체크박스 확인
        for (var i = 1; i <= 45; i++) {
            var checkbox = document.getElementById(i);
            if (checkbox && checkbox.checked) {
                voiceTypes.push(i); // 체크된 항목 저장
                voiceTypeSelected++;
            }
        }

        // 최소 하나 이상의 체크박스를 선택하지 않은 경우 경고창 표시
        if (voiceTypeSelected < 1) {
            alert('하나 이상의 키워드를 선택해주세요.');
            return;
        }

        if(voiceTypeSelected > 5) {
            alert('키워드는 최대 5개까지 선택할 수 있습니다.');
            return;
        }

        console.log("음성 유형: ", voiceTypes);


       var data = { voiceKeyList: voiceTypes };

        // ajax 전송
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/board/ct/join-fav-checkbox-endpoint", true);
        xhr.setRequestHeader("Content-Type", "application/json");

        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                // 서버 응답을 받은 후의 로직
                console.log("(회원가입) 선호키워드 설정 - 서버 응답:", xhr.responseText);

            } else if (xhr.readyState === 4 && xhr.status !== 200) {
                // 오류 처리
                console.error("(회원가입) 선호키워드 설정 - 오류 발생:", xhr.status);
            }
        };

        xhr.send(JSON.stringify(data));

        // 키워드가 설정됨 > 부모페이지의 버튼 텍스트 변경
        opener.window.document.querySelector('.selectKeywords-button').textContent = "선호키워드 변경";

        // 저장되었다는 알림을 표시
        alert('설정이 저장되었습니다.');
        window.close();
    });
}

function loadCheckbox(selectedKeyList){

        var selectedCheckboxes = {};

        // selectedCheckboxes 객체에 속성으로 추가하고 값을 true로 설정
        for (var i = 0; i < selectedKeyList.length; i++) {
           selectedCheckboxes[selectedKeyList[i]] = true;
        }

        // 음성 유형 체크박스 복원
        for (var i = 1; i <= 45; i++) {
            var checkbox = document.getElementById(i);
            // 만약 해당 ID를 가진 체크박스 엘리먼트가 존재하고, selectedCheckboxes 객체에 속성이 true로 설정되어 있다면
            if (checkbox && selectedCheckboxes[i]) {
                // 해당 체크박스를 체크 상태로 설정
                checkbox.checked = true;
            }
        }

}


