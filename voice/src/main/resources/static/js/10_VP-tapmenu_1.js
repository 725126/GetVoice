function changeTab2(tabIndex) {
    // 모든 탭 버튼과 탭 내용의 클래스를 초기화
    var tabButtons = document.getElementsByClassName('tab-button-explain');
    var tabContents = document.getElementsByClassName('tab-content-explain');

    for (var i = 0; i < tabButtons.length; i++) {
        tabButtons[i].classList.remove('active');
        tabContents[i].classList.remove('active');
    }

    // 선택된 탭 버튼과 탭 내용에 클래스를 추가하여 활성화
    tabButtons[tabIndex].classList.add('active');
    tabContents[tabIndex].classList.add('active');
}