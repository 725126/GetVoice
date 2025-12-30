function showpopup_cate(){
    window.open('11.1_GV-F-VP-ChKw-11.1', '키워드/카테고리 설정', 'width=540,height=868,left=350,top=30');
};
function showpopup_add1(){
    window.open('11.3_GV-F-VP-VideoUpload-11.3', '영상샘플파일_업로드', 'width=500,height=560,left=600,top=200');
};
function showpopup_add2(){
    window.open('11.2_GV-F-VP-AudioUpload-11.2', '음성샘플파일_업로드', 'width=500,height=580,left=600,top=200');
};
function showpopup_mod1(){
    window.open('11.3_GV-F-VP-VideoUpload-11.3', '영상샘플파일_변경', 'width=500,height=560,left=600,top=200');
};
function showpopup_mod2(){
    window.open('11.2_GV-F-VP-AudioUpload-11.2', '음성샘플파일_변경', 'width=500,height=580,left=600,top=200');
};
function showpopup_upload1(){
    window.open('11.4_thumbnail-upload-11.4', '썸네일_업로드', 'width=470,height=320,left=600,top=200');
};
function showpopup_upload2(){
    window.open('11.5_pdf-upload-11.5', '이력서_PDF_업로드', 'width=470,height=320,left=600,top=200');
};


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