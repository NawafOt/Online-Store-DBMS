package model.enums;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PaymentMethod {

    private static final Set<String> REGISTERY = new HashSet<>();

    private PaymentMethod() throws InstantiationException {
        throw new InstantiationException("util class. dont make an instance!");
    }

    public static Set<String> getAllMethod() {
        return Collections.unmodifiableSet(REGISTERY);
    }

    public static void register(String newMethodName) {
        REGISTERY.add(newMethodName.toUpperCase());
    }

    public static void clearAllMethod() {
        REGISTERY.clear();
    }

    public static boolean isValidMethod(String MethodToCheck) {
        return REGISTERY.contains(MethodToCheck.toUpperCase());
    }
}