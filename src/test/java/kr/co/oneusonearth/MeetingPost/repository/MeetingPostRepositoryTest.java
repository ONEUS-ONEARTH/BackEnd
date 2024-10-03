package kr.co.oneusonearth.MeetingPost.repository;

import kr.co.oneusonearth.MeetingPost.domain.MeetingPost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class MeetingPostRepositoryTest {

    @Autowired
    private MeetingPostRepository meetingPostRepository;

    @BeforeEach
    public void mockMvcsetUp() {
        meetingPostRepository.deleteAll();
    }
    
    @Test
    @DisplayName("포스트를 넣으면 등록이 된다")
    void addMeetingPost() {

        meetingPostRepository.save(MeetingPost.builder()
                .title("삼성에서 진행하는 플로깅")
                .content("202410/4일에 진행해요")
                .ThumbNail("사진 uri 어쩌고")
                .build());

    }

}