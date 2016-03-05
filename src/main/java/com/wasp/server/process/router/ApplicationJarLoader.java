package com.wasp.server.process.router;

import com.wasp.AppUtils;
import com.wasp.configuration.wasp.Wasp;
//import com.wasp.schemas.wasp.WaspType;
import org.apache.log4j.Logger;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ApplicationJarLoader {
    private static Logger logger=Logger.getLogger(ApplicationJarLoader.class);

    private Wasp applicationConfiguration;

    private JarClassLoader jcl;

    public ApplicationJarLoader(String jarLocation) {
        logger.info("loading jar "+jarLocation);
        this.jcl = new JarClassLoader();
        try {
            this.jcl.add(new FileInputStream(jarLocation));
            //URL url = new URL("jar:file:" + jarLocation + "!/wasp.xml");
            //InputStream stream = url.openStream();
            //this.applicationConfiguration = new AppUtils().loadXML(stream, WaspType.class);
            URL url=new URL("jar:file:" + jarLocation + "!/wasp.json");
            //System.out.println("_____________________________");
            this.applicationConfiguration=new AppUtils().fromJSON(url, Wasp.class);
            //System.out.println("_____________________________");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Wasp getApplicationConfiguration() {
        return applicationConfiguration;
    }

    public JarClassLoader getJcl() {
        return jcl;
    }

    public Class<?> loadClass(String className){
        return newInstance(className).getClass();
    }

    public Object newInstance(String className){
        return JclObjectFactory.getInstance().create(getJcl(),className);
    }
}
