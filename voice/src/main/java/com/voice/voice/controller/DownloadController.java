package com.voice.voice.controller;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.*;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class DownloadController {
    @Value("${img.dir}")
    private String imgDir; // 이미지 파일 저장 경로
    @Value("${upload.dir}")
    private String uploadDir; // 음원 파일 저장 경로
    @Value("${pdf.dir}")
    private String pdfDir; // pdf 파일 저장 경로
    @Value("${record-script.dir}")
    private String scriptDir; // 대본 파일 저장 경로

    @GetMapping("/downloadFile/{fileName:.+}") // 파일 다운로드
    public void downloadFile(@PathVariable String fileName, HttpServletResponse response) {

        System.out.println(fileName);

        try {
            // 일반 파일일 경우,
            MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
            Path filePath = Paths.get("");

            // 파일 이름에서 마지막 점의 인덱스를 찾음
            int lastDotIndex = fileName.lastIndexOf('.');

            // 마지막 점이 없을 경우 확장자가 없음
            if (lastDotIndex == -1) {
                System.out.println("확장자가 없습니다.");
            } else {
                // 마지막 점 이후의 문자열을 추출하여 소문자로 변환
                String extension = fileName.substring(lastDotIndex + 1).toLowerCase();

                if (extension.equals("mp3") || extension.equals("wav")) { // 음원 파일일 경우,
                    // 음원 파일 전용 경로에 저장
                    filePath = Paths.get(uploadDir).resolve(fileName);
                }
                else if (extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg")) { // 이미지 파일일 경우,
                    // 이미지 파일 전용 경로에 저장
                    filePath = Paths.get(imgDir).resolve(fileName);
                    // ※ 이미지 파일의 경우
                    mediaType = MediaType.MULTIPART_FORM_DATA;
                }
                else if (extension.equals("pdf")){
                    // pdf 파일 전용 경로에 저장
                    filePath = Paths.get(pdfDir).resolve(fileName);
                }
                else if(extension.equals("hwp") || extension.equals("txt")){
                    filePath = Paths.get(scriptDir).resolve(fileName);
                }
            }

            System.out.println("파일 경로: " + filePath.toString());
            System.out.println("미디어 타입: " + mediaType.toString());

            byte[] files = FileUtils.readFileToByteArray(new File(filePath.toString()));

            response.setContentType(mediaType.getType());
            response.setContentLength(files.length);
            response.setHeader("Content-Disposition","attachment; fileName=\""+ URLEncoder.encode(fileName, StandardCharsets.UTF_8)+"\";");
            response.setHeader("Content-Transfer-Encoding","binary");

            response.getOutputStream().write(files);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (IOException e){
            System.out.println("다운로드 에러: " + e.getMessage());
            e.getStackTrace();
        }
    }





}
