package hw24di.appcontainer;

import hw24di.appcontainer.api.AppComponent;
import hw24di.appcontainer.api.AppComponentsContainerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ContainerService {
        private static final Logger logger = LoggerFactory.getLogger(ContainerService.class);
        private final Map<String, Method> appComponentMethodsByName = new HashMap<>();
        private final Map<String, List<Class<?>>> appComponentArgTypesByName = new HashMap<>();
        private final Map<String, Class<?>> appComponentsToConfigClassesByNames = new HashMap<>();
        private final Map<String, Class<?>> appCanonicalComponentNameToClass = new HashMap<>();

    public ContainerService() {}

    void addComponent(Class<?> configClass)
            throws  SecurityException, IllegalArgumentException
    {
        Method[] methodsAll = configClass.getDeclaredMethods();

        Arrays.stream(methodsAll).
                filter(method -> method.getDeclaredAnnotation(AppComponent.class)!=null).
                forEachOrdered(method -> {
                    String name = method.getDeclaredAnnotation(AppComponent.class).name();
                    appComponentMethodsByName.put(name, method);
                    appComponentsToConfigClassesByNames.put(name, configClass);
                    appComponentArgTypesByName.put(name, Arrays.asList(method.getParameterTypes()));
                    try{ appCanonicalComponentNameToClass.put(method.getReturnType().getCanonicalName(),
                            Class.forName(method.getReturnType().getCanonicalName()));}
                    catch(ClassNotFoundException cnf){
                        logger.error(cnf.getMessage(), cnf);}
                });
    }

    boolean checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            return false;
        }
        return true;
    }


    void checkIntegrity(){
        for (String name : appComponentArgTypesByName.keySet()) {
            List<Class<?>> listArgs = appComponentArgTypesByName.get(name);
            if (listArgs.size() > 0) {
                for (var clazz : listArgs) {
                    List<Class<?>> cl = appComponentMethodsByName.keySet().stream()
                            .map(nameComponent -> appComponentMethodsByName.get(nameComponent).getReturnType())
                            .filter(cl1 -> cl1.isAssignableFrom(clazz)).collect(Collectors.toList());
                    if (cl.size() == 0) {
                        throw new IllegalArgumentException(String.format("scheme is not not finished for %s", name));
                    } else if (cl.size() > 1) {
                        throw new IllegalArgumentException(String.format("too many candidates for %s args", name));
                    }
                }
            }
        }
    }

    Class<?> getClassByCanonicalComponentName(String canonicalName){
        return appCanonicalComponentNameToClass.get(canonicalName);
    }

    String getAppComponentNameByClass (Class<?> componentClass)
   {
        List<String> names = appCanonicalComponentNameToClass.keySet().stream()
                .filter(canonName ->
                        appCanonicalComponentNameToClass.get(canonName).isAssignableFrom(componentClass)                    )
                .map( canonName -> getNameByCanonName(canonName))
                .collect(Collectors.toList());
        if ( names.size() == 0) {
            throw new RuntimeException("There is not a component for " + componentClass.getName());
        }
        else if ( names.size() > 1){
            throw new RuntimeException("There are too many components for " + componentClass.getName());
        }
        return names.get(0);
    }

    Set <String> getCanonicalNamesList () {
        return appCanonicalComponentNameToClass.keySet();
    }


    Method getComponentMethodByName (String componentName) {
        return appComponentMethodsByName.get (componentName);
    }

    Class<?> getConfigClassForComponentName(String componentName) {
        return appComponentsToConfigClassesByNames.get(componentName);
    }


    private String getNameByCanonName (String canonicalName){
        for(String key : appComponentMethodsByName.keySet()){
            if (appComponentMethodsByName.get(key).getReturnType().
                            getCanonicalName().equals(canonicalName))
            {
                return key;
            }
        }
        throw new RuntimeException("имя по кононическому имени не найдено: "+ canonicalName);
    }

    String getNameByType (Class <?> clazz){
        for(String key : appComponentMethodsByName.keySet()){
            if (appComponentMethodsByName.get(key).getReturnType()==clazz){
                return key;
            }
        }
        throw new RuntimeException("класс не найден:" + clazz);
    }

}
