package com.yjl.getBeanInformation;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * @Author: DLMU 袁佳林
 * @Date: 2024/3/14 10:31
 * @Description:
 **/

public class Main {


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
                System.out.println(id+" "+className);
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

    public static void main(String[] args) {
        Main main=new Main();
        main.loadFromXML("getPOM.xml");
    }
}
