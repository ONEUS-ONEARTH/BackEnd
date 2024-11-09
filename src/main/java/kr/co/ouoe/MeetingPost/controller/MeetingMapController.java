package kr.co.ouoe.MeetingPost.controller;


import kr.co.ouoe.MeetingPost.dto.map.MapListResponseDTO;
import kr.co.ouoe.MeetingPost.service.MeetingMapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/meeting")
public class MeetingMapController {
    //미팅 지도와 관련된 컨트롤러 입니다.

    @Autowired
    private MeetingMapService meetingMapService;

    @GetMapping("/map")
    public ResponseEntity<?> getMeetingMap(){
        // 전체 지도위치를 반환 합니다.
        MapListResponseDTO list=meetingMapService.searchAllplace();
        return ResponseEntity.ok().body(list);

    }


}
