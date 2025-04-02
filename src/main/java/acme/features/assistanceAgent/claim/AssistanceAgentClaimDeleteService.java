
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Principal;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.ClaimStatus;
import acme.entities.claims.ClaimType;
import acme.entities.flights.Leg;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimDeleteService extends AbstractGuiService<AssistanceAgent, Claim> {

	// Internal State --------------------------------------------------------------------

	@Autowired
	private AssistanceAgentClaimRepository repository;

	// AbstractGuiService ----------------------------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int currentAssistanceAgentId;
		int claimId;
		Claim selectedClaim;
		Principal principal;

		principal = super.getRequest().getPrincipal();

		currentAssistanceAgentId = principal.getActiveRealm().getId();

		claimId = super.getRequest().getData("id", int.class);
		selectedClaim = this.repository.findClaimById(claimId);

		status = principal.hasRealmOfType(AssistanceAgent.class) && selectedClaim.getAssistanceAgent().getId() == currentAssistanceAgentId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		int claimId;

		claimId = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(claimId);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		int legId;

		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);
		claim.setLeg(leg);
		super.bindObject(claim, "passengerEmail", "description", "type", "indicator");
	}

	@Override
	public void validate(final Claim claim) {
		if (!super.getBuffer().getErrors().hasErrors("draftMode"))
			super.state(claim.isDraftMode(), "*", "assistanceAgent.claim.form.error.draftMode");
	}

	@Override
	public void perform(final Claim claim) {
		Collection<TrackingLog> trackingLogs;

		trackingLogs = this.repository.findAllTrackingLogsByClaimId(claim.getId());

		this.repository.deleteAll(trackingLogs);
		this.repository.delete(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		SelectChoices types;
		SelectChoices indicators;
		SelectChoices legsChoices;

		Collection<Leg> legs;
		legs = this.repository.findAllLegsNotPublished();

		types = SelectChoices.from(ClaimType.class, claim.getType());
		indicators = SelectChoices.from(ClaimStatus.class, claim.getIndicator());
		legsChoices = SelectChoices.from(legs, "flightNumber", claim.getLeg());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "indicator");
		dataset.put("types", types);
		dataset.put("indicators", indicators);
		dataset.put("legs", legsChoices);
		dataset.put("leg", legsChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
