
package acme.features.assistanceAgent.trackingLog;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Principal;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogIndicator;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogUpdateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal State --------------------------------------------------------------------

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;

	// AbstractGuiService ----------------------------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int trackingLogId;
		int currentAssistanceAgentId;
		Principal principal;
		TrackingLog trackingLog;

		principal = super.getRequest().getPrincipal();

		currentAssistanceAgentId = principal.getActiveRealm().getId();
		trackingLogId = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(trackingLogId);

		status = principal.hasRealmOfType(AssistanceAgent.class) && trackingLog.getClaim().getAssistanceAgent().getId() == currentAssistanceAgentId;

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		int trackingLogId;
		TrackingLog trackingLog;

		trackingLogId = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(trackingLogId);

		super.getBuffer().addData(trackingLog);

	}

	@Override
	public void bind(final TrackingLog trackingLog) {
		super.bindObject(trackingLog, "step", "resolutionPercentage", "indicator", "resolution");
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		if (!trackingLog.isDraftMode())
			super.state(trackingLog.isDraftMode(), "*", "assistanceAgent.trackingLog.form.error.draftMode");
	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		Date currentMoment;

		currentMoment = MomentHelper.getCurrentMoment();

		trackingLog.setUpdateMoment(currentMoment);
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;
		SelectChoices indicators;

		dataset = super.unbindObject(trackingLog, "updateMoment", "step", "resolutionPercentage", "indicator", "resolution");
		indicators = SelectChoices.from(TrackingLogIndicator.class, trackingLog.getIndicator());
		dataset.put("indicators", indicators);

		super.getResponse().addData(dataset);
	}
}
