import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

public class LoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private AdminDAO adminDAO;

    public LoginGUI() {
        try {
            adminDAO = new AdminDAO(); // Initialize AdminDAO for DB operations
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to the database.");
            System.exit(1);
        }
        setSize(1200, 800);
        setTitle("Inventory Management System - Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the screen size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Set a custom background color
        getContentPane().setBackground(new Color(240, 248, 255)); // Light blue color

        // Create and style the UI components
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));

        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.WHITE); // White text
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Create and style labels
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        usernameLabel.setForeground(new Color(0, 102, 204)); // Dark blue color
        usernameLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 18));
        passwordLabel.setForeground(new Color(0, 102, 204)); // Dark blue color
        passwordLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Create and style the title label
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(new Color(0, 102, 204)); // Dark blue color
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        // Create a panel for the form and add a border around it
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 102, 204), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20) // Padding inside the border
        ));
        formPanel.setOpaque(false); // Transparent background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        // Add components to the form panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(titleLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Reset to one column
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginButton, gbc);

        // Center the form panel within the frame
        setLayout(new GridBagLayout());
        add(formPanel);

        // Add ActionListener to the login button
        loginButton.addActionListener(new LoginButtonListener());

        // Add ActionListener to the password field for "Enter" key
        passwordField.addActionListener(new LoginButtonListener());

        usernameField.addKeyListener(new ArrowKeyListener());
        passwordField.addKeyListener(new ArrowKeyListener());

        // Display the window
        setVisible(true);
    }

    private class ArrowKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                // Focus the password field when the down arrow is pressed
                passwordField.requestFocus();
            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                // Focus the username field when the up arrow is pressed
                usernameField.requestFocus();
            }
        }
    }

    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            validateAndLogin();
        }
    }

    private void validateAndLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            if (adminDAO.validateLogin(username, password)) {
                JOptionPane.showMessageDialog(LoginGUI.this, "Login successful!");
                openAdminDashboard(adminDAO.getUserId(username)); // Pass user ID to the dashboard
            } else {
                JOptionPane.showMessageDialog(LoginGUI.this, "Invalid username or password.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(LoginGUI.this, "Database error: " + ex.getMessage());
        }
    }

    private void openAdminDashboard(int userId) {
        
        // Hide the login frame
        this.setVisible(false);


        SwingUtilities.invokeLater(() -> {
            AdminDashboard dashboard = new AdminDashboard(userId);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            dashboard.setSize(screenSize);
            // Center the window on the screen
            dashboard.setLocationRelativeTo(null);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI().setVisible(true));
    }
}
