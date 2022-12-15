package com.zzh.luntan.service;

import com.zzh.luntan.entity.User;
import com.zzh.luntan.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public User selectUserById(int userId) {
        return userMapper.selectById(userId);
    }
}
