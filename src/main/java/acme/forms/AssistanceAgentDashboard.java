
package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import acme.client.components.validation.ValidScore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssistanceAgentDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@ValidScore
	private Double				resolvedClaimsRatio;

	@ValidScore
	private Double				rejectedClaimsRatio;

	private List<String>		topThreeMonthsClaims;

	private Double				avgTotalClaims;
	private Integer				minTotalClaims;
	private Integer				maxTotalClaims;
	private Double				stdevTotalClaims;

	private Double				avgLastMonthAssistedClaims;
	private Integer				minLastMonthAssistedClaims;
	private Integer				maxLastMonthAssistedClaims;
	private Double				stdevLastMonthAssistedClaims;

}
