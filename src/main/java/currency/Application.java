package currency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        System.out.println("~~~~~ Currency Rate Web-Service v.1 Started ~~~~~");
	ExchangeRate.constructCodesMap();
        SpringApplication.run(Application.class, args);
    }
}
