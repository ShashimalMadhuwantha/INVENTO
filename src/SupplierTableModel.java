import javax.swing.table.AbstractTableModel;
import java.util.List;

public class SupplierTableModel extends AbstractTableModel {
    private List<Supplier> suppliers;
    private String[] columnNames = {"Supplier ID", "Supplier Name", "Address", "Contact Number"};

    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
        fireTableDataChanged();
    }

    public Supplier getSupplierAt(int rowIndex) {
        return suppliers.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return suppliers == null ? 0 : suppliers.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Supplier supplier = suppliers.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return supplier.getId();
            case 1:
                return supplier.getName();
            case 2:
                return supplier.getAddress();
            case 3:
                return supplier.getContactNumber();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
