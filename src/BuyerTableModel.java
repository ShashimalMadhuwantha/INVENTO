import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class BuyerTableModel extends AbstractTableModel {
    private List<Buyer> buyers;
    private final String[] columnNames = {"Buyer ID", "First Name", "Last Name", "Phone Number"};

    public BuyerTableModel() {
        this.buyers = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return buyers.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Buyer buyer = buyers.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return buyer.getBuyerID();
            case 1:
                return buyer.getFirstName();
            case 2:
                return buyer.getLastName();
            case 3:
                return buyer.getPhoneNumber();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setBuyers(List<Buyer> buyers) {
        this.buyers = buyers;
        fireTableDataChanged();
    }

    public Buyer getBuyerAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < buyers.size()) {
            return buyers.get(rowIndex);
        } else {
            return null;
        }
    }
}
