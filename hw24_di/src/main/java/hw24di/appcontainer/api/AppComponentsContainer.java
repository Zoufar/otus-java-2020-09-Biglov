package hw24di.appcontainer.api;

import java.lang.reflect.InvocationTargetException;

public interface AppComponentsContainer {
    <C> C getAppComponent(Class<C> componentClass)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException;
    <C> C getAppComponent(String componentName)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException;
}
