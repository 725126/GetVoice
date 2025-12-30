var joinForm = document.forms["join-form"];

joinForm.onsubmit = function(event) {
    var phoneNumberPattern = /^01[0-9]{1}-[0-9]{3,4}-[0-9]{4}$/;
    var phoneNumber = joinForm.elements["phone"].value;
    var id = joinForm.elements["username"].value;
    var nickname = joinForm.elements["nickname"].value;

    if (!phoneNumberPattern.test(phoneNumber)) {
        alert("올바른 연락처를 입력해주세요. (예: 010-1234-5678)");
        event.preventDefault();
        return;
    }

    if (!id) {
        alert("아이디를 입력해주세요.");
        event.preventDefault();
        return;
    }

    if (!nickname) {
        alert("닉네임를 입력해주세요.");
        event.preventDefault();
        return;
    }

    var button = document.querySelector('.selectKeywords-button');
    var buttonText = button.textContent || button.innerText;

    if(buttonText.trim() === '선호키워드 선택'){
        alert("선호키워드를 선택해주세요.");
        event.preventDefault();
        return;
    }

    alert("회원가입이 완료되었습니다.");

}