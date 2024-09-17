import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SupplierGUI extends JFrame {
    private SupplierDAO supplierDAO;
    private JTextField nameField;
    private JTextField addressField;
    private JTextField contactField;
    private JTable supplierTable;
    private SupplierTableModel tableModel;

    public SupplierGUI(int userid) {
        super("Supplier Management");
        // Create the HeaderPanel with parameters
        HeaderPanel headerPanel = new HeaderPanel(userid);
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);

        supplierDAO = new SupplierDAO();

        // Panel for input fields and buttons
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1), "Supplier Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        // Labels and fields
        JLabel nameLabel = new JLabel("Supplier Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(nameLabel, gbc);

        nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        nameField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        gbc.gridx = 1;
        inputPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(addressLabel, gbc);

        addressField = new JTextField(20);
        addressField.setFont(new Font("Arial", Font.PLAIN, 14));
        addressField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        gbc.gridx = 1;
        inputPanel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel contactLabel = new JLabel("Contact Number:");
        contactLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(contactLabel, gbc);

        contactField = new JTextField(20);
        contactField.setFont(new Font("Arial", Font.PLAIN, 14));
        contactField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        gbc.gridx = 1;
        inputPanel.add(contactField, gbc);

        JButton addButton = new JButton("Add Supplier");
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

        JButton updateButton = new JButton("Update Supplier");
        updateButton.setFont(new Font("Arial", Font.BOLD, 14));
        updateButton.setBackground(new Color(0, 102, 204));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.addActionListener(new UpdateButtonListener());
        gbc.gridy = 4;
        inputPanel.add(updateButton, gbc);

        JButton deleteButton = new JButton("Delete Supplier");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.setBackground(new Color(0, 102, 204));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(new DeleteButtonListener());
        gbc.gridy = 5;
        inputPanel.add(deleteButton, gbc);

        JButton clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearButton.setBackground(new Color(0, 102, 204));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.addActionListener(new ClearButtonListener());
        gbc.gridy = 6;
        inputPanel.add(clearButton, gbc);

        add(inputPanel, BorderLayout.WEST);

        // Table for displaying suppliers
        tableModel = new SupplierTableModel();
        supplierTable = new JTable(tableModel);
        supplierTable.setFont(new Font("Arial", Font.PLAIN, 14));
        supplierTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        supplierTable.setRowHeight(40); // Increase row height for better visibility
        supplierTable.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Customize the table header
        JTableHeader header = supplierTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(0, 102, 204));
        header.setForeground(Color.WHITE);

        // Customize cell renderer to align text
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) supplierTable.getTableHeader().getDefaultRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);

        JScrollPane scrollPane = new JScrollPane(supplierTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1), "Suppliers List"));
        add(scrollPane, BorderLayout.CENTER);

        // Set maximum size for the panel
        setPreferredSize(new Dimension(1200, 800));
        setMinimumSize(new Dimension(1200, 800));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        loadSuppliers();

        supplierTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = supplierTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Supplier selectedSupplier = tableModel.getSupplierAt(selectedRow);
                    populateFields(selectedSupplier);
                }
            }
        });
    }

    private void loadSuppliers() {
        List<Supplier> suppliers = supplierDAO.getAllSuppliers();
        tableModel.setSuppliers(suppliers);
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            String address = addressField.getText();
            String contact = contactField.getText();
            if (supplierDAO.addSupplier(name, address, contact)) {
                JOptionPane.showMessageDialog(SupplierGUI.this, "Supplier added successfully.");
                loadSuppliers();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(SupplierGUI.this, "Failed to add supplier.");
            }
        }
    }

    private class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = supplierTable.getSelectedRow();
            if (selectedRow >= 0) {
                Supplier supplier = tableModel.getSupplierAt(selectedRow);
                String name = nameField.getText();
                String address = addressField.getText();
                String contact = contactField.getText();
                if (supplierDAO.updateSupplier(supplier.getId(), name, address, contact)) {
                    JOptionPane.showMessageDialog(SupplierGUI.this, "Supplier updated successfully.");
                    loadSuppliers();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(SupplierGUI.this, "Failed to update supplier.");
                }
            } else {
                JOptionPane.showMessageDialog(SupplierGUI.this, "Select a supplier to update.");
            }
        }
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = supplierTable.getSelectedRow();
            if (selectedRow >= 0) {
                Supplier supplier = tableModel.getSupplierAt(selectedRow);
                int confirmation = JOptionPane.showConfirmDialog(SupplierGUI.this,
                        "Are you sure you want to delete this supplier?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    if (supplierDAO.deleteSupplier(supplier.getId())) {
                        JOptionPane.showMessageDialog(SupplierGUI.this, "Supplier deleted successfully.");
                        loadSuppliers();
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(SupplierGUI.this, "Failed to delete supplier.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(SupplierGUI.this, "Select a supplier to delete.");
            }
        }
    }

    private class ClearButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            clearFields();
        }
    }

    private void clearFields() {
        nameField.setText("");
        addressField.setText("");
        contactField.setText("");
    }
    private void populateFields(Supplier supplier) {
        nameField.setText(supplier.getName());
        addressField.setText(supplier.getAddress());
        contactField.setText(supplier.getContactNumber());
    }
}
