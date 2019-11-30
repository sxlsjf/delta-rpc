package org.sxl.rpc.utils;

import com.sxl.common.core.bean.RpcRequest;
import com.sxl.common.core.util.StringUtil;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import org.sxl.rpc.container.LocalHandlerMap;

/**
 *
 */
public class InvokeServiceUtil {

    /**
     * 从spring中取出实现类 反射调用相应实现类并结果
     * @param request
     * @return
     */
    public static Object invoke(RpcRequest request, LocalHandlerMap localHandlerMap)throws Exception{

        // 获取服务对象
        String serviceName = request.getInterfaceName();
        String serviceVersion = request.getServiceVersion();
        if (StringUtil.isNotEmpty(serviceVersion)) {
            serviceName += "-" + serviceVersion;
        }
        Object serviceBean = localHandlerMap.getHandlers().get(serviceName);

        if(serviceBean==null){
            throw new RuntimeException(String.format("can not find service bean by key: %s",serviceName));
        }

        // 获取反射调用所需的参数
        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();
        // 使用 CGLib 执行反射调用
        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
        return serviceFastMethod.invoke(serviceBean, parameters);

    }

}
