
package acme.features.administrator.trackingLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.trackingLogs.TrackingLog;

@Repository
public interface AdministratorTrackingLogRepository extends AbstractRepository {

	@Query("select t from TrackingLog t where t.claim.id = :claimId")
	Collection<TrackingLog> findAllTrackingLogsByClaimId(int claimId);

}
