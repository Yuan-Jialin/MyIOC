package com.yjl.Version1.IOC;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: DLMU 袁佳林
 * @Date: 2024/3/14 12:55
 * @Description:
 **/

public class IocContainer {

    private final Map<String, Bean> beansMap = new ConcurrentHashMap<>();

    //从xml里加载配置的bean
    private void loadFromXML(String config) {
        InputStream resourceAsStream=null;
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
                //将beans的信息存入beansMap
                beansMap.put(id,new Bean(id,className));
            }
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }finally {
            if(resourceAsStream!=null){
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /*
    构建IocContainer时需要传入XmlName（需要读取的xml文件的文件名）
     */
    public IocContainer(String XmlName){
        //根据文件名，将里面bean的信息导入到ConcurrentHashMap
        loadFromXML(XmlName);
    }
    //根据BeanID，从ConcurrentHashMap从获取bean的信息，实力化为对象进行返还
    public Object getBean(String beanID){
        //根据beanID，拿到bean对象，对象里存了bean的信息
        Bean bean = this.beansMap.get(beanID);
        //拿到beanID，对应的完整的类名
        String beanClassName = bean.getBeanClassName();
        //使用上下文加载器是防止因为双亲委派机制而无法加载类
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            //拿到类对象
            Class<?> aClass = contextClassLoader.loadClass(beanClassName);
            return aClass.getConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
