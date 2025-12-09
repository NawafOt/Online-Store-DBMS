package model.enums;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class OrderStatus {

    private static final Set<String> REGISTERY = new HashSet<String>();

    private OrderStatus() throws InstantiationException {
        throw new InstantiationException("util class. dont make an instance!");
    }

    public static Set<String> getAllStatus() {
        return Collections.unmodifiableSet(REGISTERY);
    }

    public static void register(String newStatusName) {
        REGISTERY.add(newStatusName.toUpperCase());
    }

    public static void clearAllStatus() {
        REGISTERY.clear();
    }

    public static boolean isValidStatus(String StatusToCheck) {
        return REGISTERY.contains(StatusToCheck.toUpperCase());
    }
}
