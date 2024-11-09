package kr.co.ouoe.DiyPost.service;


import kr.co.ouoe.DiyPost.dto.PostListResponseDTO;
import kr.co.ouoe.DiyPost.dto.PostModifyRequestDTO;
import kr.co.ouoe.DiyPost.dto.PostRequestDTO;
import kr.co.ouoe.DiyPost.dto.PostResponseDTO;
import kr.co.ouoe.DiyPost.entity.DiyPost;
import kr.co.ouoe.DiyPost.entity.Like;
import kr.co.ouoe.DiyPost.repository.DiyMeterialRepository;
import kr.co.ouoe.DiyPost.repository.DiyPostRepository;
import kr.co.ouoe.DiyPost.repository.LikeRepository;
import kr.co.ouoe.User.entity.BookMark;
import kr.co.ouoe.User.entity.User;
import kr.co.ouoe.User.exception.DuplicateEmailException;
import kr.co.ouoe.User.repository.UserRepository;
import kr.co.ouoe.Util.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class upcyclePostService {

    private final DiyPostRepository diyPostRepository;
    private final UserRepository userRepository;
    private final DiyMeterialRepository diyMeterialRepository;
    private  final LikeRepository likeRepository;

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
                .build();

    }

    public  PostResponseDTO searchPostById(Long id, TokenUserInfo tokenUserInfo){

        DiyPost diyPost=diyPostRepository.getOne(id);
        User user=userRepository.getOne(diyPost.getUserId());
        PostResponseDTO postResponseDTO;

        if(tokenUserInfo==null){
            postResponseDTO=PostResponseDTO.builder()
                    .id(diyPost.getId())
                    .title(diyPost.getTitle())
                    .content(diyPost.getContent())
                    .createdDate(diyPost.getCreatedAt())
                    .author(user.getNickname())
                    .tag(diyPost.getTag())
                    .isEditable(false)
                    .build();

        }
        else{
            if(tokenUserInfo.getEmail().equals(user.getEmail())){
                postResponseDTO=PostResponseDTO.builder()
                        .id(diyPost.getId())
                        .title(diyPost.getTitle())
                        .content(diyPost.getContent())
                        .createdDate(diyPost.getCreatedAt())
                        .author(user.getNickname())
                        .tag(diyPost.getTag())
                        .isEditable(true)
                        .build();

            }else{
                postResponseDTO=PostResponseDTO.builder()
                        .id(diyPost.getId())
                        .title(diyPost.getTitle())
                        .content(diyPost.getContent())
                        .createdDate(diyPost.getCreatedAt())
                        .author(user.getNickname())
                        .tag(diyPost.getTag())
                        .isEditable(false)
                        .build();
            }
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
    public PostListResponseDTO createPost(PostRequestDTO postRequestDTO, String email) {

        log.info(postRequestDTO.getTitle());
        User user=userRepository.findByEmail(email);
        if(user==null){
            return null;
        }

        //Diypost 저장,태그까지 저장
        LocalDateTime createDateTime=LocalDateTime.now();
        DiyPost newDiyPost= new DiyPost(postRequestDTO.getTitle(), postRequestDTO.getContent(),createDateTime, postRequestDTO.getThumbnailUrl(), postRequestDTO.getTag(),user.getId());
        diyPostRepository.save(newDiyPost);

        return searchAllPost();

    }

    //pucycle 포스트 수정
    public boolean modifyPost(PostModifyRequestDTO modifyRequestDTO) {
        DiyPost diyPost=diyPostRepository.getOne(modifyRequestDTO.getPostId());
        //String user= userRepository.findById(diyPost.getUserId());
        diyPost.setTitle(modifyRequestDTO.getTitle());
        diyPost.setContent(modifyRequestDTO.getContent());
        diyPost.setTag(modifyRequestDTO.getTag());
        diyPost.setThumbnailurl(modifyRequestDTO.getThumbnailUrl());
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
    public PostListResponseDTO updateLikeScore(long postId,String email){
        // 포스트 가져오기
        DiyPost diyPost=diyPostRepository.getOne(postId);
        //User 데려오기
        User user=userRepository.findByEmail(email);
        int likeSocre=diyPost.getLikeScore();
        boolean isExists= likeRepository.existsLikeByPostIdAndUserId(postId,user.getId());
        //  Like 에서 posiId,와 UserId 조회 가져오기-> 이전에 유저가 굿을 누른적이 있는지 가져오기
        if(isExists){
            throw new DuplicateEmailException("이미 좋아요를 눌렀어요!");

        }else{
            //존재하지 않으면 1 업데이드
            diyPost.setLikeScore(likeSocre+1);

            // 굿 표시한사람 저장.
            Like like=new Like();
            like.setPostId(postId);
            like.setUserId(user.getId());
            likeRepository.save(like);

        }


        return searchAllPost();
    }
//


}
