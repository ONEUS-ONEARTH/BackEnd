package kr.co.ouoe.DiyPost.controller;


import kr.co.ouoe.DiyPost.dto.PostListResponseDTO;
import kr.co.ouoe.DiyPost.dto.PostResponseDTO;
import kr.co.ouoe.DiyPost.service.upcyclePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/upcycle")
public class upcyclePostController {

    @Autowired
    private upcyclePostService upcycleService;

    //업싸이클 게시물을 불러오는 로직
    @GetMapping("/posts")
    public ResponseEntity<?> postResponseDTOList (){
        //PostListResponseDTO allPost =upcyclePostService.searchAllPost();
        return null;


    }

    //업사이클 게시물을 올리는 로직
    @PostMapping("/createpost")
    public ResponseEntity<?> createPost(@RequestBody PostResponseDTO postResponseDTO){
        return null;
    }

    //


}
