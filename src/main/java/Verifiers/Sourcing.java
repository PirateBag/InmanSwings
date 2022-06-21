package Verifiers;

import Application.ScreenStateService;
import com.inman.entity.Item;

import javax.swing.*;

public class Sourcing extends DomainVerifier {
	public String defaultValue = Item.SOURCE_PUR;
	public Sourcing() {
		validationRules = new ValidationRules(
			"Sourcing", new String [] { Item.SOURCE_PUR, Item.SOURCE_MAN }, 3, 3, CaseConversion.UPPER,
				"   " );
		columnHeader = "Src";
		rowHeader = "Sourcing";
	}

	@Override
	public boolean verify(JComponent input) {
		ScreenStateService.getCurrentPanel().invalidate();
		ScreenStateService.getCurrentPanel().repaint();
		return validationRules.doesComponentObeyRules( input );
	}

	public JComboBox renderComponent( ) {
		var rValue = new JComboBox( validationRules.values );
		return rValue;
	}
}



