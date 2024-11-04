package kr.co.ouoe.DiyPost.controller;


import kr.co.ouoe.DiyPost.dto.PostListResponseDTO;
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
        //PostListResponseDTO allPost =upcyclePostService.searchAllPost();
        return null;


    }

    //업사이클 게시물을 올리는 로직
    @PostMapping("/createpost")
    public ResponseEntity<?> createPost(@RequestBody PostRequestDTO postRequestDTO, @AuthenticationPrincipal TokenUserInfo tokenUserInfo, BindingResult result){

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

    //


}
