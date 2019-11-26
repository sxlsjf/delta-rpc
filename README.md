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

public interface PromiseService {

    Promise<String> sayHello(String say);
}


@DeltaService(value = PromiseService.class,version = "1.0.0")
public class PromiseServiceImpl implements PromiseService {


    public Promise<String> sayHello(String say) {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("打印消费端传过来的参数："+say);

        return new LightDeferred<String>(say);
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
    private PromiseService promiseService;


    @RequestMapping("/index")
    public String say(){


        System.out.println("同步调用当前线程："+Thread.currentThread().getName());

        promiseService.sayHello("i love you").then(System.out::println)
                .then(t-> System.out.println("result:"+t))
                .then(t-> System.out.println("reslut2:"+t))
                .onSuccess(t-> System.out.println("成功执行"))
                .onFail(e->{
                    throw new RuntimeException();
                });

        return "success";

    }

}



4.项目结构





