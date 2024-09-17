import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ItemTableModel extends AbstractTableModel {
    private List<Item> items;
    private String[] columnNames = {"Item ID", "Name", "Description", "Price", "Quantity", "Supplier ID"};

    public ItemTableModel() {
        this.items = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Item item = items.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return item.getId();
            case 1:
                return item.getName();
            case 2:
                return item.getDescription();
            case 3:
                return item.getPrice();
            case 4:
                return item.getQuantity();
            case 5:
                return item.getSupplierID();
            default:
                throw new IllegalArgumentException("Invalid column index");
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setItems(List<Item> items) {
        this.items = items;
        fireTableDataChanged();
    }

    public Item getItemAt(int rowIndex) {
        return items.get(rowIndex);
    }
}
