package com.rollking.lightcloud.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;


/*
 * @author Bernard
 * @mail bernard5@qq.com
 * @date 2021-11
 * */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class User {
    @Id
    private int idx;

    private String email;

    private String password;

    private String username;

    private long used;
}

