
package acme.entities.claims;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.trackingLogs.TrackingLog;

public interface ClaimRepository extends AbstractRepository {

	@Query("select t from TrackingLog t where t.claim.id = :claimId order by t.creationMoment desc")
	Collection<TrackingLog> findTrackingLogsByClaimIdOrderedByMoment(int claimId);

}
