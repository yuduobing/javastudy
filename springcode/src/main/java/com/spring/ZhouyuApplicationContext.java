package com.spring;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ZhouyuApplicationContext {

    private Class configClass;
    //2  单例bean在获取bean时获取
    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>(); // 单例池
    // 1存放bean定义，存放名字和bean定义  beanDefinitionMap
    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    //3后置处理器
    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public ZhouyuApplicationContext(Class configClass) throws MalformedURLException {
        this.configClass = configClass;

        //  # 解析配置类
        // 1 configClass @ComponentScan("com.zhouyu.service")
        //public class AppConfig {} 告诉我们哪个文件夹是
//        2 哪 个加注解哪个是
        // ComponentScan注解--->扫描路径--->扫描--->Beandefinition--->BeanDefinitionMap

        scan(configClass);

        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            if (beanDefinition.getScope().equals("singleton")) {
                Object bean = createBean(beanName, beanDefinition); // 单例Bean
                singletonObjects.put(beanName, bean);
            }
        }


    }
    //3创造bean
    public Object createBean(String beanName, BeanDefinition beanDefinition) {

        Class clazz = beanDefinition.getClazz();
        try {

            //3.1得到一个对象
            Object instance = clazz.getDeclaredConstructor().newInstance();

            // 依赖注入
            for (Field declaredField : clazz.getDeclaredFields()) {
                if (declaredField.isAnnotationPresent(Autowired.class)) {
                    Object bean = getBean(declaredField.getName());
//                    通过反射拿到方法
                    declaredField.setAccessible(true);
                    declaredField.set(instance, bean);
                }
            }

            // Aware回调
            if (instance instanceof BeanNameAware) {
                ((BeanNameAware) instance).setBeanName(beanName);
            }
//实例化的前值方法，判断用户是否写
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
            }

            // 初始化
            if (instance instanceof InitializingBean) {
                try {
                    ((InitializingBean) instance).afterPropertiesSet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//实例化的后置方法，判断用户是否重写方法     实现aop
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessAfterInitialization(instance, beanName);
            }

            // BeanPostProcessor

            return instance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }
    //1 扫描所有bean
    private void scan(Class configClass) throws MalformedURLException {
        //## 1.1解析配置类  得到类上面的注解
        ComponentScan componentScanAnnotation = (ComponentScan) configClass.getDeclaredAnnotation(ComponentScan.class);

        String path = componentScanAnnotation.value(); // 扫描路径 com.zhouyu.service
        path = path.replace(".", "/");
        //得到系统相对路径
        ClassLoader classLoader = ZhouyuApplicationContext.class.getClassLoader();
        URL resource = classLoader.getResource(path);
        String a=resource.toString();
        URL uri = new URL(a);
        //得到路径文件包含所有文件夹
        File file = new File(uri.getFile());
        System.out.println(file.isDirectory());
//        if (file.isDirectory()) {
//            遍历所有指定扫描类文件
            File[] files = file.listFiles();

            for (File f : files) {
                String fileName = f.getAbsolutePath();
                if (fileName.endsWith(".class")) {
//                   得到com.zhouyu,service,a,class
                    String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));

                    className = className.replace("\\", ".");
                //1.2得到扫描的类信息，初始化类表
                    try {

                        Class<?> clazz = classLoader.loadClass(className);
                        // 表示当前这个类是一个Bean
                        // 解析类--->BeanDefinition
                        if (clazz.isAnnotationPresent(Component.class)) {


                            if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                                BeanPostProcessor instance = (BeanPostProcessor) clazz.getDeclaredConstructor().newInstance();
                                beanPostProcessorList.add(instance);
                            }
//                        1.21得到用户bean自定的名称
                            Component componentAnnotation = clazz.getDeclaredAnnotation(Component.class);
                            String beanName = componentAnnotation.value();

                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setClazz(clazz);
//                          1.22  判断是不是一个单例bean
                            if (clazz.isAnnotationPresent(Scope.class)) {
//                                拿到用户设置scope注解
                                Scope scopeAnnotation = clazz.getDeclaredAnnotation(Scope.class);
//                                放到作用域
                                beanDefinition.setScope(scopeAnnotation.value());
                            } else {
//                                默认单例放到作用域
                                beanDefinition.setScope("singleton");
                            }

                            beanDefinitionMap.put(beanName, beanDefinition);

                        }

                    } catch (ClassNotFoundException | NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                }
            }
//        }
    }
// 2 得到bean
    public Object getBean(String beanName) {
        if (beanDefinitionMap.containsKey(beanName)) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (beanDefinition.getScope().equals("singleton")) {
//                单例从单例池获取
                Object o = singletonObjects.get(beanName);
                return o;
            } else {
                Object bean = createBean(beanName, beanDefinition);
                return bean;
            }

        } else {
            // 不存在对应的Bean
            throw new NullPointerException();
        }
    }
}
