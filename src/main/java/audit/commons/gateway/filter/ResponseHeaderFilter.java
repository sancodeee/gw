package audit.commons.gateway.filter;

import audit.commons.gateway.commons.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.server.mvc.filter.HttpHeadersFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@Slf4j
public class ResponseHeaderFilter implements HttpHeadersFilter.ResponseHttpHeadersFilter, Ordered {

    @Autowired
    private HttpServletRequest request;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public HttpHeaders apply(HttpHeaders t, ServerResponse resp) {
        StopWatch sw = new StopWatch();
        sw.start();

        // 系统链接并且响应成功，session中加入用户
        List<String> keylogs = new ArrayList<String>();
        // 系统请求才会进行响应header处理
        if (resp.statusCode().is2xxSuccessful() && RequestUtils.isSystemRequest(request)) {
            // 所有MG开头响应，全部写入session
            Iterator<String> ite = t.keySet().iterator();
            while (ite.hasNext()) {
                String key = ite.next();
                if (RequestUtils.isSystemKey(key)) {
                    String value = t.getFirst(key);
                    key = StringUtils.lowerCase(key);
                    request.getSession().setAttribute(key, value);
                    keylogs.add(key);
                }
            }
        }
        sw.stop();
        // 打印响应头
        log.debug("method = {}, uri = {}, response header names is {}, add response header names is {}, {}ns", request.getMethod(), request.getRequestURI(), t.keySet(), keylogs, sw.getTotalTimeNanos());
        return RequestUtils.clearHeader(t);
    }
}