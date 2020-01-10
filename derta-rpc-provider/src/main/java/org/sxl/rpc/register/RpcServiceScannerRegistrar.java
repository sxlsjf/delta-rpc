package org.sxl.rpc.register;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.sxl.rpc.ann.DeltaService;
import org.sxl.rpc.ann.ServiceScan;

import java.util.Optional;
import java.util.stream.Stream;


/**
 * @Author: shenxl
 * @Date: 2019/11/11 19:45
 * @Version 1.0
 * @description：
 */
public class RpcServiceScannerRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {

        AnnotationAttributes annoAttrs = AnnotationAttributes
                .fromMap(annotationMetadata.getAnnotationAttributes(ServiceScan.class.getName()));

        String[] basePackages = annoAttrs.getStringArray("basePackage");

        //是否使用默认的filter，使用默认的filter意味着只扫描那些类上拥有Component、Service、Repository或Controller注解的类。
        ClassPathScanningCandidateComponentProvider beanScanner =
                new ClassPathScanningCandidateComponentProvider(false);

        TypeFilter includeFilter = new AnnotationTypeFilter(DeltaService.class);
        beanScanner.addIncludeFilter(includeFilter);

        Optional.ofNullable(basePackages).ifPresent((x) ->
                Stream.of(x).forEach((t) -> beanScanner.findCandidateComponents(t).forEach((y -> {
                    //beanName通常由对应的BeanNameGenerator来生成，比如Spring自带的AnnotationBeanNameGenerator、DefaultBeanNameGenerator等，也可以自己实现。
                    String beanName = y.getBeanClassName();
                    beanDefinitionRegistry.registerBeanDefinition(beanName, y);
                }))));

    }
}
