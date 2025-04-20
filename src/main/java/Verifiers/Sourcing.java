package Verifiers;

import Application.ScreenStateService;
import Application.Utility;
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
	public JTextField getJTextField() {
		JTextField newTextField = super.getJTextField();
		newTextField.setText(  defaultValue );
		return newTextField;
	}

	public JComboBox<String> getJComboBox() {
		JComboBox rValue = Utility.createCombobox( this );
		JTextField newTextField = super.getJTextField();
		newTextField.setText(  defaultValue );
		return rValue;
	}


	@Override
	public boolean verify(JComponent input) {
		ScreenStateService.getCurrentPanel().invalidate();
		ScreenStateService.getCurrentPanel().repaint();
		return validationRules.doesComponentObeyRules( input );
	}


}



