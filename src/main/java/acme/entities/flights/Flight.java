
package acme.entities.flights;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.realms.managers.Manager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	// Serialisation version ---------------------------------------------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes --------------------------------------------------------------------------------------------------

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				tag;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				indication;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				cost;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				draftMode;

	// Derived attributes -----------------------------------------------------

	//	private List<Leg>			legs;
	//
	//
	//	@Transient
	//	public Date getScheduledDeparture() {
	//		return this.legs.stream().min(Comparator.comparing(Leg::getScheduledDeparture)).map(Leg::getScheduledDeparture).orElse(null);
	//	}
	//
	//	@Transient
	//	public Date getScheduledArrival() {
	//		return this.legs.stream().max(Comparator.comparing(Leg::getScheduledArrival)).map(Leg::getScheduledArrival).orElse(null);
	//	}

	//	@Transient
	//	public String getOriginCity() {
	//		return this.legs.stream().min(Comparator.comparing(Leg::getScheduledDeparture)).map(Leg::getDepartureAirport).orElse(null);
	//	}
	//
	//	@Transient
	//	public String getDestinationCity() {
	//		return this.legs.stream().max(Comparator.comparing(Leg::getScheduledArrival)).map(Leg::getArrivalAirport).orElse(null);
	//	}
	//	@Transient
	//	public int getNumberOfLayovers() {
	//		return Math.max(this.legs.size() - 1, 0);
	//	}

	// Relationships ----------------------------------------------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Manager				manager;

}
