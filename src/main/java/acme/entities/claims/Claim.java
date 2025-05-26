
package acme.entities.claims;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidClaim;
import acme.entities.flights.Leg;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogStatus;
import acme.realms.assistanceAgents.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidClaim
public class Claim extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@Mandatory
	@ValidEmail
	@Automapped
	private String				passengerEmail;

	@Mandatory
	@ValidString(max = 255, min = 1)
	@Automapped
	private String				description;

	@Mandatory
	@Automapped
	private ClaimType			type;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	// Relationships -------------------------------------------------------------

	@Valid
	@ManyToOne(optional = false)
	private AssistanceAgent		assistanceAgent;

	@Valid
	@ManyToOne(optional = false)
	private Leg					leg;


	@Transient
	public ClaimStatus getStatus() {
		ClaimStatus claimStatus = ClaimStatus.PENDING;
		ClaimRepository repository;
		List<TrackingLog> trackingLogs;

		repository = SpringHelper.getBean(ClaimRepository.class);
		trackingLogs = repository.findTrackingLogsByClaimIdOrderedByMoment(this.getId()).stream().toList();

		if (trackingLogs.isEmpty())
			return claimStatus;

		for (int i = 0; i < trackingLogs.size(); i++)
			if (trackingLogs.get(i).getStatus() == TrackingLogStatus.ACCEPTED) {
				claimStatus = ClaimStatus.ACCEPTED;
				break;
			} else if (trackingLogs.get(i).getStatus() == TrackingLogStatus.REJECTED) {
				claimStatus = ClaimStatus.REJECTED;
				break;
			} else
				claimStatus = ClaimStatus.PENDING;

		return claimStatus;
	}

}
