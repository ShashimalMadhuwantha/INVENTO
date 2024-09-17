import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;

public class ItemGUI extends JFrame {
    private ItemDAO itemDAO;
    private JTextField nameField;
    private JTextField descriptionField;
    private JTextField priceField;
    private JTextField quantityField;
    private JTextField supplierIDField;
    private JTable itemTable;
    private ItemTableModel tableModel;

    public ItemGUI( int userId) {

        super("Item Management");
        HeaderPanel headerPanel = new HeaderPanel( userId);
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);

        itemDAO = new ItemDAO();


        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1), "Item Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        // Labels and fields
        JLabel nameLabel = new JLabel("Item Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(nameLabel, gbc);

        nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        nameField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        gbc.gridx = 1;
        inputPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(descriptionLabel, gbc);

        descriptionField = new JTextField(20);
        descriptionField.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        gbc.gridx = 1;
        inputPanel.add(descriptionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(priceLabel, gbc);

        priceField = new JTextField(20);
        priceField.setFont(new Font("Arial", Font.PLAIN, 14));
        priceField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        gbc.gridx = 1;
        inputPanel.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(quantityLabel, gbc);

        quantityField = new JTextField(20);
        quantityField.setFont(new Font("Arial", Font.PLAIN, 14));
        quantityField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        gbc.gridx = 1;
        inputPanel.add(quantityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel supplierIDLabel = new JLabel("Supplier ID:");
        supplierIDLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(supplierIDLabel, gbc);

        supplierIDField = new JTextField(20);
        supplierIDField.setFont(new Font("Arial", Font.PLAIN, 14));
        supplierIDField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        gbc.gridx = 1;
        inputPanel.add(supplierIDField, gbc);

        JButton addButton = new JButton("Add Item");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBackground(new Color(0, 102, 204));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(new AddButtonListener());
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(addButton, gbc);

        JButton updateButton = new JButton("Update Item");
        updateButton.setFont(new Font("Arial", Font.BOLD, 14));
        updateButton.setBackground(new Color(0, 102, 204));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.addActionListener(new UpdateButtonListener());
        gbc.gridy = 6;
        inputPanel.add(updateButton, gbc);

        JButton deleteButton = new JButton("Delete Item");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.setBackground(new Color(0, 102, 204));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(new DeleteButtonListener());
        gbc.gridy = 7;
        inputPanel.add(deleteButton, gbc);

        JButton clearButton = new JButton("Clear ");
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearButton.setBackground(new Color(0, 102, 204));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.addActionListener(new ClearButtonListner());
        gbc.gridy = 8;
        inputPanel.add(clearButton, gbc);

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
        itemTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = itemTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Item selectedItem = tableModel.getItemAt(selectedRow);
                    populateFields(selectedItem);
                }
            }
        });
    }

    private void loadItems() {
        List<Item> items = itemDAO.getAllItems();
        tableModel.setItems(items);
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Item item = new Item(
                        0, // ItemID will be auto-generated
                        nameField.getText(),
                        descriptionField.getText(),
                        new BigDecimal(priceField.getText()),
                        Integer.parseInt(quantityField.getText()),
                        Integer.parseInt(supplierIDField.getText())
                );
                if (itemDAO.addItem(item)) {
                    JOptionPane.showMessageDialog(ItemGUI.this, "Item added successfully.");
                    loadItems();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(ItemGUI.this, "Failed to add item.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ItemGUI.this, "Error: " + ex.getMessage());
            }
        }
    }

    private class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = itemTable.getSelectedRow();
            if (selectedRow >= 0) {
                Item item = tableModel.getItemAt(selectedRow);
                try {
                    item.setName(nameField.getText());
                    item.setDescription(descriptionField.getText());
                    item.setPrice(new BigDecimal(priceField.getText()));
                    item.setQuantity(Integer.parseInt(quantityField.getText()));
                    item.setSupplierID(Integer.parseInt(supplierIDField.getText()));
                    if (itemDAO.updateItem(item)) {
                        JOptionPane.showMessageDialog(ItemGUI.this, "Item updated successfully.");
                        loadItems();
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(ItemGUI.this, "Failed to update item.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ItemGUI.this, "Error: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(ItemGUI.this, "Select an item to update.");
            }
        }
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = itemTable.getSelectedRow();
            if (selectedRow >= 0) {
                Item item = tableModel.getItemAt(selectedRow);
                int confirmation = JOptionPane.showConfirmDialog(ItemGUI.this,
                        "Are you sure you want to delete this item?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    if (itemDAO.deleteItem(item.getId())) {
                        JOptionPane.showMessageDialog(ItemGUI.this, "Item deleted successfully.");
                        loadItems();
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(ItemGUI.this, "Failed to delete item.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(ItemGUI.this, "Select an item to delete.");
            }
        }
    }

    private class ClearButtonListner implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            clearFields();
        }
    }

    private void populateFields(Item item) {
        nameField.setText(item.getName());
        descriptionField.setText(item.getDescription());
        priceField.setText(item.getPrice().toString());
        quantityField.setText(String.valueOf(item.getQuantity()));
        supplierIDField.setText(String.valueOf(item.getSupplierID()));
    }

    private void clearFields() {
        nameField.setText("");
        descriptionField.setText("");
        priceField.setText("");
        quantityField.setText("");
        supplierIDField.setText("");
    }
}
