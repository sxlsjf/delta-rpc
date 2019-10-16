package org.sxl.rpc.container;
import lombok.Data;

import	java.util.concurrent.ConcurrentHashMap;

import java.util.Map;

/**
 * @Author: shenxl
 * @Date: 2019/9/30 10:27
 * @Version 1.0
 * @description：${description}
 */

@Data
public class LocalHandlerMap {

    //缓存本地实例
    private Map<String, Object> handlers=new ConcurrentHashMap<String, Object> ();

}





