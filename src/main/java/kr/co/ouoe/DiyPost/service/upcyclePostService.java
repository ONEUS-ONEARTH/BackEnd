package kr.co.ouoe.DiyPost.service;


import kr.co.ouoe.DiyPost.dto.PostListResponseDTO;
import kr.co.ouoe.DiyPost.dto.PostResponseDTO;
import kr.co.ouoe.DiyPost.repository.DiyMeterialRepository;
import kr.co.ouoe.DiyPost.repository.DiyPostRepository;
import kr.co.ouoe.DiyPost.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class upcyclePostService {

    private final DiyPostRepository diyPostRepository;
    private final DiyMeterialRepository diyMeterialRepository;
    private  final LikeRepository likeRepository;

    //테스트 용
    public PostListResponseDTO searchAllPost(){
        //List<PostResponseDTO> postList=diyPostRepository.findAllPostResponseDTO();
        return null;

    }
}
