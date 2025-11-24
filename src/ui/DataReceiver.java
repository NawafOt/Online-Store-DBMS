package ui;

/**
 * An interface for controllers that can receive data when loaded by the PageManager.
 */
public interface DataReceiver {
    /**
     * Called by the PageManager to pass data to the controller after the FXML has been loaded.
     * @param data The data object being passed.
     */
    void receiveData(Object data);
}
