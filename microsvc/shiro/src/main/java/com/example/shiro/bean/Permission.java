package com.example.shiro.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Data
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
public class Permission {
    private String id;
    private String permissionname;

}
