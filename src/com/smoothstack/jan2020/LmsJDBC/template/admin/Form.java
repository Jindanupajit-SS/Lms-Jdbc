package com.smoothstack.jan2020.LmsJDBC.template.admin;

import com.smoothstack.jan2020.LmsJDBC.ConnectionManager.ConnectionBuilder;
import com.smoothstack.jan2020.LmsJDBC.DataAccess.DAOFactory;
import com.smoothstack.jan2020.LmsJDBC.DataAccess.DataAccess;
import com.smoothstack.jan2020.LmsJDBC.DataAccess.Repository;
import com.smoothstack.jan2020.LmsJDBC.Debug;
import com.smoothstack.jan2020.LmsJDBC.entity.Entity;
import com.smoothstack.jan2020.LmsJDBC.entity.EntityInfo;
import com.smoothstack.jan2020.LmsJDBC.entity.FieldInfo;
import com.smoothstack.jan2020.LmsJDBC.persistence.Relation;
import com.smoothstack.jan2020.LmsJDBC.persistence.RelationToOne;
import com.smoothstack.jan2020.LmsJDBC.ui.KeyboardScanner;
import com.smoothstack.jan2020.LmsJDBC.ui.SelectOption;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@SuppressWarnings({"unchecked", "unused"})
public class Form <T extends Entity> {

    public static <R extends Entity> R inputEntity(R entity) {
        List<FieldInfo> fieldInfoList = EntityInfo.allFieldInfoOf(entity.getClass());

        for (FieldInfo fieldInfo : fieldInfoList) {
            Debug.printf("Input %s.%s as %s\n", fieldInfo.getEntityName(), fieldInfo.getFieldName(), fieldInfo.getField().getType().getSimpleName());
            if (fieldInfo.isId()) {
                Debug.println(" ... is @Id <skipped>");
                continue;
            }
            try {

                if (fieldInfo.getField().getType() == String.class) {
                    String defaultValue = (String) fieldInfo.getValue(entity);
                    if (defaultValue != null)
                        fieldInfo.setValue(entity, KeyboardScanner.getStringOrDefault(fieldInfo.getInputPrompt(), defaultValue));
                    else
                        fieldInfo.setValue(entity, KeyboardScanner.getString(fieldInfo.getInputPrompt()));
                }
                else if (fieldInfo.getField().getType() == Integer.class) {
                    Integer defaultValue = (Integer) fieldInfo.getValue(entity);
                    if (defaultValue != null)
                        fieldInfo.setValue(entity, KeyboardScanner.getIntegerOrDefault(fieldInfo.getInputPrompt(), defaultValue));
                    else
                        fieldInfo.setValue(entity, KeyboardScanner.getInteger(fieldInfo.getInputPrompt()));
                }
                else if (Entity.class.isAssignableFrom(fieldInfo.getField().getType())) {

                    fieldInfo.setValue(entity, selectRelation(fieldInfo.getInputPrompt(), ((RelationToOne) fieldInfo.getValue(entity))));
                }
                else {
                    Debug.println(" ... do not know how to get input <skipped>");
                }
            } catch (Exception e) {
                Debug.printException(e);
            }
        }

        return entity;

    }


    public static <R extends Entity> R selectRelation(String prompt, RelationToOne relationEntity) {

        try (Connection connection = new ConnectionBuilder().getConnection()){

            DataAccess<R> entityDAO = (DataAccess<R>) DAOFactory.getDAO(relationEntity.getEntityClass(), connection);
            List<R> entityList = entityDAO.read(null);

            SelectOption<R, R> menu = new SelectOption<>();
            menu.setPrompt(prompt);
            menu.getItems().addAll(entityList);
            menu.setValueMapper((o, c)->o);
            menu.setLabelMapper(Entity::toMenuLabel);

            return menu.get();

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchFieldException | SQLException e) {
            Debug.printException(e);
            return null;
        }
    }

    public T input(T defaultValue) {

        return (T) Form.inputEntity((Entity) defaultValue);
    }

    public T create(Class<Entity> entityClass) {
        try {
            Entity entity = (T) entityClass.newInstance();

            Repository<T, Integer> repository = new Repository<T, Integer>(new ConnectionBuilder().getConnection(), (Class<T>) entityClass);



        } catch (InstantiationException | IllegalAccessException | SQLException | ClassNotFoundException e) {
            Debug.printException(e);
            return null;
        }
        return null;
    }
}
