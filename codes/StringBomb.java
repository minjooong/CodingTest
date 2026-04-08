import java.util.*;

public class StringBomb {
    static Scanner sc = new Scanner(System.in);
    static String input;
    static String target;
    static StringBuilder sb;
    
    static void checkTarget() {
        for (int i = 0; i < input.length(); i++) {
            sb.append(input.charAt(i));
            
            if (sb.length() >= target.length()) {
                // 검사
                boolean isMatch = true;
                
                for (int j = 0; j < target.length(); j++) {
                    if (sb.charAt(sb.length() - target.length() + j) != target.charAt(j)) {
                        isMatch = false;
                        break;
                    }
                }
                
                if (isMatch) {
                    sb.delete(sb.length() - target.length(), sb.length());
                }
            }
        }
    }
    
    public static void stringBomb(String[] args) {
        input = sc.next();
        target = sc.next();
        
        sb = new StringBuilder();
        checkTarget();
        
        if (sb.length() == 0) System.out.println("FRULA");
        else System.out.println(sb.toString());
    }
}