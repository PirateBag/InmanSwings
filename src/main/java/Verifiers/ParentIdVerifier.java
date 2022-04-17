package Verifiers;

import Application.ScreenStateService;

import javax.swing.*;


public class ParentIdVerifier extends IdVerifier {
	public ParentIdVerifier() {
		rowHeader = "Parent";
		columnHeader = "Parent";
	}

	@Override
	public boolean verify(JComponent xInput) {
		ScreenStateService.getCurrentPanel().invalidate();
		ScreenStateService.getCurrentPanel().repaint();

		return validationRules.doesComponentObeyRules( xInput );
	}
}



