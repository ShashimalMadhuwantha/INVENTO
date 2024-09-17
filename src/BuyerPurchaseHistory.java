import com.toedter.calendar.JCalendar; // Import for the JCalendar component
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Date;

public class BuyerPurchaseHistory extends JFrame {
    private JTextField searchField;
    private JTextField dateField;
    private JButton searchButton;
    private JButton dateButton;
    private JTable resultsTable;
    private DefaultTableModel tableModel;


    private static final String URL = "jdbc:mysql://localhost:3308/inventory";
    private static final String USER = "root";
    private static final String PASSWORD = "2003";

    public BuyerPurchaseHistory(int userId) {
        // Set up the frame
        setTitle("Buyer Purchase History");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create header panel
        JPanel headerPanel = new HeaderPanel(userId);
        add(headerPanel, BorderLayout.NORTH);

        // Create components
        searchField = new JTextField(20);
        dateField = new JTextField(10);
        dateField.setEditable(false);
        dateButton = new JButton("Select Date");
        searchButton = new JButton("Search");


        searchButton.setBackground(new Color(30, 144, 255));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        dateButton.setBackground(new Color(30, 144, 255));
        dateButton.setForeground(Color.WHITE);
        dateButton.setFocusPainted(false);


        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"First Name", "Last Name", "Purchase ID", "Item ID", "Purchase Date", "Quantity", "Total Price", "Status"});
        resultsTable = new JTable(tableModel);
        resultsTable.setFillsViewportHeight(true);
        resultsTable.setRowHeight(30);
        resultsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        resultsTable.setSelectionBackground(new Color(173, 216, 230)); // Light blue selection color

        // Style table headers
        resultsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        resultsTable.getTableHeader().setBackground(new Color(0, 102, 204)); // Dark blue header
        resultsTable.getTableHeader().setForeground(Color.WHITE);
        resultsTable.getTableHeader().setReorderingAllowed(false);


        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search by Name:"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel("Selected Date:"));
        searchPanel.add(dateField);
        searchPanel.add(dateButton);
        searchPanel.add(searchButton);


        JScrollPane scrollPane = new JScrollPane(resultsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));

        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        container.add(searchPanel, BorderLayout.NORTH);
        container.add(scrollPane, BorderLayout.CENTER);
        add(container, BorderLayout.CENTER);


        dateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCalendar calendar = new JCalendar(); // Initialize JCalendar
                int result = JOptionPane.showConfirmDialog(null, calendar, "Select Date", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    Date selectedDate = calendar.getDate();
                    dateField.setText(new java.text.SimpleDateFormat("yyyy-MM-dd").format(selectedDate));
                }
            }
        });


        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchPurchaseHistory();
            }
        });
    }

    private void searchPurchaseHistory() {
        String searchName = searchField.getText().trim();
        String selectedDate = dateField.getText().trim(); // Get selected date as a string

        StringBuilder query = new StringBuilder("SELECT b.FirstName, b.LastName, p.PurchaseID, p.ItemID, p.PurchaseDate, p.Quantity, p.TotalPrice, p.Status " +
                "FROM Buyer b " +
                "INNER JOIN PurchaseItem p ON b.BuyerID = p.BuyerID " +
                "WHERE 1=1 ");

        // Append the conditions based on the inputs
        if (!searchName.isEmpty()) {
            query.append("AND CONCAT(b.FirstName, ' ', b.LastName) LIKE ? ");
        }
        if (!selectedDate.isEmpty()) {
            query.append("AND p.PurchaseDate = ? ");
        }

        query.append("ORDER BY p.PurchaseDate DESC");
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query.toString())) {

            int paramIndex = 1;

            // Set parameters based on the inputs
            if (!searchName.isEmpty()) {
                statement.setString(paramIndex++, "%" + searchName + "%");
            }
            if (!selectedDate.isEmpty()) {
                statement.setString(paramIndex++, selectedDate);
            }

            ResultSet resultSet = statement.executeQuery();
            tableModel.setRowCount(0); // Clear existing data

            while (resultSet.next()) {
                Object[] row = new Object[]{
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getInt("PurchaseID"),
                        resultSet.getInt("ItemID"),
                        resultSet.getDate("PurchaseDate"),
                        resultSet.getInt("Quantity"),
                        resultSet.getBigDecimal("TotalPrice"),
                        resultSet.getString("Status")
                };
                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
