package Buttons;

import javax.swing.*;

public class SaveButton extends JButton {
    Icon icon = ButtonUtility.basicButtonScaler( "gui/icons/save_as_FILL0_wght400_GRAD0_opsz48.png" );

    private void sharedConstructorBehavior() {
        this.setIcon( icon );
        this.setDefaultCapable(false);
        this.setToolTipText( "Save" );

    }
    public SaveButton( ) {
        super();
        sharedConstructorBehavior();
    }

    public SaveButton(String toolTip ) {
        super();
        sharedConstructorBehavior();
        this.setToolTipText( toolTip );
    }
}
