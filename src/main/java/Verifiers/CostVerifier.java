package Verifiers;

import Application.ScreenStateService;

import javax.swing.*;
import java.util.Optional;

public class CostVerifier extends DomainVerifier {
	public static Double defaultValue = 0.0;

	public CostVerifier() {
		validationRules = new ValidationRules(
				"Unit Cost", (Double) 0.0, Double.MAX_VALUE, Optional.of( (Object) defaultValue )  );
		columnHeader = "Cost";
		rowHeader = "Cost";

	}

	@Override
	public boolean verify(JComponent input) {
		ScreenStateService.getCurrentPanel().invalidate();
		ScreenStateService.getCurrentPanel().repaint();
		return validationRules.doesComponentObeyRules( input );
	}
}



