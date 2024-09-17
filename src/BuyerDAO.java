import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BuyerDAO {
    private Connection connection;

    public BuyerDAO() {

        try {
            String url = "jdbc:mysql://localhost:3308/inventory";
            String user = "root";
            String password = "2003";
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addBuyer(Buyer buyer) {
        String sql = "INSERT INTO Buyer (FirstName, LastName, PhoneNumber) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, buyer.getFirstName());
            stmt.setString(2, buyer.getLastName());
            stmt.setString(3, buyer.getPhoneNumber());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateBuyer(Buyer buyer) {
        String sql = "UPDATE Buyer SET FirstName = ?, LastName = ?, PhoneNumber = ? WHERE BuyerID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, buyer.getFirstName());
            stmt.setString(2, buyer.getLastName());
            stmt.setString(3, buyer.getPhoneNumber());
            stmt.setInt(4, buyer.getBuyerID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBuyer(int buyerID) {
        String sql = "DELETE FROM Buyer WHERE BuyerID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, buyerID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Buyer> getAllBuyers() {
        List<Buyer> buyers = new ArrayList<>();
        String sql = "SELECT * FROM Buyer";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                buyers.add(new Buyer(
                        rs.getInt("BuyerID"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("PhoneNumber")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buyers;
    }
}
