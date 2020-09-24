package it.geosolutions.swgeoserver.entry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


@ApiModel(value="用户",description="用户")
public class User implements Serializable {

    private static final long serialVersionUID = 420194691842135568L;

    @ApiModelProperty(value = "用户名",dataType = "String")
    private String username;

    @ApiModelProperty(value = "密码",dataType = "String")
    private String password;

    @ApiModelProperty(value = "启用",dataType = "String")
    private String enabled;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }
}
