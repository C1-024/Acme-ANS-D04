
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.flights.Leg;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.AssistanceAgent;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("select c from Claim c where (c.indicator = 0 or c.indicator = 2) and c.assistanceAgent.id = :assistanceAgentId")
	Collection<Claim> findCompletedClaimsByAssistanceAgentId(int assistanceAgentId);

	@Query("select c from Claim c where c.indicator = 1 and c.assistanceAgent.id = :assistanceAgentId")
	Collection<Claim> findUndergoingClaimsByAssistanceAgentId(int assistanceAgentId);

	@Query("select c from Claim c where c.id = :id")
	Claim findClaimById(int id);

	@Query("select c from Claim c where c.assistanceAgent.id = :assistanceAgentId")
	Collection<Claim> findAllClaimsByAssistanceAgentId(int assistanceAgentId);

	@Query("select a from AssistanceAgent a where a.id = :assistanceAgentId")
	AssistanceAgent findAssistanceAgentById(int assistanceAgentId);

	@Query("select l from Leg l where l.draftMode = true")
	Collection<Leg> findAllLegsNotPublished();

	@Query("select l from Leg l where l.id = :legId")
	Leg findLegById(int legId);

	@Query("select t from TrackingLog t where t.claim.id = :claimId")
	Collection<TrackingLog> findAllTrackingLogsByClaimId(int claimId);

}
