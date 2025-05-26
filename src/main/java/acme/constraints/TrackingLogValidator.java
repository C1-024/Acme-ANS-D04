
package acme.constraints;

import java.util.Comparator;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.StringHelper;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogRepository;
import acme.entities.trackingLogs.TrackingLogStatus;

@Validator
public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	@Autowired
	private TrackingLogRepository repository;


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

				super.state(context, checkPending, "resolutionPercentage", "acme.validation.trackingLog.resolutionPercentage.checkPending");
			}
			// Comprobamos que el claim perteneciente al tracking log esté publicado si el tracking log está publicado también.
			{
				if (trackingLog.getClaim() != null) {
					boolean checkTrackingLog = trackingLog.isDraftMode() || !trackingLog.getClaim().isDraftMode();

					super.state(context, checkTrackingLog, "draftMode", "acme.validation.trackingLog.draftMode.checkTrackingLog");
				}

			}
			// Comprobamos que no hayan más de 2 tracking logs completados y que el resolutionPercentage crezca monótonamemte
			{
				List<TrackingLog> claimTrackingLogs;
				boolean checkCompleted;

				claimTrackingLogs = this.repository.findAllByClaimId(trackingLog.getClaim().getId());

				// Comprobamos que tenemos el tracking log asociado a nuestro claim. 
				if (claimTrackingLogs.contains(trackingLog)) {
					int i;
					i = claimTrackingLogs.indexOf(trackingLog);

					claimTrackingLogs.set(i, trackingLog);
				} else
					claimTrackingLogs.add(trackingLog);

				// Ordenamos por fecha de creación para poder saber cual se ha creado antes
				claimTrackingLogs.sort(Comparator.comparing(t -> t.getCreationMoment()));

				// Nos quedamos con los completados
				claimTrackingLogs.removeIf(t -> t.getResolutionPercentage() != 100.00);

				// Solo puede haber un máximo de 2 tracking logs completados.
				checkCompleted = claimTrackingLogs.size() <= 2;

				super.state(context, checkCompleted, "status", "acme.validation.trackingLog.status.checkMaxCompleted");

				// Aquí comprobamos que los tracking logs crezcan monótonamente
				for (int i = 0; i < claimTrackingLogs.size(); i++) {
					TrackingLog currentTrackingLog;
					TrackingLog nextTrackingLog;

					currentTrackingLog = claimTrackingLogs.get(i);

					// Comprobamos que existan más tracking logs en la lista y seleccionamos el siguiente.
					if (claimTrackingLogs.size() > i + 1) {
						nextTrackingLog = claimTrackingLogs.get(i + 1);

						if (currentTrackingLog.getResolutionPercentage() == 100.00 && nextTrackingLog.getResolutionPercentage() == 100.00)
							// Comprobamos que el tracking log anterior se haya publicado
							super.state(context, !currentTrackingLog.isDraftMode(), "draftMode", "acme.validation.trackingLog.draftMode.checkPublished");
						else {
							// TODO: Arreglar esto
							// Comprobamos que el resolutionPercentage del siguiente sea mayor que el anterior (están ordenador por fecha)
							boolean growingPercentage = nextTrackingLog.getResolutionPercentage() > currentTrackingLog.getResolutionPercentage();
							super.state(context, !growingPercentage, "resolutionPercentage", "acme.validation.trackingLog.resolutionPercentage.growingPercentage");
						}

					}

				}

			}

		}

		result = !super.hasErrors(context);
		return result;
	}
}
