
package acme.features.assistanceAgent.assistanceAgentDashboard;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Principal;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.forms.AssistanceAgentDashboard;
import acme.realms.assistanceAgents.AssistanceAgent;

@GuiService
public class AssistanceAgentAssistanceAgentDashboardShowService extends AbstractGuiService<AssistanceAgent, AssistanceAgentDashboard> {

	// Internal State --------------------------------------------------------------------

	@Autowired
	private AssistanceAgentAssistanceAgentDashboardRepository repository;

	// AbstractGuiService ----------------------------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Principal principal;

		principal = super.getRequest().getPrincipal();

		status = principal.hasRealmOfType(AssistanceAgent.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int assistanceAgentId;
		AssistanceAgentDashboard assistanceAgentDashboard;
		Double resolvedClaimsRatio;
		Double rejectedClaimsRatio;

		assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		//		resolvedClaimsRatio = this.repository.ratioAcceptedClaimsByAssistanceAgentId(assistanceAgentId);
		//		rejectedClaimsRatio = this.repository.ratioRejectedClaimsByAssistanceAgentId(assistanceAgentId);

		assistanceAgentDashboard = new AssistanceAgentDashboard();
		//		assistanceAgentDashboard.setResolvedClaimsRatio(resolvedClaimsRatio);
		//		assistanceAgentDashboard.setRejectedClaimsRatio(rejectedClaimsRatio);

		super.getBuffer().addData(assistanceAgentDashboard);
	}

	@Override
	public void unbind(final AssistanceAgentDashboard assistanceAgentDashboard) {
		Dataset dataset;

		dataset = super.unbindObject(assistanceAgentDashboard, "resolvedClaimsRatio", "rejectedClaimsRatio", "topThreeMonthsClaims", "avgTotalClaims", "minTotalClaims", "maxTotalClaims", "stdevTotalClaims", "avgLastMonthAssistedClaims",
			"minLastMonthAssistedClaims", "maxLastMonthAssistedClaims", "stdevLastMonthAssistedClaims");

		super.getResponse().addData(dataset);
	}

}
