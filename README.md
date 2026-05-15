#### *This is an archived Course Project*

# Online Store

A JavaFX desktop app connected to a MySQL database, built for the Database Systems course at KSU. It's a learning project that connects our knowledge of coding with databases.

## What you can do

- Add, edit, delete, and search customers, products, orders, and shipping companies
- Track order status (Pending → Shipping → Delivered / Cancelled)
- Record payments (Credit Card, Bank Transfer, or Cash)
- Manage customer wishlists
- ComboBoxes for foreign key fields (e.g. picking a shipping company on an order)

## Tech

- **JavaFX** — GUI
- **MySQL** — database
- **JDBC** — connecting the two

## Setup
 
1. Run `OnlineStore.sql` in MySQL Workbench to create the schema
2. Copy `database.properties.example` → `database.properties` and fill in your MySQL username and password
3. Run the project from your IDE

## Structure

```
├── src/          # Java source + controllers
├── resources/    # FXML files and stylesheets, database.properties
├── docs/         # ER diagram
└── OnlineStore.sql
```
