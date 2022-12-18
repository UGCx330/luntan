package com.zzh.luntan.controller;

import com.zzh.luntan.entity.User;
import com.zzh.luntan.service.UserService;
import com.zzh.luntan.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String getRegisterPage() {
        return "/site/register";
    }

    // 注册
    @PostMapping("/register")
    public String register(Model model, User user) {
        Map<String, String> map = userService.register(user);
        if (map == null || map.isEmpty()) {
            // 注册成功，发送邮件后跳转到操作界面，再跳转到主界面
            model.addAttribute("msg", "注册成功,我们已经向您的邮箱发送了一封激活邮件,请尽快激活!");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        }
        model.addAttribute("usernameMsg", map.get("usernameMsg"));
        model.addAttribute("passwordMsg", map.get("passwordMsg"));
        model.addAttribute("emailMsg", map.get("emailMsg"));
        // 如果注册失败，自动注入user到Model，回填用户输入的错误的信息
        return "/site/register";
    }

    // 激活
    @GetMapping("/activation/{userId}/{activationCode}")
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("activationCode") String activationCode) {
        int result = userService.activation(userId, activationCode);
        // 激活成功则跳转到登陆界面
        if (result == ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "激活成功,您的账号已经可以正常使用了!");
            model.addAttribute("target", "/login");
        } else if (result == ACTIVATION_REPEAT) {
            model.addAttribute("msg", "无效操作,该账号已经激活过了!");
            model.addAttribute("target", "/index");
        } else {
            model.addAttribute("msg", "激活失败,您提供的激活码不正确!");
            model.addAttribute("target", "/index");
        }
        return "/site/operate-result";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "/site/login";
    }


}
