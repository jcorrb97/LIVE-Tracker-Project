package oee.linearfeet;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LinearFeetRepository extends JpaRepository<LinearFeet, Long>{
	
	//Query driver method for getLive in LinearFeetController
	@Query(value = "SELECT * FROM LINEAR_FEET where STATUS = 'LIVE'", nativeQuery = true)
	List<LinearFeet> findByStatusIsLive();
}
