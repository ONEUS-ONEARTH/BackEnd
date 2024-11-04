package kr.co.ouoe.DiyPost.controller;


import kr.co.ouoe.DiyPost.dto.PostListResponseDTO;
import kr.co.ouoe.DiyPost.dto.PostModifyRequestDTO;
import kr.co.ouoe.DiyPost.dto.PostRequestDTO;
import kr.co.ouoe.DiyPost.dto.PostResponseDTO;
import kr.co.ouoe.DiyPost.service.upcyclePostService;
import kr.co.ouoe.Util.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/upcycle")
public class upcyclePostController {

    @Autowired
   private upcyclePostService upcyclePostService;

    //업싸이클 게시물을 불러오는 로직
    @GetMapping("/posts")
    public ResponseEntity<?> postResponseDTOList (){
        PostListResponseDTO allPost =upcyclePostService.searchAllPost();
        return ResponseEntity.ok().body(allPost);


    }

    //업사이클 게시물을 올리는 로직
    @PostMapping("/createpost")
    public ResponseEntity<?> createPost(@RequestBody PostRequestDTO postRequestDTO, @AuthenticationPrincipal TokenUserInfo tokenUserInfo, BindingResult result){

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
