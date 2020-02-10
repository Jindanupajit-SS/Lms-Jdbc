package com.smoothstack.jan2020.LmsJDBC.DataAccess;

import com.smoothstack.jan2020.LmsJDBC.ConnectionManager.ConnectionBuilder;
import com.smoothstack.jan2020.LmsJDBC.entity.Entity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class DAOFactory {
    private static Map<Class<? extends Entity>, Class<? extends DataAccess<? extends Entity>>> dataAccessMap = new HashMap<>();

    public static void register(Class<? extends Entity> entityClass, Class<? extends DataAccess<? extends Entity>> dataAccessClass) throws IllegalAccessException, InstantiationException {
        dataAccessMap.put(entityClass, dataAccessClass);
    }
    public static DataAccess<? extends Entity> getDAO(Class<? extends Entity> entityClass) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        return getDAO(entityClass, new ConnectionBuilder().getConnection());
    }

   public static DataAccess<? extends Entity> getDAO(Class<? extends Entity> entityClass, Connection connection) throws IllegalAccessException, InstantiationException {
        Class<? extends DataAccess>  daClass = dataAccessMap.get(entityClass);

        if (daClass == null)
            return new DataAccess<>(connection, entityClass);
        else {
            @SuppressWarnings("unchecked")
            DataAccess<? extends Entity> da =  daClass.newInstance();
            da.init(connection, entityClass);
            return da;
        }
    }

    public static DataAccess<? extends Entity> get(Class<? extends Entity> entityClass) {
        try {
            return Objects.requireNonNull(getDAO(entityClass));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new DataAccess();

    }
}
