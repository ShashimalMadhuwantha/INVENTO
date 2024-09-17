import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PurchaseGUI extends JFrame {
    private PurchaseItemDAO purchaseItemDAO;
    private ItemDAO itemDAO;
    private JTextField buyerIDField;
    private JTextField itemIDField;
    private JTextField quantityField;
    private JTable purchaseTable;
    private JTable itemDetailsTable;
    private PurchaseItemTableModel tableModel;
    private ItemTableModel itemTableModel;
    private JLabel totalPendingLabel; // Label to show total pending amount

    public PurchaseGUI( int userId) {
        super("Purchasing");
        setLayout(new BorderLayout());
        purchaseItemDAO = new PurchaseItemDAO();
        itemDAO = new ItemDAO();

        // Create header panel
        JPanel headerPanel = new HeaderPanel(userId);
        add(headerPanel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1), "Purchase Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel buyerIDLabel = new JLabel("Buyer ID:");
        buyerIDLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(buyerIDLabel, gbc);

        buyerIDField = new JTextField(20);
        buyerIDField.setFont(new Font("Arial", Font.PLAIN, 14));
        buyerIDField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        gbc.gridx = 1;
        inputPanel.add(buyerIDField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel itemIDLabel = new JLabel("Item ID:");
        itemIDLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(itemIDLabel, gbc);

        itemIDField = new JTextField(20);
        itemIDField.setFont(new Font("Arial", Font.PLAIN, 14));
        itemIDField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        gbc.gridx = 1;
        inputPanel.add(itemIDField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(quantityLabel, gbc);

        quantityField = new JTextField(20);
        quantityField.setFont(new Font("Arial", Font.PLAIN, 14));
        quantityField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        gbc.gridx = 1;
        inputPanel.add(quantityField, gbc);

        JButton addButton = new JButton("Add Item");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBackground(new Color(0, 102, 204));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(new AddButtonListener());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(addButton, gbc);

        JButton completeButton = new JButton("Complete Purchase");
        completeButton.setFont(new Font("Arial", Font.BOLD, 14));
        completeButton.setBackground(new Color(0, 153, 51));
        completeButton.setForeground(Color.WHITE);
        completeButton.setFocusPainted(false);
        completeButton.addActionListener(new CompleteButtonListener());
        gbc.gridy = 4;
        inputPanel.add(completeButton, gbc);

        totalPendingLabel = new JLabel("Total Pending Amount: $0.00"); // Initialize the label
        totalPendingLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalPendingLabel.setForeground(new Color(0, 102, 204));
        totalPendingLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        gbc.gridy = 5;
        inputPanel.add(totalPendingLabel, gbc);

        add(inputPanel, BorderLayout.CENTER);

        tableModel = new PurchaseItemTableModel();
        purchaseTable = new JTable(tableModel);
        styleTable(purchaseTable);
        JScrollPane purchaseScrollPane = new JScrollPane(purchaseTable);
        purchaseScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1), "Pending Purchases"));

        itemTableModel = new ItemTableModel();
        itemDetailsTable = new JTable(itemTableModel);
        styleTable(itemDetailsTable);
        JScrollPane itemDetailsScrollPane = new JScrollPane(itemDetailsTable);
        itemDetailsScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1), "Item Details"));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, purchaseScrollPane, itemDetailsScrollPane);
        splitPane.setResizeWeight(0.7); // 70% width for the pending purchases table
        splitPane.setOneTouchExpandable(true);

        add(splitPane, BorderLayout.SOUTH);

        loadPendingItems();
        loadAllItems();  // Load all items into the item details table
        updateTotalPending();
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setGridColor(Color.GRAY);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(0, 102, 204));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        table.setSelectionBackground(new Color(0, 153, 255));
        table.setSelectionForeground(Color.WHITE);
    }

    private void loadPendingItems() {
        List<PurchaseItem> items = purchaseItemDAO.getPendingPurchaseItems();
        tableModel.setItems(items);
    }

    private void loadAllItems() {
        List<Item> items = itemDAO.getAllItems();
        itemTableModel.setItems(items);
    }

    private void updateTotalPending() {
        BigDecimal total = purchaseItemDAO.getPendingItemsTotal();
        totalPendingLabel.setText("Total Pending Amount: $" + total.toString());
    }

    private void refreshTable() {
        loadPendingItems();
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int buyerID = Integer.parseInt(buyerIDField.getText());
                int itemID = Integer.parseInt(itemIDField.getText());
                int quantity = Integer.parseInt(quantityField.getText());

                Item item = itemDAO.getItemById(itemID);
                if (item != null) {
                    // Check if the purchase quantity is less than or equal to the available quantity
                    if (quantity <= item.getQuantity()) {
                        BigDecimal totalPrice = item.getPrice().multiply(BigDecimal.valueOf(quantity));
                        PurchaseItem purchaseItem = new PurchaseItem(
                                0, // Auto-incremented by the database
                                buyerID,
                                itemID,
                                new java.sql.Date(new Date().getTime()),
                                quantity,
                                totalPrice,
                                "Pending"
                        );
                        if (purchaseItemDAO.addPurchaseItem(purchaseItem)) {
                            // Update the item quantity
                            if (itemDAO.updateItemQuantity(itemID, item.getQuantity() - quantity)) {
                                loadPendingItems();

                                itemIDField.setText("");
                                quantityField.setText("");
                                loadAllItems(); // Refresh the item details table
                                updateTotalPending(); // Update total pending amount after adding
                            } else {
                                JOptionPane.showMessageDialog(PurchaseGUI.this, "Failed to update item quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(PurchaseGUI.this, "Failed to add purchase item.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(PurchaseGUI.this, "Purchase quantity exceeds available quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(PurchaseGUI.this, "Item not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(PurchaseGUI.this, "Invalid input. Please enter numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(PurchaseGUI.this, "Database error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }


    private class CompleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int[] selectedRows = purchaseTable.getSelectedRows();
            if (selectedRows.length > 0) {
                List<Integer> purchaseIDs = new ArrayList<>();
                for (int row : selectedRows) {
                    int purchaseID = (int) purchaseTable.getValueAt(row, 0);
                    purchaseIDs.add(purchaseID);
                }
                if (purchaseItemDAO.updatePurchaseStatus(purchaseIDs, "Completed")) {
                    JOptionPane.showMessageDialog(PurchaseGUI.this, "Purchase completed successfully.");
                    refreshTable();
                    updateTotalPending(); // Update total pending amount after completion
                } else {
                    JOptionPane.showMessageDialog(PurchaseGUI.this, "Failed to complete purchase.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(PurchaseGUI.this, "No purchase selected.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }


    private class ItemTableModel extends AbstractTableModel {
        private final String[] columnNames = {"Item ID", "Item Name", "Price", "Quantity"};
        private List<Item> items;

        public void setItems(List<Item> items) {
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
            Item item = items.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return item.getId();
                case 1:
                    return item.getName();
                case 2:
                    return item.getPrice();
                case 3:
                    return item.getQuantity();
                default:
                    return null;
            }
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }
    }
}
