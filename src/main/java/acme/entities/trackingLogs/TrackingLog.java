
package acme.entities.trackingLogs;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidScore;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidTrackingLog;
import acme.entities.claims.Claim;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidTrackingLog
public class TrackingLog extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true)
	private Date				updateMoment;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				step;

	@Mandatory
	@ValidScore
	@Automapped
	private double				resolutionPercentage;

	@Mandatory
	@Automapped
	private TrackingLogStatus	status;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				resolution;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				creationMoment;

	// Relationships -------------------------------------------------------------

	@Valid
	@ManyToOne(optional = false)
	private Claim				claim;
}
