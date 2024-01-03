package audit.commons.gateway;

import org.springframework.boot.SpringApplication;

import java.text.ParseException;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.context.ApplicationContext;

import lombok.extern.slf4j.Slf4j;

//@EnableRedisHttpSession
@SpringBootApplication
@Slf4j
public class AuditGatewayApplication {

	public static void main(String[] args) throws ParseException {
		SpringApplication application = new SpringApplication(AuditGatewayApplication.class);
		application.setBannerMode(Mode.OFF);
		ApplicationContext context = application.run(args);

		MavenProperties mvnObj = context.getBean(MavenProperties.class);
		log.info("Application build time is {}", mvnObj.getPackage_time());
		
	}
}