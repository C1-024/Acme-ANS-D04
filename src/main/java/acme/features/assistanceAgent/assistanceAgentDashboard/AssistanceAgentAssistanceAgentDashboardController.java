
package acme.features.assistanceAgent.assistanceAgentDashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.forms.AssistanceAgentDashboard;
import acme.realms.assistanceAgents.AssistanceAgent;

@GuiController
public class AssistanceAgentAssistanceAgentDashboardController extends AbstractGuiController<AssistanceAgent, AssistanceAgentDashboard> {

	// Internal State --------------------------------------------------------------------

	@Autowired
	private AssistanceAgentAssistanceAgentDashboardShowService showService;

	// Constructors  ----------------------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);

	}

}
