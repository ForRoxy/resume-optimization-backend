package com.resume.backend.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "users")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 微信的唯一标识符，必须非空且唯一
    @Column(nullable = false, unique = true, length = 255)
    private String openid;

    // 微信用户昵称
    @Column(length = 100)
    private String nickname;

    // 微信头像 URL
    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    // 用户上传的简历文件存储路径
    @Column(name = "resume_file", length = 255)
    private String resumeFile;

    // 用户创建时间
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    // 用户信息最后更新时间
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    // 构造函数、Getter、Setter
    public User() {}

    public User(String openid, String nickname, String avatarUrl) {
        this.openid = openid;
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
    }

    public Long getId() {
        return id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getResumeFile() {
        return resumeFile;
    }

    public void setResumeFile(String resumeFile) {
        this.resumeFile = resumeFile;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", openid='" + openid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", resumeFile='" + resumeFile + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

