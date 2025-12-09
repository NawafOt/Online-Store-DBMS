package model.enums;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PaymentMethod {

    private static final Set<String> REGISTRY = new HashSet<>();

    private PaymentMethod() throws InstantiationException {
        throw new InstantiationException("util class. dont make an instance!");
    }

    public static Set<String> getAllMethod() {
        return Collections.unmodifiableSet(REGISTRY);
    }

    public static void register(String newMethodName) {
        REGISTRY.add(newMethodName.toUpperCase());
    }

    public static void clearAllMethod() {
        REGISTRY.clear();
    }

    public static boolean isValidMethod(String MethodToCheck) {
        return REGISTRY.contains(MethodToCheck.toUpperCase());
    }
}