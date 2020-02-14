package com.smoothstack.jan2020.LmsJDBC.DataAccess;

import com.smoothstack.jan2020.LmsJDBC.Debug;
import com.smoothstack.jan2020.LmsJDBC.entity.Entity;
import com.smoothstack.jan2020.LmsJDBC.entity.EntityInfo;

import java.sql.Connection;
import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Repository<R extends Entity, ID> extends DataAccess<R> {

    public Repository(Connection connection, Class<R> cls) {
        super(connection, cls);
    }

    public List<R> findAll() {
        try {
            String table = EntityInfo.tableNameOf(getEntityClass());
            String idField = EntityInfo.idFieldOf(getEntityClass()).getName();
            List<R> resultList =  executeQuery(getConnection(), "SELECT * FROM ? ",
                    this::extractValue, table, idField) ;

            if (resultList.size() == 0)
                return new ArrayList<R>();

            return resultList;

        } catch (Exception e) {
            Debug.println(e.toString());
            return new ArrayList<R>();
        }
    }

    public Optional<R> findById(ID id) {
        try {
            String table = EntityInfo.tableNameOf(getEntityClass());
            String idField = EntityInfo.idFieldOf(getEntityClass()).getName();
            List<R> resultList =  executeQuery(getConnection(), "SELECT * FROM ? WHERE ? = ? ",
                    this::extractValue, table, idField, id) ;

            if (resultList.size() == 0)
                return Optional.empty();

            if (resultList.size() > 1)
                throw new SQLDataException("expected single row, got "+resultList.size());

            return Optional.of(resultList.get(0));

        } catch (Exception e) {
            Debug.println(e.toString());
            return Optional.empty();
        }
    }
}
