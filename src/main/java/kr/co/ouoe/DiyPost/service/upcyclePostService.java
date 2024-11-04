package kr.co.ouoe.DiyPost.service;


import kr.co.ouoe.DiyPost.dto.PostListResponseDTO;
import kr.co.ouoe.DiyPost.dto.PostRequestDTO;
import kr.co.ouoe.DiyPost.dto.PostResponseDTO;
import kr.co.ouoe.DiyPost.entity.DiyPost;
import kr.co.ouoe.DiyPost.repository.DiyMeterialRepository;
import kr.co.ouoe.DiyPost.repository.DiyPostRepository;
import kr.co.ouoe.DiyPost.repository.LikeRepository;
import kr.co.ouoe.User.entity.User;
import kr.co.ouoe.User.repository.UserRepository;
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
//    public  PostListResponseDTO searchAllPost(){
//        List<PostResponseDTO> postList=diyPostRepository.findAll();
//        return PostListResponseDTO.builder()
//                .boards(postList)
//                .build();
//
//    }

    //페이징 처리된 보드 불러오기
    public List<PostResponseDTO> searchPostListWithPage(int pageNo){
        PageRequest pageRequest=PageRequest.of(pageNo,6, Sort.by("postUpdateDateTime").descending());
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


        User user=userRepository.findByEmail(email);
        if(user==null){
            return null;
        }

        //Diypost 저장,태그까지 저장
        LocalDateTime createDateTime=LocalDateTime.now();
        DiyPost newDiyPost= new DiyPost(postRequestDTO.getTitile(), postRequestDTO.getContent(),createDateTime, postRequestDTO.getThumbnailUrl(), postRequestDTO.getTag(),user.getId());
        diyPostRepository.save(newDiyPost);

        return null;

    }

    //보드 삭제 처리
//    public PostListResponseDTO delete(String email, Long boardNo) {
//        try {
//            // 보드를 찾기
//            DiyPost board = diyPostRepository.findByUserIdAndPostId(email, boardNo);
//            if (board == null) {
//                log.warn("삭제할 보드를 찾을 수 없습니다. 계정: {}, 보드 번호: {}", account, boardNo);
//                return null;
//            }
//
//            // 보드가 특정 사용자의 플레이 리스트에 담겨져 있는지 확인
//            List<PlayList> playlists = playListRepository.findByBoard(board);
//
//            // 보드를 null로 설정한 후에 해당 플레이 리스트를 삭제하고 scoreCount를 감소시킴
//            for (PlayList playlist : playlists) {
//                playlist.setBoard(null);
//                playListRepository.delete(playlist);
//
//                AllPlayList plId = playlist.getPlId();
//                plId.setScoreCount(plId.getScoreCount() -1);
//                allPlayListRepository.save(plId);
//            }
//
//            // 좋아요와 싫어요 데이터를 삭제
//            likeAndDislikeRepository.deleteByBoard(board);
//
//            // 보드를 삭제
//            boardRepository.deleteById(boardNo);
//
//            log.info("보드 삭제 성공. 계정: {}, 보드 번호: {}", account, boardNo);
//        } catch (Exception e) {
//            log.error("보드 삭제 중 오류 발생. 계정: {}, 보드 번호: {}", account, boardNo, e);
//        }
//        return searchAllPost();
//    }



}
