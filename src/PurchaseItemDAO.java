import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseItemDAO {
    private Connection connection;

    public PurchaseItemDAO() {
        try {
            String url = "jdbc:mysql://localhost:3308/inventory";
            String username = "root";
            String password = "2003";
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database", e);
        }
    }

    public List<PurchaseItem> getPendingPurchaseItems() {
        List<PurchaseItem> items = new ArrayList<>();
        String query = "SELECT * FROM PurchaseItem WHERE Status = 'Pending'";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                PurchaseItem item = new PurchaseItem(
                        rs.getInt("PurchaseID"),
                        rs.getInt("BuyerID"),
                        rs.getInt("ItemID"),
                        rs.getDate("PurchaseDate"),
                        rs.getInt("Quantity"),
                        rs.getBigDecimal("TotalPrice"),
                        rs.getString("Status")
                );
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception
        }
        return items;
    }

    public boolean addPurchaseItem(PurchaseItem purchaseItem) {
        String query = "INSERT INTO PurchaseItem (BuyerID, ItemID, PurchaseDate, Quantity, TotalPrice, Status) VALUES (?, ?, ?, ?, ?, 'Pending')";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, purchaseItem.getBuyerID());
            stmt.setInt(2, purchaseItem.getItemID());
            stmt.setDate(3, new java.sql.Date(purchaseItem.getPurchaseDate().getTime()));
            stmt.setInt(4, purchaseItem.getQuantity());
            stmt.setBigDecimal(5, purchaseItem.getTotalPrice());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception
        }
        return false;
    }




    public boolean updatePurchaseStatus(List<Integer> purchaseIDs, String status) {
        String query = "UPDATE PurchaseItem SET Status = ? WHERE PurchaseID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false); // Start transaction
            for (int purchaseID : purchaseIDs) {
                statement.setString(1, status);
                statement.setInt(2, purchaseID);
                statement.addBatch();
            }
            int[] result = statement.executeBatch();
            connection.commit();
            return result.length == purchaseIDs.size();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
        return false;
    }

    public BigDecimal getPendingItemsTotal() {
        BigDecimal total = BigDecimal.ZERO;
        String query = "SELECT SUM(TotalPrice) AS Total FROM PurchaseItem WHERE Status = 'Pending'";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                total = rs.getBigDecimal("Total");
                if (total == null) {
                    total = BigDecimal.ZERO;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
}
