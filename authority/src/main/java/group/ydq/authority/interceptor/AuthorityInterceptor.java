package group.ydq.authority.interceptor;

import group.ydq.authority.AuthorityManager;
import group.ydq.authority.PatternMatcher;
import group.ydq.authority.Subject;
import group.ydq.authority.SubjectUtils;
import group.ydq.authority.annotion.Unlimited;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * =============================================
 *
 * @author wu
 * @create 2018-11-11 22:34
 * =============================================
 */
public class AuthorityInterceptor implements HandlerInterceptor {

    private static final String SUBJECT_ID = "subjectId";

    private static final String STATUS_ONLINE = "online";
    @Autowired
    private AuthorityManager authorityManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        // 获取handlerMethod对象
        HandlerMethod handlerMethod = null;
        if (handler instanceof HandlerMethod) {
            handlerMethod = (HandlerMethod) handler;
        }

        // 1. 检查方法是否有Unlimited注解，有的话则不做权限检查
        if (!Objects.isNull(handlerMethod)) {
            if (handlerMethod.hasMethodAnnotation(Unlimited.class)) {
                return true;
            }
        }

        // 2. 检查是否在配置的权限检查路径下
        PatternMatcher matcher = authorityManager.getPatternMatcher();
        String requestUrl = request.getRequestURI();
        for (String path : authorityManager.getDefaultScanPath()) {
            if (matcher.match(requestUrl, path)) {
                // 3. 检查登陆状态
                Subject subject = SubjectUtils.getSubject();
                if (!SubjectUtils.isOnline(subject)) {
                    response.sendRedirect(authorityManager.getIndexPath());
                    return false;
                }
                // 4. 检查权限
                if (authorityManager.checkPermission(subject, requestUrl)) {
                    return true;
                } else {
                    response.sendRedirect(authorityManager.getIndexPath());
                    return false;
                }

            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }
}
