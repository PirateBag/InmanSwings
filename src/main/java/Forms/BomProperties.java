package Forms;

import Application.*;
import Buttons.PopWithoutActionButton;
import Buttons.SaveLargeButton;
import ISwing.IComboBox;
import Verifiers.VerifierLibrary;
import com.inman.entity.ActivityState;
import com.inman.entity.BomPresent;
import com.inman.lists.Items;
import com.inman.model.request.BomUpdate;
import com.inman.model.request.ItemPickListRequest;
import com.inman.model.response.BomResponse;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;


public class BomProperties extends InmanPanelV2 {
    Items itemPickListParent = new Items(ItemPickListRequest.GET_ONE_ITEM );
    Items itemPickListCandidateChildren = new Items(ItemPickListRequest.ITEMS_FOR_BOM_URL);
    BomPresent bom = new BomPresent();

    IComboBox parentDescription = new IComboBox(VerifierLibrary.descriptionVerifier, Optional.of("Parent"));
    IComboBox childDescription = new IComboBox(VerifierLibrary.descriptionVerifier, Optional.of("Child"));
    JTextField quantityPer = VerifierLibrary.quanityPerVerifier.getJTextField();

    JButton returnButton = new PopWithoutActionButton();
    JButton saveButton = new SaveLargeButton("Save and return.");

    private class DoneButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            var parentId = itemPickListParent.findIdByArrayIndex( parentDescription.getSelectedIndex());
            bom.setParentId( parentId );
            var childId = itemPickListCandidateChildren.findIdByArrayIndex( childDescription.getSelectedIndex());
            bom.setChildId(  childId );
            bom.setQuantityPer(Double.parseDouble(quantityPer.getText()));
            bom.setActivityState(ActivityState.INSERT);

            BomPresent[] componentsToBeUpdated = new BomPresent[1];
            componentsToBeUpdated[0] = bom;

            errorText.clearError();

            try {
                var completeUrl = "http://localhost:8080/" + BomUpdate.updateUrl;
                BomResponse componentResponse = restTemplate.postForObject(completeUrl, componentsToBeUpdated,BomResponse.class );

                ScreenStateService.evaluate(new NextAction(
                        "Return",
                        ScreenTransitionType.POP,
                        BomProperties.class,
                        componentResponse,
                        null,
                        ScreenMode.CHANGE));

            } catch (Exception e1) {
                errorText.signalError(e1.toString());
            }
        }
    }

    public BomProperties() {

        add(errorText);
        add(parentDescription);
        add(childDescription);
        add(quantityPer);

        saveButton.addActionListener(new DoneButtonListener());
        buttonPanel.add(saveButton);
        buttonPanel.add( returnButton );
        add(buttonPanel);
    }

    public void updateStateWhenOpeningNewChild(NextAction action) {
        errorText.clearError();
        title.setText( action.getActionName() );
        bom = (BomPresent) action.getResponsePackage().getData()[0];
        itemPickListParent.refreshFromServer(Optional.of(bom.getParentId()));
        itemPickListCandidateChildren.refreshFromServer(Optional.of(bom.getParentId()));

        parentDescription.refresh(itemPickListParent);
        int index = itemPickListParent.getIndexByid(bom.getParentId());
        parentDescription.setSelectedIndex(index);
        childDescription.refresh(itemPickListCandidateChildren);

        if ( !itemPickListCandidateChildren.isEmpty() ) {
            int selectedIndex = index == -1 ? 0 : index;
            childDescription.setSelectedIndex(selectedIndex);
            errorText.signalError( "No child items eligible for this parent");
        }
        quantityPer.setText(String.valueOf(bom.getQuantityPer()));

        this.invalidate();
    }
}