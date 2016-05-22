package GoOn.CryptoUtility;

import java.util.Scanner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class App 
{
    public static void main( String[] args )
    {
    	Scanner s = new Scanner(System.in); 
    	System.out.println("Ingrese password:");
    	String password = s.nextLine();
    	BCryptPasswordEncoder cryptor = new BCryptPasswordEncoder();
    	String hashedPassword = cryptor.encode(password);
    	System.out.println("El password hashed es: " + hashedPassword);
    	s.hasNextLine();
    }
}
