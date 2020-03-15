package org.sxl.rpc.container;
import lombok.Data;

import	java.util.concurrent.ConcurrentHashMap;

import java.util.Map;

/**
 * @Author: shenxl
 * @Date: 2019/9/30 10:27
 * @Version 1.0
 * @description：缓存本地服务
 */

@Data
public class LocalHandlerMap extends ConcurrentHashMap<String,Object>{

    //缓存本地实例
 //   private final Map<String, Object> handlers=new ConcurrentHashMap<> (16);

}





