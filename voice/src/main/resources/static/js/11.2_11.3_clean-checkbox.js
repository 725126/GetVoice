function showpopup_select(event) {
 var newWindow = window.open('', '샘플 키워드 설정', 'width=540,height=445,left=350,top=30');
 newWindow.location.href = '/popup/etc/8.1_GV-F-ChKw-8.1';
};

window.addEventListener('beforeunload', function(event) {
    // 페이지가 닫힐 때, 로컬 스토리지 비우기
    localStorage.removeItem("selectedKeywords");
});
