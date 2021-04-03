package hw21cache.jdbc.mapper;

import hw21cache.core.model.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
//import java.lang.reflect.*;

public class EntityClassMetaDataImpl <T> implements EntityClassMetaData {

    private Class <?> clazz = null;

    public EntityClassMetaDataImpl (Class <T> clazz){
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        String str = clazz.getSimpleName();
        return str;
    }

    @Override
    public Constructor getConstructor() {
        Class <?> [] fieldTypes = getAllFields().stream().map(field -> field.getType()).
               toArray(Class<?>[]::new);
        Constructor [] constructors = clazz.getConstructors();
        Constructor[] filteredConstructors = Arrays.stream(constructors).
                filter(constr->Arrays.equals(constr.getParameterTypes(), fieldTypes)).toArray(Constructor[]::new);
        if(filteredConstructors.length==0){throw new RuntimeException("no constructor matching  fields");}
        return filteredConstructors[0];
    }

    @Override
    public Field getIdField() {
        List <Field> idField = new ArrayList<>(getAllFields().stream().
                filter(field -> field.getDeclaredAnnotation(Id.class)!=null).
                collect(Collectors.toList()));
        if (idField.size()==1){return idField.get(0);}
        else if (idField.size()>1){throw new RuntimeException("too many Id fields in "+getName());}
        else if (idField.size()==0){throw new RuntimeException("no Id fields in "+getName());}
        return null;
    }

    @Override
    public List<Field> getAllFields() {
        List <Field> fields = Arrays.asList(clazz.getDeclaredFields());
        return fields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        List <Field> noIdFields = new ArrayList<>(getAllFields().stream().
                filter(field -> field.getDeclaredAnnotation(Id.class)==null).
                collect(Collectors.toList()));
        return noIdFields;
    }
}
