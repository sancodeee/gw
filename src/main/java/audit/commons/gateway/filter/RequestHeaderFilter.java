package audit.commons.gateway.filter;

import audit.commons.gateway.commons.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.server.mvc.filter.HttpHeadersFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.function.ServerRequest;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Component
@Slf4j
public class RequestHeaderFilter implements HttpHeadersFilter.RequestHttpHeadersFilter, Ordered {

    @Autowired
    private HttpServletRequest request;

    @Override
    public HttpHeaders apply(HttpHeaders headers, ServerRequest serverRequest) {
        StopWatch sw = new StopWatch();
        sw.start();
        // 清除请求原始header信息
        HttpHeaders t = RequestUtils.clearHeader(headers);

        // 1.把session中以MG前缀的信息全部代入下级header
        Enumeration<String> enu = request.getSession().getAttributeNames();
        List<String> keylogs = new ArrayList<String>();
        if (enu != null) {
            while (enu.hasMoreElements()) {
                String key = enu.nextElement();
                // 如果是mg-开头的
                if (RequestUtils.isSystemKey(key)) {
                    // 获取mg-开头的key的value
                    Object value = request.getSession().getAttribute(key);
                    if (value instanceof String) {
                        t.add(key, (String) value);// t
                        keylogs.add(key);
                    }
                }
            }
        }
        sw.stop();
        log.debug("method = {}, uri = {}, request header names is {}, add request header names is {}, {}ns", request.getMethod(), request.getRequestURI(), headers.keySet(), keylogs, sw.getTotalTimeNanos());
        return t;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}