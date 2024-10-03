package kr.co.oneusonearth.MeetingPost.repository;

import kr.co.oneusonearth.MeetingPost.domain.MeetingLocate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface MeetingLocateRepository extends JpaRepository<MeetingLocate,Long> {
}
