package hw24di.appcontainer;

import hw24di.appcontainer.api.AppComponentsContainer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import org.reflections.scanners.SubTypesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.reflections.Reflections;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private static final Logger logger = LoggerFactory.getLogger(AppComponentsContainerImpl.class);

    private final ContainerService containerService = new ContainerService();


    public AppComponentsContainerImpl(String packageString) {
        Class<?>[] initialConfigClasses = getConfigClassesFromPackage(packageString);
        processConfig(initialConfigClasses);
    }

    public AppComponentsContainerImpl(Class<?>...initialConfigClasses) {
        processConfig(initialConfigClasses);
    }

    private void processConfig(Class<?>[] configClasses)
            throws SecurityException, IllegalArgumentException
    {
        for (Class<?> configClass : configClasses) {
            if (containerService.checkConfigClass(configClass)) {
                containerService.addComponent(configClass);
            }
        }
        containerService.checkIntegrity();
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        return getAppComponent(containerService.getAppComponentNameByClass (componentClass));
    }

    @Override
    public <C> C getAppComponent(String componentName) throws
            InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        Method method = containerService.getComponentMethodByName(componentName);
        if (method == null) { throw new RuntimeException("no such a component by name");}

        var instanceForComponent = containerService.getConfigClassForComponentName(componentName)
                .getConstructor().newInstance();

        Object[] args = Arrays.stream(method.getParameterTypes())
                .map(type -> containerService.getNameByType(type))
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
  //          здесь можно посмотреть вызываемые аргументы
 //           System.out.println("Arguments for "+ method.getName() + " invocation");
 //           Arrays.stream(args).forEachOrdered(arg -> System.out.println(arg.getClass().getCanonicalName()));
            return (C) callMethod(instanceForComponent, method.getName(), args);
        }
        catch (RuntimeException e) {
            System.out.println("AppComponentMethod " + method.getName() + " throws exception"+e);
        }
        return null;
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
                .map(clazz ->(containerService.getCanonicalNamesList().stream()
                        .filter(canonName -> containerService.getClassByCanonicalComponentName(canonName).isAssignableFrom(clazz))
                        .findFirst()).orElse(null))
                .map(canonicalName -> containerService.getClassByCanonicalComponentName(canonicalName))
                .toArray(Class<?>[]::new);
    }

    private Class<?> [] getConfigClassesFromPackage(String packageString)
    {
        return new Reflections(packageString, new SubTypesScanner(false))
                .getAllTypes()
                .stream()
                .map(name -> {
                    try {
                        return Class.forName(name);
                    } catch (ClassNotFoundException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toArray(Class<?>[]:: new);
    }

}
