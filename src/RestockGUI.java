import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;

public class RestockGUI extends JFrame {
    private ItemDAO itemDAO;
    private JTextField itemIDField;
    private JTextField restockQuantityField;
    private JTable itemTable;
    private ItemTableModel tableModel;

    public RestockGUI(int userId) {
        super("Restocking");
        HeaderPanel headerPanel = new HeaderPanel(userId);
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);

        itemDAO = new ItemDAO();

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1), "Restock Item"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        // Labels and fields
        JLabel itemIDLabel = new JLabel("Item ID:");
        itemIDLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(itemIDLabel, gbc);

        itemIDField = new JTextField(20);
        itemIDField.setFont(new Font("Arial", Font.PLAIN, 14));
        itemIDField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        gbc.gridx = 1;
        inputPanel.add(itemIDField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel restockQuantityLabel = new JLabel("Restock Quantity:");
        restockQuantityLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(restockQuantityLabel, gbc);

        restockQuantityField = new JTextField(20);
        restockQuantityField.setFont(new Font("Arial", Font.PLAIN, 14));
        restockQuantityField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        gbc.gridx = 1;
        inputPanel.add(restockQuantityField, gbc);

        JButton restockButton = new JButton("Restock Item");
        restockButton.setFont(new Font("Arial", Font.BOLD, 14));
        restockButton.setBackground(new Color(0, 102, 204));
        restockButton.setForeground(Color.WHITE);
        restockButton.setFocusPainted(false);
        restockButton.addActionListener(new RestockButtonListener());
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(restockButton, gbc);

        add(inputPanel, BorderLayout.WEST);

        // Table for displaying items
        tableModel = new ItemTableModel();
        itemTable = new JTable(tableModel);
        itemTable.setFont(new Font("Arial", Font.PLAIN, 14));
        itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemTable.setRowHeight(40); // Increase row height for better visibility
        itemTable.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JTableHeader header = itemTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(0, 102, 204));
        header.setForeground(Color.WHITE);

        // Customize cell renderer to align text
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) itemTable.getTableHeader().getDefaultRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);

        JScrollPane scrollPane = new JScrollPane(itemTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1), "Items List"));
        add(scrollPane, BorderLayout.CENTER);

        // Set maximum size for the panel
        setPreferredSize(new Dimension(1200, 800));
        setMinimumSize(new Dimension(1200, 800));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        loadItems();

        itemTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                int selectedRow = itemTable.getSelectedRow();
                if (!event.getValueIsAdjusting() && selectedRow != -1) {
                    // Get the item ID from the selected row
                    int itemId = (int) tableModel.getValueAt(selectedRow, 0); // Assuming ID is in the first column
                    itemIDField.setText(String.valueOf(itemId));
                }
            }
        });
    }

    private void loadItems() {
        List<Item> items = itemDAO.getAllItems();
        tableModel.setItems(items);
    }

    private class RestockButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int itemId = Integer.parseInt(itemIDField.getText());
                int restockQuantity = Integer.parseInt(restockQuantityField.getText());

                // Get the current quantity from the database
                Item item = itemDAO.getItemById(itemId);
                if (item == null) {
                    JOptionPane.showMessageDialog(RestockGUI.this, "Item not found.");
                    return;
                }

                int currentQuantity = item.getQuantity();
                int newTotalQuantity = currentQuantity + restockQuantity;

                // Ask for confirmation before proceeding
                int confirmation = JOptionPane.showConfirmDialog(RestockGUI.this,
                        "Are you sure you want to restock this item?\n" +
                                "Current Quantity: " + currentQuantity + "\n" +
                                "Restock Quantity: " + restockQuantity + "\n" +
                                "New Total Quantity: " + newTotalQuantity,
                        "Confirm Restock",
                        JOptionPane.YES_NO_OPTION);

                // Proceed with restocking if the user confirms
                if (confirmation == JOptionPane.YES_OPTION) {
                    if (itemDAO.updateItemQuantity(itemId, newTotalQuantity)) {
                        JOptionPane.showMessageDialog(RestockGUI.this, "Item restocked successfully. New Quantity: " + newTotalQuantity);
                        loadItems();
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(RestockGUI.this, "Failed to restock item.");
                    }
                } else {
                    JOptionPane.showMessageDialog(RestockGUI.this, "Restock action canceled.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(RestockGUI.this, "Error: " + ex.getMessage());
            }
        }
    }


    private void clearFields() {
        itemIDField.setText("");
        restockQuantityField.setText("");
    }
}
