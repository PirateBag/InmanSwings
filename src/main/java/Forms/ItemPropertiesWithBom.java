package Forms;

import Application.*;
import Buttons.*;
import Verifiers.*;
import com.inman.entity.ActivityState;
import com.inman.entity.BomPresent;
import com.inman.entity.Item;
import com.inman.model.request.BomSearchRequest;
import com.inman.model.request.ItemReportRequest;
import com.inman.model.response.BomResponse;
import com.inman.model.response.ItemExplosionResponse;
import com.inman.model.response.ItemResponse;
import com.inman.model.response.ResponsePackage;
import com.inman.model.rest.ErrorLine;
import com.inman.model.rest.ItemAddRequest;
import com.inman.model.rest.ItemUpdateRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Optional;

import static Application.ScreenTransitionType.POP;

public class ItemPropertiesWithBom extends InmanPanelV2 {
    private class ItemExplosionActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ItemExplosionResponse responsePackage = null;

            var itemReportRequest = new ItemReportRequest(  Long.parseLong(id.getText() ) );

            try {
                errorText.clearError();
                String completeUrl = "http://localhost:8080/" + ItemReportRequest.EXPLOSION_URL;
                RestTemplate restTemplate = new RestTemplate();
                responsePackage = restTemplate.postForObject(completeUrl, itemReportRequest, ItemExplosionResponse.class);
            } catch (Exception e1) {
                errorText.signalError(e1.toString());
            }

            if (responsePackage != null && !responsePackage.getErrors().isEmpty()) {
                errorText.signalError(( responsePackage.getErrors().get(0)).getMessage());
            }
            ScreenStateService.evaluate(new NextAction(
                    "Show Report Results",
                    ScreenTransitionType.PUSH,
                    TextResults.class,
                    responsePackage,
                    null,
                    ScreenMode.DISPLAY));
        }
    }

    private class AddButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            BomPresent emptyNewElement = new BomPresent();
            emptyNewElement.setParentId( Long.valueOf( id.getText() ) );
            emptyNewElement.setParentSummary( summaryId.getText() );
            emptyNewElement.setParentDescription( description.getText() );
            emptyNewElement.setChildId( 999 );
            emptyNewElement.setQuantityPer( 1.0 );
            emptyNewElement.setChildDescription( "No child selected" );
            emptyNewElement.setChildSummary( "No child selected");
            emptyNewElement.setParentId(   Long.parseLong(id.getText()));
            emptyNewElement.setUnitCost( 0.0 );
            emptyNewElement.setActivityState( ActivityState.INSERT );

            ResponsePackage<BomPresent> packageToPassToProperties = new ResponsePackage<>();
            BomPresent[ ] newElementArray  = new BomPresent[ 1 ];
            newElementArray[ 0 ] = emptyNewElement;

            packageToPassToProperties.setData( newElementArray );
            ScreenStateService.evaluate(new NextAction(
                    "Insert Component",
                    ScreenTransitionType.PUSH,
                    BomProperties.class,
                    packageToPassToProperties,
                    null,
                    ScreenMode.ADD));
        }
    }

    NextAction action;

    JTextField id = Utility.createTextField("Id");
    JTextField summaryId = VerifierLibrary.summaryIdVerifier.getJTextField();
    JTextField description = VerifierLibrary.descriptionVerifier.getJTextField();
    JTextField unitCost = VerifierLibrary.costVerifier.getJTextField( "Unit Cost");
    JComboBox sourcing  =  VerifierLibrary.sourcing.getJComboBox();
    JButton saveButton = new SaveButton();
    JButton exitButton = new CancelWithoutActionButton();
    JButton spacer1 = new Spacer();
    JButton addButton = new AddButton( );
    JButton spacer2 = new Spacer();
    JButton itemExplosionButton = new ReportButton( "Item Explosion Report",
            new ItemExplosionActionListener() );

    JLabel componentsHeader;
    BomChildGrid bomChildGrid;

    BomResponse componentResponse = null;
    JTable bomChildTable;

    public ItemPropertiesWithBom() {

        add(errorText);

        add(Utility.labelMaker(" ", JLabel.TRAILING),
                BorderLayout.LINE_START);
        add(summaryId);
        add(description);
        add(unitCost);
        add(sourcing);

        add(Utility.labelMaker(" ", JLabel.TRAILING),
                BorderLayout.LINE_START);

        buttonPanel.add( saveButton );
        buttonPanel.add( exitButton );
        buttonPanel.add( spacer1 );
        buttonPanel.add( addButton );
        buttonPanel.add( spacer2 );
        buttonPanel.add( itemExplosionButton );

        String[] columnNames = {
                VerifierLibrary.childIdVerifier.getColumnHeader(),
                VerifierLibrary.summaryIdVerifier.getColumnHeader(),
                VerifierLibrary.descriptionVerifier.getColumnHeader(),
                VerifierLibrary.costVerifier.getColumnHeader(),
                VerifierLibrary.quanityPerVerifier.getColumnHeader(),
                VerifierLibrary.costVerifier.getColumnHeader()
        };
        bomChildGrid = new BomChildGrid(columnNames, this  );
        bomChildTable = new JTable(bomChildGrid);

        componentsHeader = Utility.subTitleMaker("Components", JLabel.TRAILING);
        add(componentsHeader);
        add(new JScrollPane(bomChildTable));

        showComponents();
        ScreenStateService.primaryPanel.add(this);

        //noinspection Convert2Lambda
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ItemResponse responsePackage = null;

                if (action.getScreenMode() == ScreenMode.ADD) {
                    var itemAddRequest = new ItemAddRequest(
                            summaryId.getText(), description.getText(), Double.parseDouble(unitCost.getText()),
                            (String) sourcing.getSelectedItem() );

                    var errorMessages = itemAddRequestVerifier(itemAddRequest);
                    if (errorMessages.isPresent()) {
                        errorText.signalError(errorMessages.get());
                    } else {
                        try {
                            String completeUrl = "http://localhost:8080/" + ItemAddRequest.addUrl;
                            RestTemplate restTemplate = new RestTemplate();
                            responsePackage = restTemplate.postForObject(completeUrl, itemAddRequest, ItemResponse.class);
                            errorText.clearError();
                        } catch (Exception e1) {
                            errorText.signalError(e1.toString());
                        }
                    }

                    if ((responsePackage != null) && (responsePackage.getErrors().size() > 0)) {
                        ArrayList<ErrorLine> errorLines = responsePackage.getErrors();
                        errorText.signalError(errorLines.get(0).getMessage());
                    }

                    if (errorText.hasNoError()) {
                        ScreenStateService.evaluate(new NextAction(
                                "Added Item " + itemAddRequest.getSummaryId(), POP, responsePackage));
                    }
                } else if (action.getScreenMode() == ScreenMode.CHANGE) {
                    var itemUpdateRequest = new ItemUpdateRequest(
                            Integer.valueOf(id.getText()), summaryId.getText(), description.getText(), Double.parseDouble(unitCost.getText()),
                            (String) sourcing.getSelectedItem());

                    var errorMessages = itemUpdateRequestVerifier( itemUpdateRequest );
                    if (errorMessages.isPresent()) {
                        errorText.signalError(errorMessages.get());
                    } else {
                        try {
                            String completeUrl = "http://localhost:8080/" + ItemUpdateRequest.updateUrl;
                            RestTemplate restTemplate = new RestTemplate();
                            responsePackage = restTemplate.postForObject(completeUrl, itemUpdateRequest, ItemResponse.class);
                            errorText.clearError();
                        } catch (Exception e1) {
                            errorText.signalError(e1.toString());
                        }
                    }

                    if ((responsePackage != null) && (responsePackage.getErrors().size() > 0)) {
                        ArrayList<ErrorLine> errorLines = responsePackage.getErrors();
                        errorText.signalError(errorLines.get(0).getMessage());
                    }

                    if (errorText.hasNoError()) {
                        ScreenStateService.evaluate(new NextAction(
                                "Added Item " + itemUpdateRequest.getSummaryId(), POP, responsePackage));
                    }
                }
            } } );

        addButton.addActionListener( new AddButtonActionListener() );
    }
        /**
     * Set the visibility of the component list based on the existence of components.
     * Called when panel is constructed and whenever updated with components.
     */
    private void showComponents() {

        if (componentResponse == null || componentResponse.getData().length == 0) {
            componentsHeader.setText( "This Item has no components.");
            bomChildTable.setFillsViewportHeight(false);
            bomChildTable.setVisible(false);

           return;
        }
        bomChildGrid.recomputeParentUnitCost();
        componentsHeader.setText( "Components of Item.");
        bomChildTable.setFillsViewportHeight(false);
        bomChildTable.setVisible(true);
    }



    /**
     * Validate the fields of the ItemAddRequest, for use with Save button.
     *
     * @param itemAddRequest proposed ItemAddRequest
     * @return Optional error message text, or empty()
     */
    @SuppressWarnings("DuplicatedCode")
    private Optional<String> itemAddRequestVerifier(@NotNull ItemAddRequest itemAddRequest) {

        //  Multiple messages will likely be implemented as verification is extended to all fields.
        StringBuilder errorMessages = new StringBuilder();

        VerifierLibrary.summaryIdVerifier.validateValueDomain(errorMessages, itemAddRequest.getSummaryId());
        VerifierLibrary.descriptionVerifier.validateValueDomain(errorMessages, itemAddRequest.getDescription());
        VerifierLibrary.costVerifier.validateValueDomain(errorMessages, itemAddRequest.getUnitCost());
        VerifierLibrary.sourcing.validateValueDomain(errorMessages, itemAddRequest.getSourcing());

        return errorMessages.toString().length() == 0 ? Optional.empty() : Optional.of(errorMessages.toString());
    }

    /**
     * Validate the fields of the ItemUpdateRequest, for use with Update button.
     *
     * @param xItemUpdateRequest Update Request
     * @return Optional error message text, or empty()
     */
    @SuppressWarnings("DuplicatedCode")
    private Optional<String> itemUpdateRequestVerifier(@NotNull ItemUpdateRequest xItemUpdateRequest) {

        //  Multiple messages will likely be implemented as verification is extended to all fields.
        StringBuilder errorMessages = new StringBuilder();

        VerifierLibrary.summaryIdVerifier.validateValueDomain(errorMessages, xItemUpdateRequest.getSummaryId());
        VerifierLibrary.descriptionVerifier.validateValueDomain(errorMessages, xItemUpdateRequest.getDescription());
        VerifierLibrary.costVerifier.validateValueDomain(errorMessages, xItemUpdateRequest.getUnitCost());
        VerifierLibrary.sourcing.validateValueDomain(errorMessages, xItemUpdateRequest.getSourcing());

        return errorMessages.toString().isEmpty() ? Optional.empty() : Optional.of(errorMessages.toString());
    }

    public void setDefaultValues() {
        this.errorText.clearError();
        this.description.setText(DescriptionVerifier.defaultValue);
        this.summaryId.setText(SummaryIdVerifier.defaultValue);
        this.unitCost.setText(Double.toString(CostVerifier.defaultValue));

    }

    @Override
    public void updateStateWhenOpeningNewChild(NextAction xAction) {
        action = xAction;
        errorText.clearError();
        switch (xAction.getScreenMode()) {
            case ADD:
                setDefaultValues();
                title.setText("Add Item");
                saveButton.setVisible(true);
//                  updateButton.setVisible(false);
                break;

            case CHANGE:
                setValuesFromAction();
                title.setText("Change Item Properties of " + summaryId.getText() );

                try {
                    String completeUrl = "http://localhost:8080/" + BomSearchRequest.findByParent;
                    RestTemplate restTemplate = new RestTemplate();
                    BomSearchRequest bomSearchRequest = new BomSearchRequest();
                    bomSearchRequest.setIdToSearchFor(Long.parseLong(id.getText()));

                    componentResponse = restTemplate.postForObject(completeUrl, bomSearchRequest, BomResponse.class);
                    errorText.clearError();

                } catch (Exception e1) {
                    errorText.signalError(e1.toString());
                }
                bomChildGrid.initializeRows(componentResponse.getData());
                showComponents();
                saveButton.setVisible( true );
    //              updateButton.setVisible(true);
                break;
        }

    }

    private void setValuesFromAction() {
        Item[] items = (Item[]) action.getResponsePackage().getData();
        Item itemToBeModified = null;
        for (var item : items) {
            if (item.getId() == action.getIdsToActOn()[0]) {
                itemToBeModified = item;
                break;
            }
        }

        if (itemToBeModified != null) {
            id.setText(Long.toString(itemToBeModified.getId()));
            summaryId.setText(itemToBeModified.getSummaryId());
            description.setText(itemToBeModified.getDescription());
            unitCost.setText(Double.toString(itemToBeModified.getUnitCost()));

            /*
            for (int i = 0; i < VerifierLibrary.sourcing.getValidationRules().values.length; i++) {
                if (itemToBeModified.getSourcing().equals( VerifierLibrary.sourcing.getValidationRules().values[i])) {
                    sourcing.setSelectedIndex(i);
                    break;
                }
            }  */
            id.setVisible(true);
        } else {
            id.setVisible(false);
        }
    }
}
