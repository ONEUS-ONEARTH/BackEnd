package kr.co.ouoe.MeetingPost.controller;


import kr.co.ouoe.MeetingPost.dto.MeetingTopListResponseDTO;
import kr.co.ouoe.MeetingPost.dto.map.MapListResponseDTO;
import kr.co.ouoe.MeetingPost.service.MeetingMapService;
import kr.co.ouoe.MeetingPost.service.MeetingPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/meeting")
public class MeetingMapController {
    //미팅 지도와 관련된 컨트롤러 입니다.

    @Autowired
    private MeetingMapService meetingMapService;

    @Autowired
    private MeetingPostService meetingPostService;

    @GetMapping("/map")
    public ResponseEntity<?> getMeetingMap(){
        // 전체 지도위치를 반환 합니다.
        MapListResponseDTO list=meetingMapService.searchAllplace();
        return ResponseEntity.ok().body(list);

    }
    // 도 시 군구 맞는 지도를 젼환합니다.
    @GetMapping("/map/search/{do}/{si}")
    public ResponseEntity<?> searchMeetingMap(@RequestBody MapListResponseDTO search){
        return null;
    }

    @GetMapping("/map/main")
    public ResponseEntity<?> getMeetingMapMain(){
        //메인 페이지에 가장 좋아요 수를 많이 받은 5개를 반환 합니다.
        MeetingTopListResponseDTO meetingTopListResponseDTO=meetingPostService.getTopFive();
        return ResponseEntity.ok().body(meetingTopListResponseDTO);
    }

}
