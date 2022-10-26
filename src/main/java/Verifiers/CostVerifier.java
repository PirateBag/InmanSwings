package Verifiers;

import Application.ScreenStateService;

import javax.swing.*;
import java.util.Optional;

public class CostVerifier extends DomainVerifier {
	public static Double defaultValue = 0.0;

	private void commonInit( String xColumnHeader, String xRowHeader ) {
		validationRules = new ValidationRules(
				"Unit Cost", (Double) 0.0, Double.MAX_VALUE, Optional.of( (Object) defaultValue )  );
		columnHeader = xColumnHeader;
		rowHeader = xRowHeader;
	}

	public CostVerifier() {
		commonInit( "Cost", "Cost" );
	}

	public CostVerifier( String xColumnHeader, String xRowHeader ) {
		commonInit(  xColumnHeader, xRowHeader) ;
	}


	@Override
	public boolean verify(JComponent input) {
		ScreenStateService.getCurrentPanel().invalidate();
		ScreenStateService.getCurrentPanel().repaint();
		return validationRules.doesComponentObeyRules( input );
	}
}



