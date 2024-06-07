package Mail;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.Random;

public class GenerateOtp {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final AtomicReference<String> value = new AtomicReference<>();

    public static String getOtp() {
        // Generate and store a random 6-digit number
        String randomSixDigitNumber = generateRandomSixDigitNumber();
        setValue(randomSixDigitNumber);
        System.out.println("Generated value: " + randomSixDigitNumber);

        // Schedule to clear the value after 2 minutes
        scheduler.schedule(() -> {
            clearValue();
            System.out.println("Value cleared after 2 minutes");
        }, 2, TimeUnit.MINUTES);
        return randomSixDigitNumber;
    }

    public static String generateRandomSixDigitNumber() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000);
        return String.valueOf(number);
    }

    public static void setValue(String newValue) {
        value.set(newValue);
    }
    public static String getValue() {
        return value.get();
    }
    public static void clearValue() {
        value.set(null);
    }
}

