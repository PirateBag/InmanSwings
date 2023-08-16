package Buttons;

import javax.swing.*;

public class SearchButton extends JButton {
    Icon searchIcon = new ImageIcon( "gui/icons/search_FILL0_wght400_GRAD0_opsz48.png" );
    public SearchButton() {
        super();
        this.setIcon( searchIcon );
        this.setDefaultCapable(true);
        this.setToolTipText("Search");
    }
}
