package audit.commons.gateway;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "maven", ignoreUnknownFields = false)
@PropertySource(value = "classpath:remote.properties", encoding = "utf-8")
@Data
@Component
public class MavenProperties {
	/** maven打包时间 */
	private String package_time;
}