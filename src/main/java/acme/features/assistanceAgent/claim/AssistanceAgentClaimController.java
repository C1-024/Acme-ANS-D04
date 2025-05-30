
package acme.features.assistanceAgent.claim;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.claims.Claim;
import acme.realms.assistanceAgents.AssistanceAgent;

@GuiController
public class AssistanceAgentClaimController extends AbstractGuiController<AssistanceAgent, Claim> {

	// Internal State --------------------------------------------------------------------

	@Autowired
	private AssistanceAgentClaimListMineCompletedService	listCompletedService;

	@Autowired
	private AssistanceAgentClaimListMineUndergoingService	listUndergoingService;

	@Autowired
	private AssistanceAgentClaimShowService						showService;
	@Autowired
	private AssistanceAgentClaimCreateService					createService;

	@Autowired
	private AssistanceAgentClaimDeleteService					deleteService;

	@Autowired
	private AssistanceAgentClaimUpdateService					updateService;

	@Autowired
	private AssistanceAgentClaimPublishService					publishService;

	// Constructors  ----------------------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("update", this.updateService);

		super.addCustomCommand("list-mine-completed", "list", this.listCompletedService);
		super.addCustomCommand("list-mine-undergoing", "list", this.listUndergoingService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
