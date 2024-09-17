import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {
    private Connection connection;

    public ItemDAO() {
        try {

            String url = "jdbc:mysql://localhost:3308/inventory";
            String username = "root";
            String password = "2003";
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database");
        }
    }

    public boolean updateItemQuantity(int itemId, int newQuantity) throws SQLException {

        String sql = "UPDATE Item SET Quantity = ? WHERE ItemID = ?";


        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, itemId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace();
            throw new SQLException("Error updating item quantity.", e);
        }
    }

    public boolean addItem(Item item) {
        String sql = "INSERT INTO item (Name, Description, Price, Quantity, SupplierID) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setBigDecimal(3, item.getPrice());
            stmt.setInt(4, item.getQuantity());
            stmt.setInt(5, item.getSupplierID());
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




    public boolean updateItem(Item item) {
        String sql = "UPDATE item SET Name = ?, Description = ?, Price = ?, Quantity = ?, SupplierID = ? WHERE ItemID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setBigDecimal(3, item.getPrice());
            stmt.setInt(4, item.getQuantity());
            stmt.setInt(5, item.getSupplierID());
            stmt.setInt(6, item.getId()); // Use getItemID() here
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteItem(int itemId) {
        String sql = "DELETE FROM item WHERE ItemID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, itemId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM item";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Item item = new Item(
                        rs.getInt("ItemID"),
                        rs.getString("Name"),
                        rs.getString("Description"),
                        rs.getBigDecimal("Price"),
                        rs.getInt("Quantity"),
                        rs.getInt("SupplierID")
                );
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }



    public Item getItemById(int itemId) {
        String sql = "SELECT * FROM item WHERE ItemID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, itemId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Item(
                            rs.getInt("ItemID"),
                            rs.getString("Name"),
                            rs.getString("Description"),
                            rs.getBigDecimal("Price"),
                            rs.getInt("Quantity"),
                            rs.getInt("SupplierID")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



}
