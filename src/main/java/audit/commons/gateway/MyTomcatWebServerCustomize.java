package audit.commons.gateway;

import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

/**
 * 自定义Tomcat配置
 *
 * @author 王新宁
 * @version 1.0
 * @date 2023/12/28 13:54
 */
@Component
public class MyTomcatWebServerCustomize implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    private int maxParameterCount = 10000;

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        PropertyMapper propertyMapper = PropertyMapper.get();
        propertyMapper.from(this::getMaxParameterCount).when(v -> true).to(v -> customizerProperty(factory));
    }

    /**
     * params特殊字符过滤
     *
     * @param factory 工厂
     * @author Atomicyo
     * @date 2021/11/3 16:44
     */
    private void customizerProperty(TomcatServletWebServerFactory factory) {
        factory.addConnectorCustomizers(connector -> connector.setProperty("relaxedQueryChars", "[]{}="));
    }

    public void setMaxParameterCount(int maxParameterCount) {
        this.maxParameterCount = maxParameterCount;
    }

    public int getMaxParameterCount() {
        return maxParameterCount;
    }
}