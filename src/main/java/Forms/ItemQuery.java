package Forms;

import Application.Action;
import Application.*;
import Verifiers.CostVerifier;
import Verifiers.DescriptionVerifier;
import Verifiers.IdVerifier;
import Verifiers.SummaryIdVerifier;
import com.inman.business.QueryParameterException;
import com.inman.entity.Item;
import com.inman.model.response.ItemResponse;
import com.inman.model.response.ResponsePackage;
import com.inman.model.rest.SearchItemRequest;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;


public class ItemQuery extends InmanPanel {
    ResponsePackage responsePackage = new ResponsePackage();
    DefaultTableModel tableModel = new DefaultTableModel();
    IdVerifier idVerifier = new IdVerifier();
    SummaryIdVerifier summaryIdVerifier = new SummaryIdVerifier();
    DescriptionVerifier descriptionVerifier = new DescriptionVerifier();
    CostVerifier costVerifier = new CostVerifier();

    public ItemQuery() {
        responsePackage.setData( new Item[ 0 ]);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Utility.titleMaker("Search Parameters"), BorderLayout.PAGE_START);
        add(Utility.labelMaker(" ", JLabel.TRAILING),
                BorderLayout.LINE_START);

        JLabel errorMessage = Utility.createErrorMessage(JLabel.TRAILING);
        add(errorMessage);

        JTextField itemId = Utility.createTextField("Item Id");
        add(itemId);

        JTextField summaryId = Utility.createTextField("Summary Id");
        add(summaryId);

        JTextField description = Utility.createTextField("Description");
        add(description);

        add(Utility.labelMaker(" ", JLabel.TRAILING),
                BorderLayout.LINE_START);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        var searchButton = new JButton("Search");
        buttonPanel.add(searchButton);

        var exitButton = new JButton("Exit");
        buttonPanel.add(exitButton);

        var addButton = new JButton("Add");
        buttonPanel.add(addButton);

        var editButton = new JButton("Edit");
        buttonPanel.add(editButton);
        add(buttonPanel);

        add(Utility.labelMaker(" ", JLabel.TRAILING),
                BorderLayout.LINE_START);

        JTable queryResults = new JTable(tableModel);
        String[] columnNames = {idVerifier.getColumnHeader(),
                summaryIdVerifier.getColumnHeader(),
                descriptionVerifier.getColumnHeader(),
                costVerifier.getColumnHeader()};
        for (String columnName : columnNames) {
            tableModel.addColumn(columnName);
        }
        queryResults.setFillsViewportHeight(true);
        add(new JScrollPane(queryResults));

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SearchItemRequest searchItemRequest = new SearchItemRequest(
                            itemId.getText(), summaryId.getText(), description.getText());

                    String completeUrl = "http://localhost:8080/" + SearchItemRequest.queryUrl;
                    RestTemplate restTemplate = new RestTemplate();
                    String responseJson = restTemplate.postForObject(completeUrl, searchItemRequest, String.class);
                    responsePackage = restTemplate.postForObject(completeUrl, searchItemRequest, ItemResponse.class);

                    errorMessage.setText("");

                    createTableModelFromResponsePackage();
                } catch (QueryParameterException qfe) {
                    errorMessage.setText(qfe.getMessage());
                } catch (Exception e1) {
                    errorMessage.setText(e1.getMessage());
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Action action = new Action("Add new item", ScreenTransitionType.PUSH, FormsLibrary.getItemProperties(),
                        null, null, ScreenMode.ADD);

                ScreenStateService.evaluate(action);


            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var numberOfRows = queryResults.getSelectedRows();

                if (numberOfRows.length != 1) {
                    errorMessage.setText("Please select exactly one row for editing");
                    return;
                }

                Long selectedCellValue = (Long) queryResults.getValueAt(queryResults.getSelectedRow(), 0);
                Long[] selectValues = new Long[]{selectedCellValue};

                Action action = new Action("displayAndUpdate", ScreenTransitionType.PUSH, FormsLibrary.getItemProperties(),
                        responsePackage, selectValues, ScreenMode.CHANGE);
                ScreenStateService.evaluate(action);

            }
        });


        queryResults.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                Long selectedCellValue = 0L;
                if (queryResults.getRowCount() > 0 && queryResults.getSelectedRow() >= 0) {
                    selectedCellValue = (Long) queryResults.getValueAt(queryResults.getSelectedRow(), 0);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        ScreenStateService.primaryPanel.add(this);
    }

    /**
     * Create the table model based on response package.
     * No values are returned; side effect is the changes to the table model.
     */
    private void createTableModelFromResponsePackage() {
        tableModel.setRowCount(0);
        var numRows = responsePackage.getData().length;

        for (int row = 0; row < numRows; row++) {
            var item = (Item) responsePackage.getData()[row];
            tableModel.insertRow(row, new Object[]{item.getId(), item.getSummaryId(), item.getDescription(), item.getUnitCost() });
        }
    }

    @Override
    /**
     * Update the items associated with this screen based on the server response from the prior screen (ItemProperties).
     *
     * The updated or inserted item is added to the table model.
     *
     */
    public void updateStateWhenChildCloses(Action xAction) {
        if (xAction.getResponsePackage() == null) {
            /*  No response package may mean the cancel button was pushed.  */
            return;
        }
        var numRows = (responsePackage.getData() == null) ? 0 : responsePackage.getData().length;

        /*  In the future, we may find that we can get multiple updates.  */
        assert (1 != xAction.getResponsePackage().getData().length);

        Item newOrUpdatedItem = (Item) xAction.getResponsePackage().getData()[0];
        int row;
        for ( row = 0; row < numRows; row++) {
            var item = (Item) responsePackage.getData()[row];
            if (item.getId() == newOrUpdatedItem.getId()) {
                responsePackage.getData()[row] = newOrUpdatedItem;
                break;
            }
        }

        /*  If we ran through all the existing rows in the item response without finding a match,
        This must be a new row.
         */
        if ( row == numRows ) {

            Item [] newItems  = Arrays.copyOf( (Item []) responsePackage.getData(), responsePackage.getData().length+ 1 );
            newItems[responsePackage.getData().length ] = newOrUpdatedItem;
            responsePackage.setData( newItems );
        }
        createTableModelFromResponsePackage();
    }

}
