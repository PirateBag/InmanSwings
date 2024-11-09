package Forms;

import Application.*;
import Buttons.ClearButton;
import Buttons.DoneButton;
import ISwing.IComboBox;
import Verifiers.VerifierLibrary;
import com.inman.entity.ActivityState;
import com.inman.entity.BomPresent;
import com.inman.lists.Items;
import com.inman.model.request.BomUpdate;
import com.inman.model.response.BomResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;


public class BomProperties extends InmanPanel {
    static Logger logger = LoggerFactory.getLogger( "controller: " + BomProperties.class.getName() );
    Items itemPickList = new Items();
    BomPresent bom = new BomPresent();

    protected JLabel title = Utility.titleMaker("Where Used Properties ");

    IComboBox parentDescription = new IComboBox( VerifierLibrary.descriptionVerifier, Optional.of("Parent" ) );
    IComboBox childDescription = new IComboBox( VerifierLibrary.descriptionVerifier, Optional.of( "Child" ) );

    JTextField quantityPer = VerifierLibrary.quanityPerVerifier.getJTextField();

    JButton clearButton = new ClearButton( "Cancel changes and stay" );

    JButton saveButton = new DoneButton();
    JButton doneButton = new DoneButton();
    RestTemplate restTemplate = new RestTemplate();

    private class DoneButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            //  bom.setParentId( parentDescription.getSelectedIndex() );
            var selectedIndex = childDescription.getSelectedIndex() +1;
            bom.setChildId( selectedIndex );
            bom.setQuantityPer( Double.parseDouble( quantityPer.getText() ) );
            BomPresent [] componentsToBeUpdated = new BomPresent[1];
            bom.setActivityState( ActivityState.INSERT );
            componentsToBeUpdated[0] = bom;
            errorText.clearError();
            try {
            var completeUrl = "http://localhost:8080/" + BomUpdate.updateUrl;
            var componentResponse = restTemplate.postForObject(completeUrl, componentsToBeUpdated, BomResponse.class);

        } catch (Exception e1) {
            errorText.signalError(e1.toString());
        }

            ScreenStateService.evaluate(new NextAction(
                    "Return",
                    ScreenTransitionType.POP,
                    BomProperties.class,
                    null,
                    null,
                    ScreenMode.CHANGE));
        }
    }

    public BomProperties() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Utility.labelMaker(" ", JLabel.TRAILING),
                BorderLayout.LINE_START);
        setBorder(Utility.blackLine);

        add(title, BorderLayout.LINE_START);

        add(Utility.labelMaker(" ", JLabel.TRAILING),
                BorderLayout.LINE_START);


        errorText.clearError();
        add(errorText);

        add( parentDescription );
        add( childDescription );

        add( quantityPer );

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        saveButton.addActionListener( new DoneButtonListener() );
        clearButton.addActionListener( new DoneButtonListener() );
        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);
        add(buttonPanel);

    }

    public void updateStateWhenOpeningNewChild(NextAction action ) {
        itemPickList.refreshFromServer();
        bom  = (BomPresent) action.getResponsePackage().getData()[ 0 ];
        bomPresentToFields();
       }

    private void bomPresentToFields() {
        int index ;
        parentDescription.refresh( itemPickList );
        index = itemPickList.getIndexByid( bom.getParentId() );
        parentDescription.setSelectedIndex( index );
        childDescription.refresh( itemPickList );
        int selectedIndex = index == -1 ? 0 : index;
        childDescription.setSelectedIndex( selectedIndex );
        logger.info( "indexOfChildId is " + selectedIndex );
        quantityPer.setText(String.valueOf( bom.getQuantityPer()));
        errorText.clearError();
        this.invalidate();
    }
}