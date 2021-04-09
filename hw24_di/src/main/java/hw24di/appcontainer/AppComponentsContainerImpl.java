package hw24di.appcontainer;

import hw24di.appcontainer.api.AppComponent;
import hw24di.appcontainer.api.AppComponentsContainer;
import hw24di.appcontainer.api.AppComponentsContainerConfig;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private static final Logger logger = LoggerFactory.getLogger(AppComponentsContainerImpl.class);
    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    private final List<Method> appCompntMethods = new ArrayList<>();
    private final Map<String, String> appCanonNameByName = new HashMap<>();
    private final Map<String, Method> appCompntMethodsByName = new HashMap<>();
//    private final Map<String, List<Object>> appComponentsByNameArgs = new HashMap<>();
    private final Map<String, Class<?>> appCompntsToConfigClassesByNames = new HashMap<>();

    public AppComponentsContainerImpl(String packageString)
            throws IOException {
        Class<?>[] initialConfigClasses = getConfigClassesFromPackage(packageString);
        processConfig(initialConfigClasses);
    }

    public AppComponentsContainerImpl(Class<?>...initialConfigClasses) {
        processConfig(initialConfigClasses);
    }

    public AppComponentsContainerImpl(Class<?> initialConfigClass)
            throws NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException
    {
           processConfig(initialConfigClass);
      }

    private void processConfig(Class<?>[] configClasses)
            throws SecurityException, IllegalArgumentException
    {
        for (Class<?> configClass : configClasses){
            checkConfigClass(configClass);
            processConfig(configClass);
        }
    }

    private void processConfig(Class<?> configClass)
            throws  SecurityException, IllegalArgumentException
    {
        checkConfigClass(configClass);

        Method[] methodsAll = configClass.getDeclaredMethods();

        Arrays.stream(methodsAll).
                filter(method -> method.getDeclaredAnnotation(AppComponent.class)!=null).
                forEachOrdered(method -> {
                    String name = method.getDeclaredAnnotation(AppComponent.class).name();
                    appComponents.add(method.getReturnType());
                    appComponentsByName.put(name, method.getReturnType());
                    appCompntMethods.add(method);
                    appCompntMethodsByName.put(name, method);
                    appCanonNameByName.put(name, method.getReturnType().getCanonicalName());
                    appCompntsToConfigClassesByNames.put(name, configClass);
                });
    }

    private boolean checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
        return true;
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
     {
        List<String> names = appCanonNameByName.keySet().stream()
                .map (name -> appCanonNameByName.get(name))
                .filter(canonName -> {
                    try{return Class.forName(canonName).isAssignableFrom(componentClass);}
                    catch(ClassNotFoundException cnf){
                        logger.error(cnf.getMessage(), cnf);}
                    return false;})
                .map( canonName -> getNameByCanonName(canonName))
                .collect(Collectors.toList());
        if ( names.size() == 0) {
            throw new RuntimeException("There is not a component for " + componentClass.getName());
        }
        else if ( names.size() > 1){
            throw new RuntimeException("There are too many components for " + componentClass.getName());
        }
               System.out.println("component name extracted:" + names.get(0));
        return getAppComponent(names.get(0));
    }

    @Override
    public <C> C getAppComponent(String componentName) throws
            InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        Method method = appCompntMethodsByName.get(componentName);
        if (method == null) { throw new RuntimeException("no such a component by name");}

        var object = appCompntsToConfigClassesByNames.get(componentName).getConstructor().newInstance();

        Object[] args = Arrays.stream(method.getParameterTypes()).map(type -> getNameByType(type))
                .map(name ->{
                    try{ Object obj1 = getAppComponent(name);
                    return obj1;}
                    catch (InstantiationException ie) {
                        logger.error(ie.getMessage(), ie);}
                    catch (IllegalAccessException ie) {
                        logger.error(ie.getMessage(), ie);}
                    catch (InvocationTargetException ie) {
                        logger.error(ie.getMessage(), ie); }
                    catch (NoSuchMethodException ie) {
                        logger.error(ie.getMessage(), ie); }
                    return null;})
                .toArray(Object[]::new);

        try {
             return (C) callMethod(object, method.getName(), args);
        }
        catch (RuntimeException e) {
            System.out.println("AppComponentMethod " + method.getName() + " throws exception"+e);
        }
        return null;
    }

    private String getNameByCanonName (String canonName){
        for(String key : appCanonNameByName.keySet()){
            if (appCanonNameByName.get(key).equals(canonName)){
                return key;
            }
        }
        throw new RuntimeException("имя по кононическому имени не найдено: "+ canonName);
         }

    private String getNameByType (Class <?> clazz){
        for(String key : appComponentsByName.keySet()){
            if (appComponentsByName.get(key)==clazz){
                return key;
            }
        }
        throw new RuntimeException("класс не найден:" + clazz);
    }


    public Object callMethod(Object object, String name, Object... args) {
        try {
            var method = object.getClass().getDeclaredMethod(name, toClasses(args));
            method.setAccessible(true);
            return method.invoke(object, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Class<?>[] toClasses(Object[] args) {
        return Arrays.stream(args).map(Object::getClass)
                .map(clazz ->(appCanonNameByName.keySet().stream()
                        .map (name -> appCanonNameByName.get(name))
                        .filter(canonName -> {
                            try{return Class.forName(canonName).isAssignableFrom(clazz);}
                            catch(ClassNotFoundException cnf){
                                logger.error(cnf.getMessage(), cnf);}
                        return false;})
                .findFirst()).orElse(null))
                .map(canonicalName -> {
                    try{return Class.forName(canonicalName);}
                    catch(ClassNotFoundException cnf){
                              logger.error(cnf.getMessage(), cnf);}
                  return false;})
                .toArray(Class<?>[]::new);
    }

    private Class<?> [] getConfigClassesFromPackage(String packageString) throws IOException {

        String libs = System.getProperty("user.dir");
        String path = libs.substring(0, libs.lastIndexOf("build"))
                +"src\\main\\java\\"
                + packageString.replace("." , "\\");

      Class<?>[] configClasses =
              Arrays.stream(new File(path).listFiles())
                .filter(File::isFile).
                      map(file -> packageString+"."+file.getName()
                              .substring(0,file.getName().lastIndexOf(".")))
                        .map(name -> {
                            try {System.out.println(name);
                                Class<?> cl = Class.forName(name);
                                return cl;
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                                return null;
                            }
                        })
                .filter (cl -> checkConfigClass(cl)).toArray(Class<?>[]:: new);
      return configClasses;
    }

}
