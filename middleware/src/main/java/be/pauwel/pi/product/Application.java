
package be.pauwel.pi.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import be.pauwel.pi.product.viewmodel.servers.Server;

//@EnableTransactionManagement
//@EnableNeo4jRepositories
@SpringBootApplication
//@EnableAsync
public class Application {

	public static void main(String[] args) {
		Server.initiate();
		SpringApplication.run(Application.class, args);
	}
}
