package kr.co.ouoe.MeetingPost.service;


import kr.co.ouoe.MeetingPost.domain.MeetingLocate;
import kr.co.ouoe.MeetingPost.dto.map.MapListResponseDTO;
import kr.co.ouoe.MeetingPost.dto.map.MapResponseDTO;
import kr.co.ouoe.MeetingPost.repository.MeetingLocateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeetingMapService {

    private final MeetingLocateRepository locateRepository;

    public MapListResponseDTO searchAllplace(){

        List<MapResponseDTO> locates = new ArrayList<>();
        log.info("searchAllplace");
        for (MeetingLocate meetingLocate : locateRepository.findAll()) {
            //MapResponseDTO mapres=MapResponseDTO.builder().x(meetingLocate.getLantitude()).y(meetingLocate.getLongitude()).build();
            //locates.add(mapres);
        }
        //MapListResponseDTO.builder().maps(locates).build();
        return MapListResponseDTO.builder().maps(locates).build();
    }

}
