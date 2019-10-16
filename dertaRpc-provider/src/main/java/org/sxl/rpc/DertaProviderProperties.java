package org.sxl.rpc;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: shenxl
 * @Date: 2019/10/10 17:15
 * @Version 1.0
 * @description：${description}
 */
@Data
@ConfigurationProperties(prefix = "spring.derta")
public class DertaProviderProperties {

    private String zkAddressRegister;

    private Integer serverPort;

    private boolean provider;

}
