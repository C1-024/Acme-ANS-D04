
package acme.features.administrator.trackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.principals.Principal;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.trackingLogs.TrackingLog;

@GuiService
public class AdministratorTrackingLogListService extends AbstractGuiService<Administrator, TrackingLog> {

	// Internal State --------------------------------------------------------------------

	@Autowired
	private AdministratorTrackingLogRepository repository;

	// AbstractGuiService ----------------------------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Principal principal;

		principal = super.getRequest().getPrincipal();

		status = principal.hasRealmOfType(Administrator.class);

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

}
