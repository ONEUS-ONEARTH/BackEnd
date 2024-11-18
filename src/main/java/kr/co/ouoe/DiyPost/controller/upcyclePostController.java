package kr.co.ouoe.DiyPost.controller;


import jakarta.transaction.Transactional;
import kr.co.ouoe.DiyPost.dto.*;
import kr.co.ouoe.DiyPost.service.upcyclePostService;
import kr.co.ouoe.Util.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Slf4j
@RequiredArgsConstructor
@Transactional
@RestController
@RequestMapping("/api/upcycle")
public class upcyclePostController {

    @Autowired
   private upcyclePostService upcyclePostService;

    //업싸이클 게시물을 불러오는 로직
    @GetMapping("/posts")
    public ResponseEntity<?> postResponseDTOList (@AuthenticationPrincipal TokenUserInfo tokenUserInfo){
        PostListResponseDTO allPost;
        if(tokenUserInfo==null){
             allPost =upcyclePostService.searchAllPost();
        }else{
             allPost =upcyclePostService.searchAllPostWithToken(tokenUserInfo);
        }
        return ResponseEntity.ok().body(allPost);


    }
    //보드 페이징 처리 목록 조회 요청
    @GetMapping("/pageNo/{pageNo}")
    public ResponseEntity<?> searchPostListBypageNo(@PathVariable int pageNo){
        List<PostResponseDTO> postResponseDTOList=upcyclePostService.searchPostListWithPage(pageNo-1);
        int allPageNo=upcyclePostService.getAllPageNo(pageNo-1);
        PagePostResponseDTO postListResponseDTO= PagePostResponseDTO.builder().list(postResponseDTOList).allPageNo(allPageNo).build();
        return ResponseEntity.ok().body(postListResponseDTO);

    }

    //업싸이클 디테일 보여주는 로직
    @GetMapping("/posts/{postId}")
    public ResponseEntity<?> showPostDeatil (@PathVariable Long postId,@AuthenticationPrincipal TokenUserInfo tokenUserInfo){
        if (postId == null || postId.equals("")){
            return ResponseEntity
                    .badRequest()
                    .body(PostListResponseDTO.builder().error("postId는 공백 일 수 없습니다!").build());
        }try{
            PostResponseDTO postResponseDTO=upcyclePostService.searchPostById(postId,tokenUserInfo);
            return ResponseEntity.ok().body(postResponseDTO);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }


    // 업사이클 스코어 올리는 로직
    @GetMapping("/posts/score/{postId}")
    public ResponseEntity<?> updateLikeScore (@PathVariable Long postId,@AuthenticationPrincipal TokenUserInfo tokenUserInfo){
        if (postId == null || postId.equals("")){
            return ResponseEntity
                    .badRequest()
                    .body(PostListResponseDTO.builder().error("postId는 공백 일 수 없습니다!").build());
        }
        if(tokenUserInfo==null){
            return ResponseEntity.badRequest().body("로그인 후 이용하실수 있어요!");
        }
        PostListResponseDTO postListResponseDTO = upcyclePostService.updateLikeScore(postId, tokenUserInfo.getEmail());

        return ResponseEntity.ok().body(postListResponseDTO);
    }
    //


    //업사이클 게시물을 올리는 로직
    @PostMapping("/createpost")
    public ResponseEntity<?> createPost( PostRequestDTO postRequestDTO, @AuthenticationPrincipal TokenUserInfo tokenUserInfo, BindingResult result){

        log.info("createPost 실행중");
        if(result.hasErrors()){
            log.warn("DTO 검증 에러 입니다: {}", result.getAllErrors());
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        try{
            upcyclePostService.createPost(postRequestDTO,tokenUserInfo.getEmail());
            return ResponseEntity.ok().body(postRequestDTO);
        }catch(Exception e){
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    //포스트 수정 요청
    @RequestMapping(method = {PUT,PATCH},path = "/modify")
    public ResponseEntity<?> UpdatePost(@RequestBody PostModifyRequestDTO postModifyRequestDTO){
        log.info(postModifyRequestDTO.toString());

        try{
            boolean isupdate=upcyclePostService.modifyPost(postModifyRequestDTO);
            return ResponseEntity.ok().body(isupdate);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }



    //  포스트 삭제 요청
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId, @AuthenticationPrincipal TokenUserInfo tokenUserInfo){
        if (postId == null || postId.equals("")){
            return ResponseEntity
                    .badRequest()
                    .body(PostListResponseDTO.builder().error("postId는 공백 일 수 없습니다!").build());
        }
        try{
            PostListResponseDTO postListResponseDTO=upcyclePostService.delete(tokenUserInfo.getEmail(),postId);
            return  ResponseEntity.ok().body(postListResponseDTO);
        }catch (Exception e){
            return ResponseEntity
                    .internalServerError()
                    .body(PostListResponseDTO.builder().error(e.getMessage()).build());
        }

    }


}
