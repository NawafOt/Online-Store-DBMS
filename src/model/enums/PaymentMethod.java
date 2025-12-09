package model.enums;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PaymentMethod {

    private static final Set<String> paymentMethods = new HashSet<>();

    private PaymentMethod() throws InstantiationException {
        throw new InstantiationException("util class. dont make an instance!");
    }

    public static Set<String> getAllMethod() {
        return Collections.unmodifiableSet(paymentMethods);
    }

    public static void register(String newMethodName) {
        paymentMethods.add(newMethodName);
    }

    public static void clearAllMethod() {
        paymentMethods.clear();
    }

    public static boolean isValidMethod(String MethodToCheck) {
        return paymentMethods.contains(MethodToCheck);
    }
}