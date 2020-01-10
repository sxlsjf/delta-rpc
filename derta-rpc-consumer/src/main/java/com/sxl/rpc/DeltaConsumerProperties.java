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
@ConfigurationProperties(prefix = "spring.delta")
public class DeltaConsumerProperties {

    /**
     * zk地址
     */
    private String zkAddressDiscover;

    /**
     * 是否开启客户端
     */
    private boolean consumer;

    /**
     * 最大空闲连接数
     */
    private Integer poolMaxIdle;

    /**
     * 最大连接数
     */
    private Integer poolMaxTotal;
}
