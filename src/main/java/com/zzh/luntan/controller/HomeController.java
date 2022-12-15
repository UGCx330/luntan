package com.zzh.luntan.controller;

import com.zzh.luntan.entity.PageHelper;
import com.zzh.luntan.entity.Post;
import com.zzh.luntan.entity.User;
import com.zzh.luntan.service.PostService;
import com.zzh.luntan.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    PostService postService;
    @Autowired
    UserService userService;

    // 路径中的参数名current与方法中的属性current名字一致，可以自动注入
    @GetMapping(path = "/index")
    public String index(PageHelper pageHelper, Model model) {
        // 设置总页数
        pageHelper.setRows(postService.postCounts(0));
        // 搜索id为0的所有帖子
        List<Post> postList = postService.selectPostByUserId(0, pageHelper.getOffSet(), pageHelper.getLimit());
        HashMap<Post, User> map = new LinkedHashMap<>();
        // 存入map，渲染index
        for (Post post : postList) {
            map.put(post, userService.selectUserById(post.getUserId()));
        }
        model.addAttribute("map", map);
        return "index";
    }
}
