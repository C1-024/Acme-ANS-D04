
package acme.features.assistanceAgent.claim;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.claims.Claim;
import acme.realms.AssistanceAgent;

@GuiController
public class AssistanceAgentClaimController extends AbstractGuiController<AssistanceAgent, Claim> {

	// Internal State --------------------------------------------------------------------

	@Autowired
	private AssistanceAgentClaimListCompletedService	listCompletedService;

	@Autowired
	private AssistanceAgentClaimListUndergoingService	listUndergoingService;

	// Constructors  ----------------------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addCustomCommand("list-mine-completed", "list", this.listCompletedService);
		super.addCustomCommand("list-mine-undergoing", "list", this.listUndergoingService);
	}

}
