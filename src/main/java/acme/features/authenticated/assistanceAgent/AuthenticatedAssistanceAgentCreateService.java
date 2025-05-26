
package acme.features.authenticated.assistanceAgent;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.principals.Principal;
import acme.client.components.principals.UserAccount;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airports.Airline;
import acme.realms.assistanceAgents.AssistanceAgent;

@GuiService
public class AuthenticatedAssistanceAgentCreateService extends AbstractGuiService<Authenticated, AssistanceAgent> {

	// Internal State --------------------------------------------------------------------

	@Autowired
	private AuthenticatedAssistanceAgentRepository repository;

	// AbstractGuiService ----------------------------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Principal principal;

		principal = super.getRequest().getPrincipal();

		status = !principal.hasRealmOfType(AssistanceAgent.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AssistanceAgent assistanceAgent;
		int userAccountId;
		UserAccount userAccount;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		userAccount = this.repository.findUserAccountById(userAccountId);

		assistanceAgent = new AssistanceAgent();
		assistanceAgent.setUserAccount(userAccount);

		super.getBuffer().addData(assistanceAgent);
	}

	@Override
	public void bind(final AssistanceAgent assistanceAgent) {
		super.bindObject(assistanceAgent, "employeeCode", "languages", "airline", "moment", "bio", "salary", "photoLink");
	}

	@Override
	public void validate(final AssistanceAgent assistanceAgent) {

		if (!super.getBuffer().getErrors().hasErrors("employeeCode")) {
			AssistanceAgent existing;

			existing = this.repository.findAssistanceAgentByEmployeeCode(assistanceAgent.getEmployeeCode());
			super.state(existing == null, "employeeCode", "authenticated.assistanceAgent.form.error.duplicateCode");
		}
	}

	@Override
	public void perform(final AssistanceAgent assistanceAgent) {
		this.repository.save(assistanceAgent);
	}

	@Override
	public void unbind(final AssistanceAgent assistanceAgent) {
		Dataset dataset;
		SelectChoices airlinesChoices;
		Collection<Airline> airlines;

		airlines = this.repository.findAllAirlines();
		airlinesChoices = SelectChoices.from(airlines, "name", assistanceAgent.getAirline());

		dataset = super.unbindObject(assistanceAgent, "employeeCode", "languages", "moment", "bio", "salary", "photoLink");
		dataset.put("airlines", airlinesChoices);
		dataset.put("airline", airlinesChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
