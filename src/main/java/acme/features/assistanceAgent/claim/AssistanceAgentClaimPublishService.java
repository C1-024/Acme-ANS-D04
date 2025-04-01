
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
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimPublishService extends AbstractGuiService<AssistanceAgent, Claim> {

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
		int claimId;
		Claim claim;

		claimId = super.getRequest().getData("id", int.class);

		claim = this.repository.findClaimById(claimId);
		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {

		super.bindObject(claim, "passengerEmail", "description", "type", "indicator", "leg");
	}

	@Override
	public void validate(final Claim claim) {

		if (claim.getLeg() == null)
			super.state(claim.getLeg() != null, "leg", "assistanceAgent.claim.form.error.emptyLeg");

		if (!super.getBuffer().getErrors().hasErrors("draftMode"))
			super.state(claim.isDraftMode(), "draftMode", "assistanceAgent.claim.form.error.draftMode");

	}

	@Override
	public void perform(final Claim claim) {

		claim.setDraftMode(false);

		this.repository.save(claim);
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
