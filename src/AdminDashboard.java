import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AdminDashboard extends JFrame {
    private int userId;
    private AdminDAO adminDAO;


    public AdminDashboard(int userId) {
        this.userId = userId;
        setTitle("Admin Dashboard");
        setSize(1200, 800); // Set the size of the dashboard window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        try {
            adminDAO = new AdminDAO();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to the database.");
            System.exit(1);
        }



        HeaderPanel headerPanel = new HeaderPanel(userId);
       Container container = getContentPane();

       Container container1 = new JPanel();
       container1.setLayout(new BorderLayout());


       JPanel uppanel = createUpdateFormPanel();
       JPanel welcome = createWelcomePanel();
        container1.add(welcome,BorderLayout.NORTH);
        container1.add(uppanel,BorderLayout.CENTER);


        container.add(headerPanel,BorderLayout.NORTH);
        container.add(container1,BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);


        String username = "";
        try {
            username = adminDAO.fetchUsername(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch username.");
        }


        JLabel welcomeLabel = new JLabel("Welcome to the Dashboard, " + username);
        welcomeLabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 24));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        welcomeLabel.setForeground(new Color(0, 102, 204)); // Dark blue color

        panel.add(welcomeLabel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createUpdateFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(usernameField, gbc);


        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(passwordField, gbc);

        JLabel conpasswordLabel = new JLabel("Confirm Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(conpasswordLabel, gbc);

        JPasswordField conpasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(conpasswordField, gbc);

        JButton updateButton = new JButton("Change Password");
        updateButton.setBackground(new Color(0, 102, 204)); // Dark blue button
        updateButton.setForeground(Color.WHITE); // White text
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(updateButton, gbc);


        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(AdminDashboard.this,
                        "Are you sure you want to change the username and password?",
                        "Confirm Update", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (response == JOptionPane.YES_OPTION) {
                    String newUsername = usernameField.getText().trim();
                    String newPassword = new String(passwordField.getPassword()).trim();
                    String confirmPassword = new String(conpasswordField.getPassword()).trim();

                    if (newUsername.isEmpty() || newPassword.isEmpty()) {
                        JOptionPane.showMessageDialog(AdminDashboard.this, "Username and password cannot be empty.");
                        return;
                    }

                    if (!newPassword.equals(confirmPassword)) {
                        JOptionPane.showMessageDialog(AdminDashboard.this, "Passwords do not match.");
                        return;
                    }

                    try {
                        boolean success = adminDAO.updateAdmin(userId, newUsername, newPassword);
                        if (success) {
                            JOptionPane.showMessageDialog(AdminDashboard.this, "Profile updated successfully.");
                        } else {
                            JOptionPane.showMessageDialog(AdminDashboard.this, "Failed to update profile.");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(AdminDashboard.this, "Database error: " + ex.getMessage());
                    }
                }
            }
        });

        return panel;
    }


}
