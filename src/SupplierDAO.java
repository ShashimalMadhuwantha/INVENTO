import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {
    private static final String URL = "jdbc:mysql://localhost:3308/inventory";
    private static final String USER = "root";
    private static final String PASSWORD = "2003";

    private Connection connection;

    public SupplierDAO() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database.");
        }
    }

    // Method to add a new supplier
    public boolean addSupplier(String name, String address, String contactNumber) {
        String query = "INSERT INTO Supplier (SupplierName, Address, ContactNumber) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, address);
            stmt.setString(3, contactNumber);
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to update an existing supplier
    public boolean updateSupplier(int supplierID, String name, String address, String contactNumber) {
        String query = "UPDATE Supplier SET SupplierName = ?, Address = ?, ContactNumber = ? WHERE SupplierID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, address);
            stmt.setString(3, contactNumber);
            stmt.setInt(4, supplierID);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to delete a supplier
    public boolean deleteSupplier(int supplierID) {
        String query = "DELETE FROM Supplier WHERE SupplierID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, supplierID);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to get a list of all suppliers
    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        String query = "SELECT * FROM Supplier";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("SupplierID");
                String name = rs.getString("SupplierName");
                String address = rs.getString("Address");
                String contactNumber = rs.getString("ContactNumber");
                suppliers.add(new Supplier(id, name, address, contactNumber));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }
}
