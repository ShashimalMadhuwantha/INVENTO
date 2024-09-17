import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class HeaderPanel extends JPanel implements ActionListener {

    private int userID;

    public HeaderPanel( int userID) {

        this.userID = userID;
        setLayout(new BorderLayout());
        setBackground(new Color(15, 150, 195));

        // Left side - Inventory Management System label
        JLabel titleLabel = new JLabel("INVENTORY MANAGEMENT SYSTEM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(255, 255, 255, 255));
        add(titleLabel, BorderLayout.WEST);

        // Right side - Navigation items
        JPanel navPanel = new JPanel();
        navPanel.setBackground(new Color(15, 150, 195));
        navPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        String[] navItems = {"Dashboard", "Item", "Supplier", "Purchasing", "Buyers", "PurchaseHistory","Restocking"};
        for (String item : navItems) {
            JButton navButton = new JButton(item);
            navButton.setFont(new Font("Arial", Font.PLAIN, 18));
            navButton.setForeground(Color.WHITE);
            navButton.setBackground(new Color(0, 102, 204));
            navButton.setFocusPainted(false); // Removes the focus border
            navButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.WHITE, 1),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
            ));
            navButton.setActionCommand(item); // Set action command


            navButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    navButton.setBackground(new Color(77, 116, 209));
                    navButton.setForeground(new Color(255, 255, 255));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    navButton.setBackground(new Color(0, 102, 204));
                    navButton.setForeground(Color.WHITE);
                }
            });

            navButton.addActionListener(this); // Add action listener
            navPanel.add(navButton);
        }

        add(navPanel, BorderLayout.EAST);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        try {
            switch (command) {
                case "Supplier":

                    SwingUtilities.getWindowAncestor(this).setVisible(false);
                    SupplierGUI supplierGUI = new SupplierGUI(userID);

                    supplierGUI.setSize(screenSize);

                    // Center the window on the screen
                    supplierGUI.setLocationRelativeTo(null);
                    supplierGUI.setVisible(true);
                    break;

                case "Item":
                    SwingUtilities.getWindowAncestor(this).setVisible(false);
                    ItemGUI itemGUI = new ItemGUI(userID);

                    itemGUI.setSize(screenSize);

                    // Center the window on the screen
                    itemGUI.setLocationRelativeTo(null);
                    itemGUI.setVisible(true);
                    break;
                case "Dashboard":
                    // Create and show the AdminDashboard with userID
                    SwingUtilities.getWindowAncestor(this).setVisible(false);
                    AdminDashboard adminDashboard = new AdminDashboard(userID);

                    adminDashboard.setSize(screenSize);

                    // Center the window on the screen
                    adminDashboard.setLocationRelativeTo(null);
                    adminDashboard.setVisible(true);
                    break;

                case "Purchasing":
                    SwingUtilities.getWindowAncestor(this).setVisible(false);
                    PurchaseGUI purchaseGUI = new PurchaseGUI(userID);

                    purchaseGUI.setSize(screenSize);

                    // Center the window on the screen
                    purchaseGUI.setLocationRelativeTo(null);
                    purchaseGUI.setVisible(true);
                    break;

                case "Buyers":
                    SwingUtilities.getWindowAncestor(this).setVisible(false);
                    BuyerGUI buyerGUI = new BuyerGUI(userID);

                    buyerGUI.setSize(screenSize);

                    // Center the window on the screen
                    buyerGUI.setLocationRelativeTo(null);
                    buyerGUI.setVisible(true);
                    break;

                case "PurchaseHistory":
                    // Ensure InventoryGUI is correctly initialized
                    SwingUtilities.getWindowAncestor(this).setVisible(false);
                    BuyerPurchaseHistory purchaseHistoryGUI = new BuyerPurchaseHistory(userID);
                    // Set the size of the window to full screen
                    Dimension screenSize1 = Toolkit.getDefaultToolkit().getScreenSize();
                    purchaseHistoryGUI.setSize(screenSize1);

                    // Center the window on the screen
                    purchaseHistoryGUI.setLocationRelativeTo(null);
                    purchaseHistoryGUI.setVisible(true);
                    break;


                case "Restocking":
                    // Ensure InventoryGUI is correctly initialized
                    SwingUtilities.getWindowAncestor(this).setVisible(false);
                   RestockGUI restockGUI = new RestockGUI(userID);
                    // Set the size of the window to full screen
                    restockGUI.setSize(screenSize);
                    // Center the window on the screen
                    restockGUI.setLocationRelativeTo(null);
                    restockGUI.setVisible(true);
                    break;

                default:
                    JOptionPane.showMessageDialog(this, "Unknown section: " + command);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while switching views: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}
