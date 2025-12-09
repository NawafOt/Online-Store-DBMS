package model.enums;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class OrderStatus {

    private static final Set<String> REGISTRY = new HashSet<String>();

    private OrderStatus() throws InstantiationException {
        throw new InstantiationException("util class. dont make an instance!");
    }

    public static Set<String> getAllStatus() {
        return Collections.unmodifiableSet(REGISTRY);
    }

    public static void register(String newStatusName) {
        REGISTRY.add(newStatusName.toUpperCase());
    }

    public static void clearAllStatus() {
        REGISTRY.clear();
    }

    public static boolean isValidStatus(String StatusToCheck) {
        return REGISTRY.contains(StatusToCheck.toUpperCase());
    }
}
