
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.assistanceAgents.AssistanceAgent;
import acme.realms.assistanceAgents.AssistanceAgentRepository;

@Validator
public class AssistanceAgentValidator extends AbstractValidator<ValidAssistanceAgent, AssistanceAgent> {

	@Autowired
	private AssistanceAgentRepository repository;


	@Override
	public boolean isValid(final AssistanceAgent assistanceAgent, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (assistanceAgent == null)
			super.state(context, false, "*", "acme.validation.NotNull.message");
		else {
			//			Comprobar employee code con las dos primeras letras iniciales del nombre.

			DefaultUserIdentity userIdentity = assistanceAgent.getIdentity();

			String name = userIdentity.getName();
			String surname = userIdentity.getSurname();

			if (name != null || !name.isEmpty() || surname != null || !surname.isEmpty()) {
				char nameInitial = Character.toUpperCase(name.trim().charAt(0));
				char surnameInitial = Character.toUpperCase(surname.trim().charAt(0));

				String employeeCode = assistanceAgent.getEmployeeCode();

				boolean checkInitials = employeeCode.charAt(0) == nameInitial && employeeCode.charAt(1) == surnameInitial;

				super.state(context, checkInitials, "employeeCode", "acme.validation.assistanceAgent.employeeCode.initials");
			}

		}

		result = !super.hasErrors(context);

		return result;
	}

}
