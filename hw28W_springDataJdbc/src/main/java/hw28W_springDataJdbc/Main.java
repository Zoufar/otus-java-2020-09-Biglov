package hw28W_springDataJdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Main {
    public static void main(String[] args) {

        var context = SpringApplication.run(Main.class, args);

        context.getBean("initBD", InitBD.class).action();

    }
}
