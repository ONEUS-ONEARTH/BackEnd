package kr.co.ouoe.EcoStore.repository;

import kr.co.ouoe.EcoStore.domain.EcoStore;
import kr.co.ouoe.EcoStore.domain.EcoStoreReviewPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EcoStoreReviewRepository extends JpaRepository<EcoStoreReviewPost,Long> {
}
