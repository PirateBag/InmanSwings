package Buttons;

import javax.swing.*;

public class LoginButton extends JButton {
    Icon loginIcon = new ImageIcon( "gui/icons/login_FILL0_wght400_GRAD0_opsz48.png" );
    public LoginButton() {
        super();
        this.setIcon( loginIcon );
        this.setDefaultCapable(true);
        this.setToolTipText("Login");
    }
}
