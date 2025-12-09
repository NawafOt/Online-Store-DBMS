package model.enums;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class OrderStatus {

    private static final Set<String> orderStatus = new HashSet<String>();

    private OrderStatus() throws InstantiationException {
        throw new InstantiationException("util class. dont make an instance!");
    }

    public static Set<String> getAllStatus() {
        return Collections.unmodifiableSet(orderStatus);
    }

    public static void register(String newStatusName) {
        orderStatus.add(newStatusName);
    }

    public static void clearAllStatus() {
        orderStatus.clear();
    }

    public static boolean isValidStatus(String StatusToCheck) {
        return orderStatus.contains(StatusToCheck);
    }
}
