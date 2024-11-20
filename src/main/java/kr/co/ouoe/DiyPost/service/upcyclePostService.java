package kr.co.ouoe.DiyPost.service;


import kr.co.ouoe.DiyPost.dto.*;
import kr.co.ouoe.DiyPost.entity.DiyPost;
import kr.co.ouoe.DiyPost.entity.LikeScore;
import kr.co.ouoe.DiyPost.repository.DiyMeterialRepository;
import kr.co.ouoe.DiyPost.repository.DiyPostRepository;
import kr.co.ouoe.DiyPost.repository.LikeScoreRepository;
import kr.co.ouoe.User.entity.User;
import kr.co.ouoe.User.repository.UserRepository;
import kr.co.ouoe.Util.S3Uploader;
import kr.co.ouoe.Util.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class upcyclePostService {

    private final DiyPostRepository diyPostRepository;
    private final UserRepository userRepository;
    private final DiyMeterialRepository diyMeterialRepository;
    private  final LikeScoreRepository likeScoreRepository;
    private final S3Uploader s3Uploader;

    //게시물 불러오기
    public  PostListResponseDTO searchAllPost(){
        List<PostResponseDTO> postList=diyPostRepository.findAllPostResponseDTO();
        
        //닉네임 찾아서 넣어주기

        for(PostResponseDTO post:postList){
            String nickname=userRepository.findById(post.getUserId()).get().getNickname();
            post.setAuthor(nickname);
        }



        
        return PostListResponseDTO.builder()
                .boards(postList)
                .totalPost(postList.size())
                .build();

    }
    public  PostListResponseDTO searchAllPostWithToken(TokenUserInfo tokenUserInfo){
        List<PostResponseDTO> postList=diyPostRepository.findAllPostResponseDTO();

        //닉네임 찾아서 넣어주기

        User user=userRepository.findByEmail(tokenUserInfo.getEmail());
        for(PostResponseDTO post:postList){
            String nickname=userRepository.findById(post.getUserId()).get().getNickname();
            post.setAuthor(nickname);
            boolean isClicked= likeScoreRepository.existsLikeScoreByPostIdAndUserId(post.getId(), user.getId());
            post.setCilcked(isClicked);
        }
        //계정이 있으면 따봉 여부 확인해서 보내주기



        return PostListResponseDTO.builder()
                .boards(postList)
                .totalPost(postList.size())
                .build();

    }





    public  PostResponseDTO searchPostById(Long id, TokenUserInfo tokenUserInfo){

        DiyPost diyPost=diyPostRepository.getOne(id);
        Optional<User> writer=userRepository.findById(diyPost.getUserId());

        PostResponseDTO postResponseDTO;

        if(tokenUserInfo!=null){
            User user=userRepository.findByEmail(tokenUserInfo.getEmail());
            boolean isClicked= likeScoreRepository.existsLikeScoreByPostIdAndUserId(diyPost.getId(), user.getId());

            if(tokenUserInfo.getEmail().equals(user.getEmail())){

                postResponseDTO=PostResponseDTO.builder()
                        .id(diyPost.getId())
                        .title(diyPost.getTitle())
                        .content(diyPost.getContent())
                        .createdDate(diyPost.getCreatedAt())
                        .likeScore(diyPost.getLikeScore())
                        .thumbnailUrl(diyPost.getThumbnailurl())
                        .author(user.getNickname())
                        .userId(user.getId())
                        .isCilcked(isClicked)
                        .tag(diyPost.getTag())
                        .isEditable(true)
                        .build();

            }else{
                postResponseDTO=PostResponseDTO.builder()
                        .id(diyPost.getId())
                        .title(diyPost.getTitle())
                        .content(diyPost.getContent())
                        .likeScore(diyPost.getLikeScore())
                        .userId(writer.get().getId())
                        .createdDate(diyPost.getCreatedAt())
                        .thumbnailUrl(diyPost.getThumbnailurl())
                        .author(writer.get().getNickname())
                        .tag(diyPost.getTag())
                        .isCilcked(isClicked)
                        .isEditable(false)
                        .build();
            }

        }
        else{
            postResponseDTO=PostResponseDTO.builder()
                    .id(diyPost.getId())
                    .title(diyPost.getTitle())
                    .content(diyPost.getContent())
                    .createdDate(diyPost.getCreatedAt())
                    .author(writer.get().getNickname())
                    .likeScore(diyPost.getLikeScore())
                    .thumbnailUrl(diyPost.getThumbnailurl())
                    .userId(writer.get().getId())
                    .tag(diyPost.getTag())
                    .isCilcked(false)
                    .isEditable(false)
                    .build();

        }




        return postResponseDTO;
    }


    //페이징 처리된 보드 불러오기
    public List<PostResponseDTO> searchPostListWithPage(int pageNo){
        PageRequest pageRequest=PageRequest.of(pageNo,20, Sort.by("postUpdateDateTime").descending());
        Page<DiyPost> result=diyPostRepository.findAll(pageRequest);
        int totalPage=result.getTotalPages();
        List<PostResponseDTO> list = new ArrayList<>();
        result.forEach(diyPost -> {
            PostResponseDTO post= PostResponseDTO.builder()
                    .id(diyPost.getId())
                    .title(diyPost.getTitle())
                    .content(diyPost.getContent())
                    .build();
            list.add(post);
                }

        );
        return list;
    }

    // 총 페이지 수 구하기
    public int getAllPageNo(int pageNo) {
        PageRequest pageRequest = PageRequest.of(pageNo, 6, Sort.by("boardUpdateDateTime").descending());
        Page<DiyPost> result = diyPostRepository.findAll(pageRequest); // 해당 페이지 리스트
        return result.getTotalPages();
    }

    //upcycle 포스트 등록
    public PostListResponseDTO createPost(PostRequestDTO postRequestDTO, String email) throws IOException {

        log.info(postRequestDTO.getTitle());
        User user=userRepository.findByEmail(email);
        if(user==null){
            return null;
        }

        //Diypost 저장,태그까지 저장
        LocalDateTime createDateTime=LocalDateTime.now();
        String s3Url= s3Uploader.uploadFileToS3(postRequestDTO.getThumbnailUrl());
        DiyPost newDiyPost= new DiyPost(postRequestDTO.getTitle(), postRequestDTO.getContent(),createDateTime,s3Url , postRequestDTO.getTag(),user.getId());
        diyPostRepository.save(newDiyPost);

        return searchAllPost();

    }

    //pucycle 포스트 수정
    public boolean modifyPost(PostModifyRequestDTO modifyRequestDTO) throws IOException {
        DiyPost diyPost=diyPostRepository.getOne(modifyRequestDTO.getPostId());
        //String user= userRepository.findById(diyPost.getUserId());
        diyPost.setTitle(modifyRequestDTO.getTitle());
        diyPost.setContent(modifyRequestDTO.getContent());
        diyPost.setTag(modifyRequestDTO.getTag());

        String s3url= s3Uploader.uploadFileToS3(modifyRequestDTO.getThumbnailUrl());

        diyPost.setThumbnailurl(s3url);
        return true;

    }


    //보드 삭제 처리
    public PostListResponseDTO delete(String useremail, Long boardNo) {
        try {
            //유저 찾기
            User user=userRepository.findByEmail(useremail);
            // 보드를 찾기
            DiyPost post = diyPostRepository.findByIdAndUserId(boardNo,user.getId());
            if (post == null) {
                log.warn("삭제할 보드를 찾을 수 없습니다. 계정: {}, 보드 번호: {}", useremail, boardNo);
                return null;
            }


            // 보드를 삭제
            diyPostRepository.deleteById(boardNo);

            log.info("보드 삭제 성공. 계정: {}, 보드 번호: {}", useremail, boardNo);
        } catch (Exception e) {
            log.error("보드 삭제 중 오류 발생. 계정: {}, 보드 번호: {}",useremail, boardNo, e);
        }
        return searchAllPost();
    }

    //포스트 좋아요 업데이트
    public PostResponseDTO updateLikeScore(long postId, TokenUserInfo tokenUserInfo){
        // 포스트 가져오기
        DiyPost diyPost=diyPostRepository.getOne(postId);
        //User 데려오기
        User user=userRepository.findByEmail(tokenUserInfo.getEmail());
        int likeSocre=diyPost.getLikeScore();
        boolean isExists= likeScoreRepository.existsLikeScoreByPostIdAndUserId(postId,user.getId());
        //  Like 에서 posiId,와 UserId 조회 가져오기-> 이전에 유저가 굿을 누른적이 있는지 가져오기
        if(isExists){
            //존재하면 -1로 감소
            diyPost.setLikeScore(likeSocre-1);
            LikeScore likeScore = likeScoreRepository.findLikeScoreByPostIdAndUserId(postId,user.getId());
            likeScoreRepository.delete(likeScore);



        }else{
            //존재하지 않으면 1 업데이드
            diyPost.setLikeScore(likeSocre+1);

            // 굿 표시한사람 저장.
            LikeScore likeScore =new LikeScore();
            likeScore.setPostId(postId);
            likeScore.setUserId(user.getId());
            likeScoreRepository.save(likeScore);

        }


        return searchPostById(postId,tokenUserInfo);
    }

    public PostTopListResponseDTO   getTopFivePost(){
        List<PostTopResponseDTO> postTopResponseDTOList=diyPostRepository.findTopFivePostTopResponse();

        for(PostTopResponseDTO post:postTopResponseDTOList){
            String nickname=userRepository.findById(post.getUserId()).get().getNickname();
            post.setAuthor(nickname);
        }
        PostTopListResponseDTO postTopListResponseDTO= PostTopListResponseDTO.builder().boards(postTopResponseDTOList).build();
        return postTopListResponseDTO;


    }
//


}
