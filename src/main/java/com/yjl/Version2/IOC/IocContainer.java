package com.yjl.Version2.IOC;

import com.yjl.Version2.IOC.Bean;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: DLMU 袁佳林
 * @Date: 2024/3/14 13:53
 * @Description:
 **/

public class IocContainer {

    private final Map<String, Bean> beansMap = new ConcurrentHashMap<>();

    public IocContainer(String configName) {
        loadFromXML(configName);
    }

    //从xml里加载配置的bean
    private void loadFromXML(String config) {
        InputStream resourceAsStream = null;
        try {
            //获取一个类加载器
            ClassLoader ClassLoader = this.getClass().getClassLoader();
            //调用类加载器的getResourceAsStream将xml配置文件读取为流
            resourceAsStream = ClassLoader.getResourceAsStream(config);
            //实力化一个SAXReader类来解读流文件，SAXReader专门用于读取XML文件
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(resourceAsStream);
            //得到xml文件里的根元素：beans
            Element root = document.getRootElement();
            //获得beans下的子元素的迭代器
            Iterator iterator = root.elementIterator();
            //遍历beans下子元素
            while (iterator.hasNext()) {
                Element next = (Element) iterator.next();
                //获得各属性的值
                String id = next.attributeValue("id");
                String className = next.attributeValue("class");
                Bean bean = new Bean(id, className);

                //通过该方法将通过构造方法注入的值存入bean中
                parseConstructorArgElement(next, bean);
                //通过该方法将通过setter方法注入的值存入bean中
                parsePropertyElement(next, bean);
                //将beans的信息存入beansMap
                beansMap.put(id, bean);
            }
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } finally {
            if (resourceAsStream != null) {
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    private void parseConstructorArgElement(Element element, Bean bean) {
        Iterator iterator = element.elementIterator("constructor-arg");
        List<String> constructorArguments = bean.getConstructorArguments();
        while (iterator.hasNext()) {
            Element next = (Element) iterator.next();
            String string = next.attributeValue("ref");
            if (StringUtils.hasLength(string)) {
                constructorArguments.add(string);
            } else {
                return;
            }
        }
    }

    private void parsePropertyElement(Element element, Bean bean) {
        Iterator iterator = element.elementIterator("property");
        List<String> propertyNames = bean.getPropertyNames();
        List<String> propertyValue = bean.getPropertyValue();
        while (iterator.hasNext()) {
            Element next = (Element) iterator.next();
            String string = next.attributeValue("name");
            String string1 = next.attributeValue("value");
            if (!StringUtils.hasLength(string)) {
                return;
            }
            propertyNames.add(string);
            propertyValue.add(string1);

        }
    }

    public Object getBean(String beanId) {
        //最终创建的对象
        Object object = null;
        Bean bean = beansMap.get(beanId);
        //没有需要通过构造方法注入的参数，直接走无参构造，否则走else有参数构造
        if (bean.ConstructorArgumentsIsEmplty()) {
            //无参构造，与version1相同
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Class<?> aClass = contextClassLoader.loadClass(bean.getBeanClassName());
                return aClass.getConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            //有参构造的实现
            try {
                //通过ContextClassLoad 拿到需要创建的类的类对象
                Class<?> aClass = Thread.currentThread().getContextClassLoader().loadClass(bean.getBeanClassName());
                //通过这个类对象拿到类中声明的构造函数
                Constructor<?>[] declaredConstructors = aClass.getDeclaredConstructors();

                Constructor<?> use = null;//找到合适的构造函数后用use来表示
                Object[] args = null;//args用来存储构造函数需要用到的参数

                //遍历构造函数，找到合适的构造函数
                for (Constructor<?> declaredConstructor : declaredConstructors) {
                    int parameterCount = declaredConstructor.getParameterCount();//获得参数的个数
                    /*
                    比较参数的个数和从xml读取的参数的个数是否相等
                    这里做了简化，认为只要参数个数相等就可以使用该构造方法，实际上还需要比较参数的类型
                     */
                    if (parameterCount != bean.getPropertyNames().size()) {
                        continue;
                    }
                    //给args数组分配内存
                    args = new Object[parameterCount];

                    List<String> constructorArguments = bean.getConstructorArguments();
                    /*
                    从ConstructorArguments中获取参数的id。
                    这里是做了简化的，
                    String string = next.attributeValue("ref");
                    通过这个代码得到的是同样在xml中声明的对象
                    类似这种：<constructor-arg ref="userServiceImpl"/>
                     */
                    for (int i = 0; i < constructorArguments.size(); i++) {
                        args[i] = getBean(constructorArguments.get(i));
                    }
                    //将符合的构造函数赋给use
                    use = declaredConstructor;
                    break;
                }
                //通过选中的构造函数和提取出的参数构造对象
                object = use.newInstance(args);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            //现在只是创建好了对象，还没有注入参数，下面是注入参数
            List<String> propertyNames = bean.getPropertyNames();
            List<String> propertyValue = bean.getPropertyValue();
            // 通过反射获取当前类所有的方法信息（Method 对象）
            Method[] declaredMethods = object.getClass().getDeclaredMethods();
            for (int i = 0; i < propertyNames.size(); i++) {
                for (Method method : declaredMethods) {
                    if (method.getName().equals("set" + upperCaseFirstChar(propertyNames.get(i)))) {
                        // 获得方法参数实例
                        String string = propertyValue.get(i);
                        try {
                            // 通过反射执行调用 setter() 方法。invoke：调用方法，propertyBean 作为方法的参数
                            method.invoke(object,string);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }
                }
            }
            return object;
        }
    }

    private String upperCaseFirstChar(String str) {
        char chars[] = str.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }
}
