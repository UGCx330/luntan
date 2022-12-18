package com.zzh.luntan.service;

import com.zzh.luntan.entity.Post;
import com.zzh.luntan.entity.User;
import com.zzh.luntan.mapper.PostMapper;
import com.zzh.luntan.mapper.UserMapper;
import com.zzh.luntan.util.CommunityConstant;
import com.zzh.luntan.util.CommunityUtil;
import com.zzh.luntan.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService implements CommunityConstant {
    @Autowired
    private TemplateEngine templateEngine;
    @Value("${zzh.path.domain}")
    String community;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Autowired
    private MailClient mailClient;
    @Autowired
    UserMapper userMapper;

    public User selectUserById(int userId) {
        return userMapper.selectById(userId);
    }

    public Map<String, String> register(User user) {
        // 判空处理
        HashMap<String, String> map = new HashMap<>();
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空");
            return map;
        }

        // 重复处理
        if (userMapper.selectByUsername(user.getUsername()) != null) {
            map.put("usernameMsg", "该用户名已被使用");
            return map;
        }
        if (userMapper.selectByEmail(user.getEmail()) != null) {
            map.put("emailMsg", "该邮箱已被注册");
            return map;
        }

        // 插入注册用户
        String salt = CommunityUtil.generateUUID().substring(0, 5);
        user.setSalt(salt);
        user.setPassword(CommunityUtil.MD5(user.getPassword() + salt));
        user.setType(0);
        user.setStatus(0);
        String activationCode = CommunityUtil.generateUUID();
        user.setActivationCode(activationCode);
        user.setHeaderUrl("https://joeschmoe.io/api/v1/random");
        user.setCreateTime(new Date());
        // 插入数据，同时将主键id返回注入
        userMapper.insertUser(user);

        // 渲染html体
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // http://www.nowcoder.com/activation/abcdefg123456.html
        context.setVariable("target", community + contextPath + "/activation/" + user.getId() + "/" + activationCode);
        String process = templateEngine.process("/mail/activation", context);

        // 发送html邮件
        mailClient.sendMail(user.getEmail(), "激活账号", process);

        return map;
    }

    public int activation(int userId, String activationCode) {
        User user = userMapper.selectById(userId);
        // 激活失败：重复激活，激活码不对失败
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(activationCode)) {
            // 激活
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;
        }
    }

}
