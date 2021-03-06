package com.wasp.server.process.router;

import com.wasp.AppUtils;
import com.wasp.configuration.wasp.ViewMapping;
import com.wasp.configuration.wasp.Wasp;
import com.wasp.configuration.wasp.WaspConfig;
import com.wasp.server.process.router.exceptions.MappingException;
import com.wasp.util.views.IView;
import org.apache.log4j.Logger;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

public class ApplicationJarLoader {
    private static Logger logger = Logger.getLogger(ApplicationJarLoader.class);

    private Wasp applicationConfiguration;

    private JarClassLoader jcl;

    public ApplicationJarLoader(String jarLocation) {
        logger.info("loading jar " + jarLocation);
        this.jcl = new JarClassLoader();
        URL url=null;

        try {
            this.jcl.add(new FileInputStream(jarLocation));
            url = new URL("jar:file:" + jarLocation + "!/WASP-INF/wasp.json");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        try {
            this.applicationConfiguration = new AppUtils().fromJSON(url, Wasp.class);
        } catch (IOException e) {
            Optional<Object> first = jcl.getLoadedResources()
                    .keySet()
                    .stream()
                    .filter(r -> r.contains(".class"))
                    .map(r -> newInstance(r.replaceFirst("\\.class$", "").replaceAll("/", ".")))
                    .filter(o -> o != null && o instanceof WaspConfig)
                    .findFirst();
            if(first.isPresent()){
                WaspConfig waspConfig = (WaspConfig) first.get();
                waspConfig.init();
                this.applicationConfiguration=waspConfig;
            }
        }
    }

    public Wasp getApplicationConfiguration() {
        return applicationConfiguration;
    }

    public JarClassLoader getJcl() {
        return jcl;
    }

    public Class<?> loadClass(String className) {
        return newInstance(className).getClass();
    }

    public Object newInstance(String className) {
        return JclObjectFactory.getInstance().create(getJcl(), className);
    }

    public void initContentView(IView view) throws MappingException {
        ViewMapping viewMapping = applicationConfiguration.getViewMapping();
        String name = view.getName();
        if (viewMapping != null)
            name = viewMapping.getPrefix() + name + viewMapping.getSuffix();
        final String finalName = "/WASP-INF/" + name;
        view.setContent(new String(getResourceContent(finalName)));

        List<IView> collect = view.getTemplates().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        for(IView v:collect){
            initContentView(v);
        }

    }

    public byte[] getResourceContent(String path) throws MappingException {
        if (path.matches("^/WASP-INF/.+")) {
            path = path.replaceFirst("^/", "");
            final String finalPath = path;
            try {
                byte[] bytes = jcl.getLoadedResources()
                        .entrySet()
                        .stream()
                        .filter(e -> e.getKey().equals(finalPath))
                        .findFirst()
                        .get()
                        .getValue();
                return bytes;
            } catch (NoSuchElementException e) {
                throw new MappingException("resource " + finalPath + " does not existe");
            }
        } else {
            return null;
        }
    }
}
