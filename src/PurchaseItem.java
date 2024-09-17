import java.math.BigDecimal;
import java.util.Date;

public class PurchaseItem {
    private int purchaseID;
    private int buyerID;
    private int itemID;
    private Date purchaseDate;
    private int quantity;
    private BigDecimal totalPrice;
    private String status;

    public PurchaseItem(int purchaseID, int buyerID, int itemID, Date purchaseDate, int quantity, BigDecimal totalPrice, String status) {
        this.purchaseID = purchaseID;
        this.buyerID = buyerID;
        this.itemID = itemID;
        this.purchaseDate = purchaseDate;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    // Getters and Setters
    public int getPurchaseID() { return purchaseID; }
    public void setPurchaseID(int purchaseID) { this.purchaseID = purchaseID; }

    public int getBuyerID() { return buyerID; }
    public void setBuyerID(int buyerID) { this.buyerID = buyerID; }

    public int getItemID() { return itemID; }
    public void setItemID(int itemID) { this.itemID = itemID; }

    public Date getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(Date purchaseDate) { this.purchaseDate = purchaseDate; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
