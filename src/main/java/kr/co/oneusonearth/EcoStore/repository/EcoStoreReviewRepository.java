package kr.co.oneusonearth.EcoStore.repository;

import kr.co.oneusonearth.EcoStore.domain.EcoStore;
import kr.co.oneusonearth.EcoStore.domain.EcoStoreReviewPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EcoStoreReviewRepository extends JpaRepository<EcoStoreReviewPost,Long> {
}
