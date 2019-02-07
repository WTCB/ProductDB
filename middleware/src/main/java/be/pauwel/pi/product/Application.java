
package be.pauwel.pi.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableTransactionManagement
//@EnableNeo4jRepositories
@SpringBootApplication
//@EnableAsync
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
