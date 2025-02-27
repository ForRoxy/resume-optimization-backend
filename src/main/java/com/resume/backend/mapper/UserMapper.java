package com.resume.backend.mapper;

import com.resume.backend.entity.User;
import org.apache.ibatis.annotations.*;


@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(@Param("id") Long id);

    @Select("SELECT * FROM users WHERE openid = #{openid}")
    User findByOpenid(String openid);

    @Insert("INSERT INTO users(openid, nickname, avatar_url) VALUES(#{openid}, #{nickname}, #{avatarUrl})")
    @Options(useGeneratedKeys = true, keyProperty = "id")  // 让 MyBatis 生成主键 ID
    int insertUser(User user); // 传入 User 对象

    @Update("UPDATE users SET resume_file = #{resumeFile} WHERE id = #{id}")
    int updateResumeFile(@Param("id") Long id, @Param("resumeFile") String resumeFile);
}

