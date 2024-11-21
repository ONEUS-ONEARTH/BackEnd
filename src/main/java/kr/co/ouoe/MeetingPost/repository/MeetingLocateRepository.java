package kr.co.ouoe.MeetingPost.repository;

import kr.co.ouoe.MeetingPost.entity.MeetingLocate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MeetingLocateRepository extends JpaRepository<MeetingLocate,Long> {
    @Override
    List<MeetingLocate> findAll();
}
