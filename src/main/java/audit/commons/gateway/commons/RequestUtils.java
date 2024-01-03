package audit.commons.gateway.commons;

import audit.commons.gateway.Constant;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RequestUtils {

    /**
     * 请求头中 MG-Service-Id 值为9开头，系统定义为系统接口，过滤器会截取响应头信息中以MG-开头的信息保存到session中
     *
     * @param request 请求
     * @return boolean
     */
    public static boolean isSystemRequest(HttpServletRequest request) {
        boolean ret = Boolean.FALSE;

        String serviceId = request.getHeader(Constant.Key_Serviceid);
        if (StringUtils.startsWith(serviceId, "9")) {
            ret = Boolean.TRUE;
        }

        return ret;
    }

    /**
     * 前缀为mg开头的，定义为系统内部header值
     *
     * @param key 关键
     * @return boolean
     */
    public static boolean isSystemKey(String key) {
        return StringUtils.startsWithIgnoreCase(key, Constant.Prefix_HeaderKey);
    }

    /**
     * 删除头信息中MG开头的内部传递数据
     * <p>
     * 对于request header，删除后向后传 对于response header，内部删除后才能返回到前端，避免泄密
     *
     * @param t t
     * @return {@link HttpHeaders}
     */
    public static HttpHeaders clearHeader(HttpHeaders t) {

        HttpHeaders ret = new HttpHeaders();
        ret.addAll(t);

        Iterator<String> ite = ret.keySet().iterator();
        List<String> filterKeys = new ArrayList<String>();
        while (ite.hasNext()) {
            String key = ite.next();
            if (StringUtils.startsWithIgnoreCase(key, Constant.Prefix_HeaderKey)) {
                filterKeys.add(key);
            }
        }
        for (String key : filterKeys) {
            ret.remove(key);
        }

        return ret;
    }
}