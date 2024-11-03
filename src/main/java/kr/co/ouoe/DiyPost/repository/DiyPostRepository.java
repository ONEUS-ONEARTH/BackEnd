package kr.co.ouoe.DiyPost.repository;


import kr.co.ouoe.DiyPost.dto.PostResponseDTO;
import kr.co.ouoe.DiyPost.entity.DiyPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiyPostRepository extends JpaRepository<DiyPost, Long> {

    // 쿼리 설정으로 모든 레파지토리 키워드랑, 좋아요 수 불러와야함.
    //네이티브 쿼리로 작성해보기
    @Query("select new kr.co.ouoe.DiyPost.dto.PostResponseDTO(" +
            "dp.id ,dp.title, dp.content,dp.user.nickname,dp.createdAt)"+
            "from DiyPost dp")
    List<PostResponseDTO> findAllPostResponseDTO();

    Page<DiyPost> findAll(Pageable pageable);
}
