
package acme.entities.flights;

import java.beans.Transient;
import java.time.Duration;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Leg extends AbstractEntity {

	// Serialisation version ---------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Pattern(regexp = "^[A-Z]{2,3}\\d{4}$")
	@Column(unique = true)
	private String				flightNumber;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledArrival;

	@Mandatory
	@Valid
	@Automapped
	private StatusLeg			status;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				draftMode;

	// Derived attributes -----------------------------------------------------------------------------------------


	@Transient
	public int duration() {
		long durationInMinutes = Duration.between(this.scheduledDeparture.toInstant(), this.scheduledArrival.toInstant()).toMinutes();
		return (int) Math.ceil(durationInMinutes / 60.0);
	}


	// Relationships ----------------------------------------------------------------------------------------------
	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight flight;

	//	@Mandatory
	//	@Valid
	//	@ManyToOne(optional = false)
	//	private Airport		departureAirport;
	//
	//	@Mandatory
	//	@Valid
	//	@ManyToOne(optional = false)
	//	private Airport		arrivalAirport;
	//
	//	@Mandatory
	//	@Valid
	//	@ManyToOne(optional = false)
	//	private Aircraft	aircraft;
}
