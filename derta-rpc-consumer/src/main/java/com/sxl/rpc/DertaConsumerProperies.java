package com.sxl.rpc;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: shenxl
 * @Date: 2019/10/10 23:29
 * @Version 1.0
 * @description
 */
@Data
@ConfigurationProperties(prefix = "spring.derta")
public class DertaConsumerProperies {

    /**
     *zk地址
     */
    private String zkAddressDiscover;

    private boolean consumer;
}
