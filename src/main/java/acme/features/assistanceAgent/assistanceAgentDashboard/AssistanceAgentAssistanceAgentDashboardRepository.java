
package acme.features.assistanceAgent.assistanceAgentDashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AssistanceAgentAssistanceAgentDashboardRepository extends AbstractRepository {

	@Query("select 1.0 * count(a) / (select count(b) from Claim b where b.assistanceAgent.id = :assistanceAgentId) from Claim a where a.indicator = 0 and a.assistanceAgent.id = :assistanceAgentId")
	Double ratioAcceptedClaimsByAssistanceAgentId(int assistanceAgentId);

	@Query("select 1.0 * count(a) / (select count(b) from Claim b where b.assistanceAgent.id = :assistanceAgentId) from Claim a where a.indicator = 2 and a.assistanceAgent.id = :assistanceAgentId")
	Double ratioRejectedClaimsByAssistanceAgentId(int assistanceAgentId);

	//	@Query("")
	//	Double averageTrackingLogsOfClaimsByAssistanceAgentId(int assistanceAgentId);
	//
	//	@Query("")
	//	Integer maxTrackingLogsOfClaimsByAssistanceAgentId(int assistanceAgentId);

}
