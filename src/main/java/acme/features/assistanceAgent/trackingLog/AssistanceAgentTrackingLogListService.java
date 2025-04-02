
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Principal;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogListService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal State --------------------------------------------------------------------

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;

	// AbstractGuiService ----------------------------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int claimId;
		int currentAssistanceAgentId;
		Claim claim;
		Principal principal;

		principal = super.getRequest().getPrincipal();

		currentAssistanceAgentId = principal.getActiveRealm().getId();
		claimId = super.getRequest().getData("claimId", int.class);
		claim = this.repository.findClaimById(claimId);

		status = principal.hasRealmOfType(AssistanceAgent.class) && claim.getAssistanceAgent().getId() == currentAssistanceAgentId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int claimId;
		Collection<TrackingLog> trackingLogs;

		claimId = super.getRequest().getData("claimId", int.class);

		trackingLogs = this.repository.findAllTrackingLogsByClaimId(claimId);
		super.getBuffer().addData(trackingLogs);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;

		dataset = super.unbindObject(trackingLog, "updateMoment", "step", "resolutionPercentage", "indicator", "resolution");

		super.getResponse().addData(dataset);

	}

	@Override
	public void unbind(final Collection<TrackingLog> objects) {
		int claimId;

		claimId = super.getRequest().getData("claimId", int.class);

		super.getResponse().addGlobal("claimId", claimId);
	}

}
