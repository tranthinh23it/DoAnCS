package Mail;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        GenerateOtp generateOtp = new GenerateOtp();
        OtpMessage otpMessage = new OtpMessage();
        String to = "";
        String otp = generateOtp.getOtp();
        String otpCheck = "";

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your email: ");
        to = sc.nextLine();

        otpMessage.setTo(to);
        otpMessage.setOtp(otp);
        otpMessage.send();

        System.out.println("Enter OTP sent to your email: ");
        otpCheck = sc.nextLine();
        if (otpCheck.equals(otp)) {
            //
            System.out.println("OTP is correct");
        } else {
            System.out.println("OTP is incorrect");
        }
    }
}
