package dev.yua.lynxiberian.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;
import java.util.jar.JarFile;

@Component
public class BeanInjector {

    @Autowired
    ConfigurableApplicationContext applicationContext;
    public void loadBeans(File folder) throws IOException {
        for(File file : folder.listFiles()) {
            JarFile jarFile = new JarFile(file);
            InputStream inputStream = jarFile.getInputStream(jarFile.getJarEntry(".properties"));
            Properties properties = new Properties();
            properties.load(inputStream);

            String beans = properties.getProperty("beans");

            if(beans == null) continue;

            String[] classes = beans.split(":");
            URL[] urls = { new URL("jar:file:" + file.getAbsolutePath()+"!/") };
            URLClassLoader cl = URLClassLoader.newInstance(urls);

            for(String clazzName : classes){
                try {
                    Class<?> clazz = cl.loadClass(clazzName);
                    this.applicationContext.getAutowireCapableBeanFactory().createBean(clazz);
                }catch(Exception e){
                    e.printStackTrace(System.err);
                }
                jarFile.close();
            }
        }
    }
}
