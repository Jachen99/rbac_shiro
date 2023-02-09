package space.jachen.rbac_shiro;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * shiro 框架
 * @author JaChen
 * @date 2023/01/19
 */
@SpringBootApplication
@MapperScan("space.jachen.rbac_shiro.dao")
public class ShiroMain {
    public static void main(String[] args) {
        SpringApplication.run(ShiroMain.class);
    }
}