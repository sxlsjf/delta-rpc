# deta-rpc
a sample demo for rpc


# deta-rpc
a sample demo for rpc

1.基于netty高性能NIO通信框架
2.基于全注解使用
3.实现自动装配
4.使用zookeeper注册中心
5.对spring进行扩展
6.将服务端和客户端进行解耦，分别引入不同和jar包
7.同步调用和异步调用分别实现
8.使用连接池缓存tcp连接
9.基于JDK8，大量使用lamada表达式，以及函数接口
10.不同分支侧重的功能不同，以及代码优化。
分支介绍：
https://github.com/sxlsjf/delta-rpc/tree/future-promise-2.1.0这个分支是采用promise编程范式实现的异步调用，https://github.com/sxlsjf/delta-rpc/tree/feature-asyn-v2.0此分支分别实现同步异步调用，与采用promise的方式稍有不同

测试步骤
1.开启zookeeper，地址127.0.0.1：2181
2.启动服务端代码，spring boot项目

@SpringBootApplication
@ServiceScan(basePackage = {"com.sxl.sample.test.provider"})
public class TestProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestProviderApplication.class, args);
    }

}
服务端测试案列

@DeltaService(value = AsyncService.class,version = "1.0.0")
public class AsynServiceImpl implements AsyncService {

    public String asyncHello(String say) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("异步调用"+say+"=========================================");

        return "异步调用rpc成功";
    }
}

@DeltaService(value = MyService.class,version = "1.0.0")
public class MyServiceImpl implements MyService {


    public String sayHello(String say) {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("同步调用"+say+"==========================================");

        return "同步rpc调用成功";

    }
}

3.启动消费端代码。spring boot项目
@SpringBootApplication
public class TestConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestConsumerApplication.class, args);
    }

}
消费端测试案例：
@RestController
public class MyController {

    @RpcReference(version = "1.0.0")
    private MyService myservice;

    @RpcReference(version = "1.0.0",interfaceClass = AsyncService.class)
    private IAsyncProxyObject<AsyncService> asyncObjectProxy;

    @RequestMapping("/index")
    public String say(){

        String str=myservice.sayHello(" world");
        System.out.println("同步调用当前线程："+Thread.currentThread().getName());
        System.out.println("同步调用结果===================="+str);


        RPCFuture future=asyncObjectProxy.call("asyncHello","shenxl");
        future.success((v)->{
            System.out.println("异步调用当前线程："+Thread.currentThread().getName());
            System.out.println("异步调用结果："+v);
        }).fail((e)-> System.out.println("异步调用结果"+e));

        return "success";

    }

}

4.项目结构





