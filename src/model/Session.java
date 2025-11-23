package model;

import javafx.stage.Stage;
import ui.customer.pages.CartPage;

import java.util.ArrayList;

/**
 * Session class to manage the current user's login state.
 * Uses Singleton pattern to ensure only one active session exists.
 * Tracks user role (Admin, Customer, Guest) and current user information.
 */
public class Session {

    private static Session instance;
    private static final ArrayList<Product> cartSelection = new ArrayList<>();

    public enum UserRole {
        GUEST,
        CUSTOMER,
        ADMIN
    }

    private UserRole currentRole;
    private Customer currentCustomer;
    private int adminId;
    private boolean isLoggedIn;


    private Session() {
        this.currentRole = UserRole.GUEST;
        this.currentCustomer = null;
        this.adminId = -1;
        this.isLoggedIn = false;
    }


    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }


    public void loginAsCustomer(Customer customer) {
        this.currentCustomer = customer;
        this.currentRole = UserRole.CUSTOMER;
        this.isLoggedIn = true;
        this.adminId = -1;
        cartSelection.clear();
        // System.out.println("Customer logged in: " + customer.getName());
    }


    public void loginAsAdmin(int adminId, String adminName) {
        this.adminId = adminId;
        this.currentRole = UserRole.ADMIN;
        this.isLoggedIn = true;
        this.currentCustomer = null;
        cartSelection.clear();
        // System.out.println("Admin logged in: " + adminName);
    }


    public void logout() {
        this.currentCustomer = null;
        this.currentRole = UserRole.GUEST;
        this.isLoggedIn = false;
        this.adminId = -1;
        cartSelection.clear();
        // System.out.println("User logged out");
    }


    public boolean isLoggedIn() {
        return isLoggedIn;
    }


    public boolean isGuest() {
        return currentRole == UserRole.GUEST;
    }


    public boolean isCustomer() {
        return currentRole == UserRole.CUSTOMER;
    }


    public boolean isAdmin() {
        return currentRole == UserRole.ADMIN;
    }


    public UserRole getCurrentRole() {
        return currentRole;
    }


    public Customer getCurrentCustomer() {
        return currentCustomer;
    }


    public int getCustomerId() {
        return (currentCustomer != null) ? currentCustomer.getCid() : -1;
    }


    public String getCustomerName() {
        return (currentCustomer != null) ? currentCustomer.getName() : "Guest";
    }


    public String getCustomerEmail() {
        return (currentCustomer != null) ? currentCustomer.getEmail() : null;
    }


    public int getAdminId() {
        return adminId;
    }


    public void updateCustomer(Customer customer) {
        if (isCustomer() && currentCustomer != null) {
            this.currentCustomer = customer;
            // System.out.println("Session customer updated");
        }
    }


    public String getDisplayName() {
        switch (currentRole) {
            case ADMIN:
                return "Admin";
            case CUSTOMER:
                return currentCustomer != null ? currentCustomer.getName() : "Customer";
            case GUEST:
            default:
                return "Guest";
        }
    }



    public void requireLogin() throws IllegalStateException {
        if (!isLoggedIn) {
            throw new IllegalStateException("User must be logged in to perform this action");
        }
    }


    public void requireAdmin() throws IllegalStateException {
        if (currentRole != UserRole.ADMIN) {
            throw new IllegalStateException("Admin permission required");
        }
    }

    public ArrayList<Product> getCart() {
        return cartSelection;
    }


    @Override
    public String toString() {
        return "Session{" +
                "role=" + currentRole +
                ", isLoggedIn=" + isLoggedIn +
                ", user='" + getDisplayName() + '\'' +
                '}';
    }
}