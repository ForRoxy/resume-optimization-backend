package com.resume.backend.mapper;

import com.resume.backend.entity.Resume;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ResumeMapper {

    @Insert("INSERT INTO resumes(user_id, file_path) VALUES(#{userId}, #{filePath})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertResume(Resume resume);

    @Select("SELECT * FROM resumes WHERE user_id = #{userId}")
    List<Resume> findResumesByUserId(@Param("userId") Long userId);
}
