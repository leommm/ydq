package group.ydq.authority;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * =============================================
 *
 * @author wu
 * @create 2018-11-26 22:11
 * =============================================
 */
public class SubjectUtils {

    // 不允许实例化
    private SubjectUtils() {
    }

    private enum LoginStatus {
        ONLINE, LOGOUT;

    }

    private static Map<Subject, Object> dataMap = new ConcurrentHashMap<>();
    private static Map<Subject, LoginStatus> statusMap = new ConcurrentHashMap<>();
    private static Map<HttpSession, Subject> subjectMap = new ConcurrentHashMap<>();

    private static HttpServletRequest getHttpRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    private static HttpSession getHttpSession() {
        return getHttpRequest().getSession();
    }

    /**
     * 判断subject是否在线
     *
     * @param subject
     * @return
     */
    public static boolean isOnline(Subject subject) {
        LoginStatus status = statusMap.get(subject);
        return !Objects.isNull(status) && status.equals(LoginStatus.ONLINE);
    }

    /**
     * 根据当前线程获得subject对象
     *
     * @return
     */
    public static Subject getSubject() {
        HttpSession session = getHttpSession();
        Subject subject = subjectMap.get(session);
        if (!Objects.isNull(subject)) {
            return subject;
        } else {
            subject = new Subject();
            subjectMap.putIfAbsent(session, subject);
            return subject;
        }
    }


    /**
     * 在Subject上绑定数据
     *
     * @param subject
     * @param object
     */
    static void bind(Subject subject, Object object) {
        if (isOnline(subject)) {
            dataMap.put(subject, object);
        }
    }


    static boolean login(Subject subject) {
        if (isOnline(subject)) {
            return true;
        }
        if (AuthorityManager.checkSubject(subject)) {
            statusMap.put(subject, LoginStatus.ONLINE);
            return true;
        } else {
            return false;
        }
    }

    static boolean logout(Subject subject) {
        if (isOnline(subject)) {
            statusMap.put(subject, LoginStatus.LOGOUT);
            return true;
        }
        return false;
    }
}