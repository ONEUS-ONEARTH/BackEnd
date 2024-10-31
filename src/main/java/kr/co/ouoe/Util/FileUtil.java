package kr.co.ouoe.Util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
public class FileUtil {

    public static String convertNewPath(MultipartFile file){
        // 원본 파일명을 중복이 없는 랜덤 이름으로 변경
        String newFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();

        String[] dateInfo = {year+"", len2(month), len2(day)};
        String directoryPath = "";
        for (String s : dateInfo) {
            directoryPath += s+"/";
        }
        return directoryPath+newFileName;
    }

    public static Path upload(MultipartFile file, String rootPath) {
        // 원본 파일명을 중복이 없는 랜덤 이름으로 변경
        String newFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // 이 파일을 날짜별로 관리하기 위해 날짜별 폴더를 생성
        String newUploadPath = makeDateFormatDirectory(rootPath);
        log.info("newupload{}",newUploadPath);
        // 파일의 풀 경로를 생성
        String fullPath = newUploadPath + "/" + newFileName;

        log.info("filefulpath{}",fullPath);
        Path copyOfLocation = Paths.get(newUploadPath + File.separator + StringUtils.cleanPath(newFileName));
        log.info("copyOfLocation{}",copyOfLocation);

        //return fullPath.substring(rootPath.length());
        //return  fullPath;
        return  copyOfLocation;
    }



    private static String makeDateFormatDirectory(String rootPath) {
        // 오늘 날짜 정보 추출
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();

        String[] dateInfo = {year+"", len2(month), len2(day)};
        String directoryPath = rootPath;
        for (String s : dateInfo) {
            directoryPath += "/"+s;
            File f = new File(directoryPath);
            if(!f.exists()) f.mkdirs();
        }

        return  directoryPath;
    }

    private static String len2(int n){
        return new DecimalFormat("00").format(n);
    }


   /* public static String downloadFile(String fileUrl, String saveFilePath) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> response = restTemplate.getForEntity(fileUrl, byte[].class);

        String uploadPath = null;

        if (response.getStatusCode() == HttpStatus.OK) {
            HttpHeaders headers = response.getHeaders();
            byte[] fileBytes = response.getBody();
            //디렉터리 파일 없으면 만들기
            makeDateFormatDirectory(saveFilePath);
            //fileByte 기반 멀티파트 파일 만들기
            MultipartFile multipartFile = MultipartFileUtil.convertBytesToMultipartFile(fileBytes,"temp");
            // 이걸 기반으로 uuid있는 파일 만들기

            uploadPath = upload(multipartFile, saveFilePath);


        } else {
            // Handle error
        }
        return uploadPath;
    }

    */

    public static byte[] convertImageToByteArray(String filePath) throws IOException {
        File imageFile = new File(filePath);
        FileInputStream inputStream = new FileInputStream(imageFile);
        try {
            return StreamUtils.copyToByteArray(inputStream);
        } finally {
            inputStream.close();
        }
    }

    public  static void fileUpload(MultipartFile multipartFile,Path uploadDir) throws IOException {
        // File.seperator 는 OS종속적이다.
        // Spring에서 제공하는 cleanPath()를 통해서 ../ 내부 점들에 대해서 사용을 억제한다
        //Path copyOfLocation = Paths.get(uploadDir + File.separator + StringUtils.cleanPath(filename));
        //log.info("copylocation{}",copyOfLocation);
        // inputStream을 가져와서
        // copyOfLocation (저장위치)로 파일을 쓴다.
        // copy의 옵션은 기존에 존재하면 REPLACE(대체한다), 오버라이딩 한다
        Files.copy(multipartFile.getInputStream(), uploadDir, StandardCopyOption.REPLACE_EXISTING);

    }




}