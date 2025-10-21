package src;

import java.util.Scanner;

public class Utils {
    private static Scanner sc = new Scanner(System.in);

    public static int getInt(String msg) {
        System.out.print(msg);
        return sc.nextInt();
    }

    public static String getString(String msg) {
        System.out.print(msg);
        return sc.next();
    }
}
