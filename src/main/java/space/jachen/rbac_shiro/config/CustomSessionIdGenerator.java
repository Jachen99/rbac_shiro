package space.jachen.rbac_shiro.config;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

import java.io.Serializable;
import java.util.UUID;

/**
 *
 * 自定义session
 *
 * @author JaChen
 * @date 2023/1/20 13:55
 */
public class CustomSessionIdGenerator implements SessionIdGenerator {
    @Override
    public Serializable generateId(Session session) {

        return "jachen"+UUID.randomUUID().toString().replace("-","");
    }
}
