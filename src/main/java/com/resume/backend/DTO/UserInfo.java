package com.resume.backend.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfo {
    private String avatarUrl;
    private String nickName;
    private int gender;
    private String language;
    private String city;
    private String province;
    private String country;

}
