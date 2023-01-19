package space.jachen.rbac_shiro.controller;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.jachen.rbac_shiro.utils.JsonData;


@RestController
public class LogoutController {


    @RequestMapping("/logout")
    public JsonData findMyPlayRecord(){

        Subject subject = SecurityUtils.getSubject();

        if(subject.getPrincipals() != null ){

        }

        SecurityUtils.getSubject().logout();

        return JsonData.buildSuccess("logout成功");

    }

}
