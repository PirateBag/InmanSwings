package Forms;

import com.inman.entity.BomPresent;

import javax.swing.table.AbstractTableModel;
import java.util.Arrays;

public class BomChildGrid extends AbstractTableModel {
    private String[] columnNames;
    private Object[][] data;

    public BomChildGrid(String [] xColumnNames ) {
        columnNames = xColumnNames;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        return (col == 4);
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;

        if ( col == 4 ) {
            BomPresent bom = columnsToObject( data[ row ] );
            data[row] = objectToColumns( bom );
        }

        fireTableCellUpdated(row, col);
    }

    public void setColumnNames( String[] xColumnNames ) { this.columnNames = xColumnNames; }

    public void appendRow( BomPresent xRowOfData ) {

        Object[][] newArray = new Object[ data.length+1 ][columnNames.length];
        for (int rowIndex = 0; rowIndex < data.length ; rowIndex ++ ) {
            newArray[ rowIndex ] = Arrays.copyOf( data[ rowIndex ], columnNames.length );
        }
        newArray[data.length ] = objectToColumns( xRowOfData );
        data = newArray;
    }

    public void initializeRows( BomPresent[] xRowsOfData ) {
        Object[][] newArray = new Object[ xRowsOfData.length ][columnNames.length];
        for (int rowIndex = 0; rowIndex < xRowsOfData.length ; rowIndex ++ ) {
            newArray[ rowIndex ] = objectToColumns( xRowsOfData[ rowIndex ] );
        }
        data = newArray;
    }

    public Object[] objectToColumns(BomPresent xBom ) {
        Object[] columns = new Object[ columnNames.length ];
        columns[ 0 ] = xBom.getChildId();
        columns[ 1 ] = xBom.getChildSummary();
        columns[ 2 ] = xBom.getChildDescription();
        columns[ 3 ] = xBom.getUnitCost();
        columns[ 4 ] = xBom.getQuantityPer();
        columns[ 5 ] = xBom.getExtendedCost();

        return columns;
    }

    public BomPresent columnsToObject( Object[] xData ) {
        var bom = new BomPresent();
        bom.setChildId((Long) xData[ 0 ]); ;
        bom.setChildSummary( xData[ 1 ] );
        bom.setChildDescription( xData[ 2 ] );
        bom.setUnitCost((Double) xData[ 3 ]);
        bom.setQuantityPer((Double) xData[ 4 ]);
        //  Extended cost is omitted, as the getter calculates it.
        return bom;
    }

    public BomPresent getWhileRowAt( int row ) {
        var bom = columnsToObject( data[ row ]);
        return bom;
    }
/*
    public void updateBom() {

    BomPresentResponse responsePackage = null;

    Bom = new ItemUpdateRequest(
            Long.parseLong(id.getText()), summaryId.getText(), description.getText(), Double.parseDouble(unitCost.getText()),
            (String) sourcing.getSelectedItem());

    var errorMessages = itemUpdateRequestVerifier( itemUpdateRequest);
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

                if (responsePackage != null && responsePackage.getErrors().size() > 0) {
        errorText.signalError(((ErrorLine) responsePackage.getErrors().get( 0 )).getMessage());
    }

                if (errorText.hasNoError()) {
        ScreenStateService.evaluate(new NextAction(
                "Updated Item " + itemUpdateRequest.getSummaryId(), responsePackage ));
    }
}
        });
*/
                }
