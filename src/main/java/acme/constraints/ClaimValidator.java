
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.claims.Claim;
import acme.entities.flights.Leg;

@Validator
public class ClaimValidator extends AbstractValidator<ValidClaim, Claim> {

	@Override
	protected void initialise(final ValidClaim annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Claim claim, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (claim == null)
			super.state(context, false, "*", "acme.validation.NotNull.message");
		else if (claim.getLeg() != null) {
			boolean checkLeg;
			Leg claimLeg = claim.getLeg();
			checkLeg = MomentHelper.compare(MomentHelper.getCurrentMoment(), claimLeg.getScheduledArrival()) > 0 && claimLeg.getDraftMode().equals(false);

			super.state(context, checkLeg, "leg", "acme.validation.claim.leg.draftModeMoment");
		}

		result = !super.hasErrors(context);
		return result;
	}

}
