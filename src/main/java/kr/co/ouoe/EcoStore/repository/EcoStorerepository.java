package kr.co.ouoe.EcoStore.repository;

import kr.co.ouoe.EcoStore.domain.EcoStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EcoStorerepository extends JpaRepository<EcoStore,Long> {
}
