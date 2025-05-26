
package acme.entities.trackingLogs;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface TrackingLogRepository extends AbstractRepository {

	@Query("select t from TrackingLog t where t.claim.id = :claimId")
	List<TrackingLog> findAllByClaimId(int claimId);

}
