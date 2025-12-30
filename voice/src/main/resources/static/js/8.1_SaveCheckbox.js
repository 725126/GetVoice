 // 저장
function saveCheckbox() {
    var selectedKeywords = [];
    var checkboxes = document.querySelectorAll('.checkbox-option:checked');
    if (checkboxes.length > 3) {
        alert("키워드는 최대 3개까지 선택할 수 있습니다.");
        return; // 3개 초과일 때 함수를 종료하여 선택을 저장하지 않음
    }
    if (checkboxes.length < 1) {
        alert("하나 이상의 키워드를 선택해주세요.");
        return; // 1개 미만일 때 함수를 종료하여 선택을 저장하지 않음
    }
    checkboxes.forEach(function(checkbox) {
        selectedKeywords.push(checkbox.name);
    });

    localStorage.setItem("selectedKeywords", JSON.stringify(selectedKeywords));
    window.close();
}

// 로드
function loadCheckbox() {
    var selectedKeywords = localStorage.getItem("selectedKeywords");
    if (selectedKeywords) {
        selectedKeywords = JSON.parse(selectedKeywords);
        selectedKeywords.forEach(function(option) {
            var checkbox = document.getElementById(option);
            if (checkbox) {
                checkbox.checked = true;
            }
        });
    }
}






