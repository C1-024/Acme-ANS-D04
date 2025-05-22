
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.StringHelper;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogStatus;

@Validator
public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	@Override
	protected void initialise(final ValidTrackingLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (trackingLog == null)
			super.state(context, false, "*", "acme.validation.NotNull.message");
		else {
			// Comprobamos que el campo resolution esté relleno cuando tengamos un tracking log al 100%
			{
				boolean validResolution = true;

				if (trackingLog.getResolutionPercentage() == 100.00)
					validResolution = !StringHelper.isBlank(trackingLog.getResolution());

				super.state(context, validResolution, "resolution", "acme.validation.trackinLog.validResolution.required");
			}
			// Comprobamos que el porcentaje de resolución sea del 100% cuando se haya completado un tracking log.
			{
				boolean checkCompleted = true;
				boolean hasCompletedResolutionPercentage = trackingLog.getResolutionPercentage() == 100.00;
				boolean hasCompletedStatus = trackingLog.getStatus() == TrackingLogStatus.ACCEPTED || trackingLog.getStatus() == TrackingLogStatus.REJECTED;

				if (hasCompletedResolutionPercentage)
					checkCompleted = hasCompletedStatus;
				else if (hasCompletedStatus)
					checkCompleted = hasCompletedResolutionPercentage;

				super.state(context, checkCompleted, "resolutionPercentage", "acme.validation.trackingLog.status.checkCompleted");
			}
			// Comprobamos que el porcentaje de resolución sea menor de 100% cuando la el registro de seguimiento esté pendiente
			{
				boolean checkPending = true;
				boolean notCompleted = trackingLog.getResolutionPercentage() < 100.00;
				boolean isPending = trackingLog.getStatus() == TrackingLogStatus.PENDING;

				if (isPending)
					checkPending = notCompleted;
				else if (notCompleted)
					checkPending = isPending;

				super.state(context, checkPending, "resolutionPercentage", "acme.validation.trackingLog.status.checkPending");
			}
			// Comprobamos que el claim perteneciente al tracking log esté publicado si el tracking log está publicado también.
			{
				if (trackingLog.getClaim() != null) {
					boolean checkTrackingLog = trackingLog.isDraftMode() || !trackingLog.getClaim().isDraftMode();

					super.state(context, checkTrackingLog, "draftMode", "acme.validation.trackingLog.draftMode.checkTrackingLog");
				}

			}

		}

		result = !super.hasErrors(context);
		return result;
	}
}
