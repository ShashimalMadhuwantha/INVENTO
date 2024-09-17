
CREATE DATABASE inventory;


USE inventory;


CREATE TABLE admin (
    id INT AUTO_INCREMENT PRIMARY KEY,  
    username VARCHAR(50) NOT NULL,       
    password VARCHAR(255) NOT NULL      
);



INSERT INTO admin (username, password) VALUES ('shashimal', '2003');

CREATE TABLE Supplier (
    SupplierID INT AUTO_INCREMENT PRIMARY KEY,
    SupplierName VARCHAR(255) NOT NULL,
    Address VARCHAR(255),
    ContactNumber VARCHAR(50)
);
INSERT INTO Supplier (SupplierName, Address, ContactNumber)
VALUES ('Supplier Name', 'Supplier Address', 'Supplier Contact Number');

CREATE TABLE Item (
    ItemID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Description TEXT,
    Price DECIMAL(10, 2) NOT NULL,
    Quantity INT NOT NULL,
    SupplierID INT,
    FOREIGN KEY (SupplierID) REFERENCES Supplier(SupplierID)
);

CREATE TABLE Buyer (
    BuyerID INT AUTO_INCREMENT PRIMARY KEY,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    PhoneNumber VARCHAR(20)
);

CREATE TABLE PurchaseItem (
    PurchaseID INT AUTO_INCREMENT PRIMARY KEY,
    BuyerID INT,
    ItemID INT,
    PurchaseDate DATE,
    Quantity INT,
    TotalPrice DECIMAL(10, 2),
    Status VARCHAR(20) NOT NULL DEFAULT 'Pending', 
    FOREIGN KEY (BuyerID) REFERENCES Buyer(BuyerID),
    FOREIGN KEY (ItemID) REFERENCES Item(ItemID)
);
use inventory;
INSERT INTO admin (username, password) VALUES ('admin', 'admin123');
select * from admin;