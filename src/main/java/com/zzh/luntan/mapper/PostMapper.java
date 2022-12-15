package com.zzh.luntan.mapper;

import com.zzh.luntan.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper {
    List<Post> selectPost(@Param("userId") int userId, int offset, int limit);

    int postCounts(@Param("userId") int userId);

}
