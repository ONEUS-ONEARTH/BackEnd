package kr.co.ouoe.Util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class S3Uploader {

    @Autowired
    private AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFileToS3(MultipartFile multipartFile) throws IOException {
        File uploadFile=null;
        try{
            uploadFile=convert(multipartFile).orElseThrow(()-> new IllegalArgumentException("erroe : Muiti 파일 변환 실패"));

        }catch (IOException e){
            throw new RuntimeException(e);

        }

        //S3에 저장된 파일 이름
        String fileName=FileUtil.convertNewPath(multipartFile);
        //Path fileName=FileUtil.upload(multipartFile,bucket);

        //s3에 업로드후 로컬 파일 삭제
        String uploadImageUrl=putS3(uploadFile,fileName);
        //removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    public String putS3(File uploadFile,String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket,fileName,uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket,fileName).toString();
    }
    public void deleteS3(String filePath) throws Exception {
        try{
            String key = filePath.substring(56); // 폴더/파일.확장자

            try {
                amazonS3Client.deleteObject(bucket, key);
            } catch (AmazonServiceException e) {
                log.info(e.getErrorMessage());
            }

        } catch (Exception exception) {
            log.info(exception.getMessage());
        }
        log.info("[S3Uploader] : S3에 있는 파일 삭제");
    }

    /**
     * 로컬에 저장된 파일 지우기
     * @param targetFile : 저장된 파일
     */
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("[파일 업로드] : 파일 삭제 성공");
            return;
        }
        log.info("[파일 업로드] : 파일 삭제 실패");
    }




    private Optional<File> convert(MultipartFile file) throws IOException{
        // 로컬에서 저장할 파일 경로
        String dirPath=System.getProperty("user.dir")+"/"+file.getOriginalFilename();
        File convertFile=new File(dirPath);
//        if(!convertFile.createNewFile()){
//            try(FileOutputStream fos=new FileOutputStream(convertFile)){
//                fos.write(file.getBytes());
//            }
//            return Optional.of(convertFile);
//        }
        file.transferTo(convertFile);
        return Optional.of(convertFile);
    }


}
