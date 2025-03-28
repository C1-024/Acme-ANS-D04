
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("select c from Claim c where (c.indicator = 0 or c.indicator = 2) and c.assistanceAgent.id = :id")
	Collection<Claim> findCompletedClaims(int id);

	@Query("select c from Claim c where c.indicator = 1 and c.assistanceAgent.id = :id")
	Collection<Claim> findUndergoingClaims(int id);

}
