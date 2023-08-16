package Forms;

import com.inman.entity.ActivityState;
import com.inman.entity.BomPresent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.util.Arrays;


public class BomChildGrid extends AbstractTableModel {
    static Logger logger = LoggerFactory.getLogger( "controller: " + BomChildGrid.class );

    private String[] columnNames;
    private Object[][] data;

    private final ItemPropertiesWithBom itemPropertiesWithBom;



    public BomChildGrid(String [] xColumnNames, ItemPropertiesWithBom xItemPropertiesWithBom ) {
        columnNames = xColumnNames;
        itemPropertiesWithBom = xItemPropertiesWithBom;


        addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                var updatedRow = getWhileRowAt(e.getFirstRow());
                updatedRow.setActivityState(ActivityState.CHANGE);
                recomputeParentUnitCost();
                xItemPropertiesWithBom.componentResponse.getData()[e.getFirstRow()] = updatedRow;

            }
        });

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
            BomPresent bom = getWhileRowAt( row );
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

    public BomPresent getWhileRowAt( int row ) {
        var bom = new BomPresent( this.itemPropertiesWithBom.componentResponse.getData()[ row ] );

        bom.setChildId( (Long) data[ row ][ 0 ]) ;
        bom.setChildSummary( data[ row ][ 1 ] );
        bom.setChildDescription( data[ row][ 2 ] );
        bom.setUnitCost((Double) data[ row ][ 3 ]);
        bom.setQuantityPer((Double) data[ row ][ 4 ]);
        //  Extended cost is omitted, as the getter calculates it.
        return bom;
    }

    public void recomputeParentUnitCost() {
        Double parentUnitCost = 0.0;
        for (int index = 0; index < data.length; index++) {
            var childRow = getWhileRowAt( index );

            parentUnitCost += childRow.getExtendedCost();
      }
        logger.info( "Rolled up " + data.length + " children to get cost of " + parentUnitCost );
        itemPropertiesWithBom.unitCost.setText( Double.toString( parentUnitCost ) );
    }
}
