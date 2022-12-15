package com.zzh.luntan.service;

import com.zzh.luntan.entity.Post;
import com.zzh.luntan.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @Autowired
    PostMapper postMapper;

    public List<Post> selectPostByUserId(int userId, int offset, int limit) {
        return postMapper.selectPost(userId, offset, limit);
    }

    public int postCounts(int userId) {
        return postMapper.postCounts(userId);
    }
}
