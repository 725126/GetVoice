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

function saveCheckbox() {
    const selectedCheckbox = document.querySelector('input[type="checkbox"]:checked');

    if (!selectedCheckbox) {
        alert("충전할 금액을 선택해주세요.");
        return;
    }

    const selectedAmount = selectedCheckbox.value;

    if(confirm("선택한 금액: " + formatCurrency(selectedAmount) + "원\n충전하시겠습니까?")){
        // AJAX 요청 보내기
        $.ajax({
            type: "POST",
            url: "/board/ct/vm-char-endpoint",
            data: { amount: selectedAmount },
            success: function(response) {
                alert("V머니가 충전되었습니다." + "\n현재 나의 V머니: " + formatCurrency(response) + "원");
                window.close();
            },
            error: function(error) {
                console.error("서버 요청 중 오류:", error);
            }
        });
    }
}


function formatCurrency(number) {
    // 숫자가 1000 미만인 경우에는 그대로 반환
    if (number < 1000) {
        return number.toString();
    }

    // 숫자를 문자열로 변환하고 소수점을 기준으로 나눔
    var parts = number.toString().split(".");

    // 정수 부분에 콤마를 추가
    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");

    // 다시 합치고 반환
    return parts.join(".");
}