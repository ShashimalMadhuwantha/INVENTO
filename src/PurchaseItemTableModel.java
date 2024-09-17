import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PurchaseItemTableModel extends AbstractTableModel {
    private final String[] columnNames = {"Purchase ID", "Buyer ID", "Item ID", "Date", "Quantity", "Total Price", "Status"};
    private List<PurchaseItem> items;

    public void setItems(List<PurchaseItem> items) {
        this.items = items;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        PurchaseItem item = items.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return item.getPurchaseID();
            case 1:
                return item.getBuyerID();
            case 2:
                return item.getItemID();
            case 3:
                return item.getPurchaseDate();
            case 4:
                return item.getQuantity();
            case 5:
                return item.getTotalPrice();
            case 6:
                return item.getStatus();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}