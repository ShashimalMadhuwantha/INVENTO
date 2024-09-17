import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BuyerGUI extends JFrame {
    private BuyerDAO buyerDAO;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField phoneNumberField;
    private JTable buyerTable;
    private BuyerTableModel tableModel;

    public BuyerGUI( int userId) {

        super("Buyer Management");
        HeaderPanel headerPanel = new HeaderPanel(userId);

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);

        buyerDAO = new BuyerDAO(); // Initialize DAO

        // Panel for input fields and buttons
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1), "Buyer Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        // Labels and fields
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(firstNameLabel, gbc);

        firstNameField = new JTextField(20);
        firstNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        firstNameField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        gbc.gridx = 1;
        inputPanel.add(firstNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(lastNameLabel, gbc);

        lastNameField = new JTextField(20);
        lastNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        lastNameField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        gbc.gridx = 1;
        inputPanel.add(lastNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        phoneNumberLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(phoneNumberLabel, gbc);

        phoneNumberField = new JTextField(20);
        phoneNumberField.setFont(new Font("Arial", Font.PLAIN, 14));
        phoneNumberField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        gbc.gridx = 1;
        inputPanel.add(phoneNumberField, gbc);

        JButton addButton = new JButton("Add Buyer");
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

        JButton updateButton = new JButton("Update Buyer");
        updateButton.setFont(new Font("Arial", Font.BOLD, 14));
        updateButton.setBackground(new Color(0, 102, 204));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.addActionListener(new UpdateButtonListener());
        gbc.gridy = 4;
        inputPanel.add(updateButton, gbc);

        JButton deleteButton = new JButton("Delete Buyer");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.setBackground(new Color(0, 102, 204));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(new DeleteButtonListener());
        gbc.gridy = 5;
        inputPanel.add(deleteButton, gbc);

        JButton clearButton = new JButton("Clear ");
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearButton.setBackground(new Color(0, 102, 204));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.addActionListener(new ClearButtonListner());
        gbc.gridy = 6;
        inputPanel.add(clearButton, gbc);

        add(inputPanel, BorderLayout.WEST);

        // Table for displaying buyers
        tableModel = new BuyerTableModel();
        buyerTable = new JTable(tableModel);
        buyerTable.setFont(new Font("Arial", Font.PLAIN, 14));
        buyerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        buyerTable.setRowHeight(40); // Increase row height for better visibility
        buyerTable.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Customize the table header
        JTableHeader header = buyerTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(0, 102, 204));
        header.setForeground(Color.WHITE);

        // Customize cell renderer to align text
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) buyerTable.getTableHeader().getDefaultRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);

        JScrollPane scrollPane = new JScrollPane(buyerTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1), "Buyer List"));
        add(scrollPane, BorderLayout.CENTER);

        loadBuyers(); // Load buyers from the database

        buyerTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = buyerTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        Buyer selectedBuyer = tableModel.getBuyerAt(selectedRow);
                        populateFields(selectedBuyer);
                    }
                }
            }
        });
    }

    private void loadBuyers() {
        List<Buyer> buyers = buyerDAO.getAllBuyers();
        tableModel.setBuyers(buyers);
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String phoneNumber = phoneNumberField.getText();
            Buyer buyer = new Buyer(0, firstName, lastName, phoneNumber);
            if (buyerDAO.addBuyer(buyer)) {
                JOptionPane.showMessageDialog(BuyerGUI.this, "Buyer added successfully.");
                loadBuyers();
                cleartextFields();
            } else {
                JOptionPane.showMessageDialog(BuyerGUI.this, "Failed to add buyer.");
            }
        }
    }

    private class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = buyerTable.getSelectedRow();
            if (selectedRow != -1) {
                int buyerID = (int) buyerTable.getValueAt(selectedRow, 0);
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String phoneNumber = phoneNumberField.getText();
                Buyer buyer = new Buyer(buyerID, firstName, lastName, phoneNumber);
                if (buyerDAO.updateBuyer(buyer)) {
                    JOptionPane.showMessageDialog(BuyerGUI.this, "Buyer updated successfully.");
                    loadBuyers();
                    cleartextFields();
                } else {
                    JOptionPane.showMessageDialog(BuyerGUI.this, "Failed to update buyer.");
                }
            } else {
                JOptionPane.showMessageDialog(BuyerGUI.this, "Please select a buyer to update.");
            }
        }
    }

    private void populateFields(Buyer buyer) {
        firstNameField.setText(buyer.getFirstName());
        lastNameField.setText(buyer.getLastName());
        phoneNumberField.setText(buyer.getPhoneNumber());
    }

    private void cleartextFields()
    {
        firstNameField.setText("");
        lastNameField.setText("");
        phoneNumberField.setText("");
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = buyerTable.getSelectedRow();
            if (selectedRow != -1) {
                // Retrieve the Buyer ID from the selected row
                int buyerID = (int) buyerTable.getValueAt(selectedRow, 0);

                // Confirm deletion
                int confirmation = JOptionPane.showConfirmDialog(BuyerGUI.this,
                        "Are you sure you want to delete this Buyer?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);

                // Proceed if confirmed
                if (confirmation == JOptionPane.YES_OPTION) {
                    // Try to delete the buyer
                    if (buyerDAO.deleteBuyer(buyerID)) {
                        JOptionPane.showMessageDialog(BuyerGUI.this, "Buyer deleted successfully.");
                        // Reload the list of buyers to reflect the deletion
                        loadBuyers();
                    } else {
                        JOptionPane.showMessageDialog(BuyerGUI.this, "Failed to delete buyer.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(BuyerGUI.this, "Please select a buyer to delete.");
            }
        }
    }


    private  class  ClearButtonListner implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            cleartextFields();
        }
    }
}
