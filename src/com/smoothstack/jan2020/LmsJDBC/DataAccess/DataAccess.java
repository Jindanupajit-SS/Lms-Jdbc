package com.smoothstack.jan2020.LmsJDBC.DataAccess;

import com.smoothstack.jan2020.LmsJDBC.DataAccess.Utils.Condition;
import com.smoothstack.jan2020.LmsJDBC.DataAccess.Utils.Table;
import com.smoothstack.jan2020.LmsJDBC.DataAccess.Utils.Where;
import com.smoothstack.jan2020.LmsJDBC.Debug;
import com.smoothstack.jan2020.LmsJDBC.entity.*;
import com.smoothstack.jan2020.LmsJDBC.persistence.TableName;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;


public class DataAccess<T extends Entity>  implements Closeable {
    private Connection connection = null;
    private Class<T> cls = null;

    DataAccess() {

    }

    void init(Connection connection, Class<? extends Entity> cls) {
        this.connection = Objects.requireNonNull(connection);
        this.cls = (Class<T>) cls;
    }


    DataAccess(Connection connection, Class<T> cls) {
        this.connection = Objects.requireNonNull(connection);
        this.cls = cls;
    }



    public void insert(T object) throws SQLException, NoSuchFieldException {
        FieldInfoMap fieldInfoMap = FieldInfoMap.of(object);

        if (fieldInfoMap.size() == 0)
            return;

        ColumnMap columnMap = new ColumnMap(fieldInfoMap);
        columnMap.selectAll();

        @SuppressWarnings("unchecked")
        Set<FieldInfo> idSet = fieldInfoMap.getIdSet();
        idSet.iterator().forEachRemaining(f->columnMap.deselect(f.getColumnName()));

        String sqlStatement;
        PreparedStatement pstmt = connection.prepareStatement(
                sqlStatement = String.format("INSERT INTO %s (%s) VALUES (%s)",
                        EntityInfo.tableNameOf(object.getClass()),
                        columnMap.getColumnMapCsv(),
                        columnMap.getValueMapCsv()
                ),
                idSet.size()>0?Statement.RETURN_GENERATED_KEYS:Statement.NO_GENERATED_KEYS);
        Debug.println(sqlStatement);


        Iterator<String> selectedColumn = columnMap.getSelectedColumn().iterator();

        for(int i = 0; selectedColumn.hasNext(); ++i) {
            FieldInfo fieldInfo = fieldInfoMap.getByColumnName(selectedColumn.next());
            Object value = fieldInfo.getValue(object);
            pstmt.setObject(1 + i, value);
            Debug.printf(" ?[%d] %s (%s.%s) =  %s \n",
                    1+i, fieldInfo.getColumnName(), fieldInfo.getEntityName(), fieldInfo.getFieldName(), value);
        }

        pstmt.executeUpdate();

        ResultSet keys = pstmt.getGeneratedKeys();

        if (keys.next() && idSet.size()>0)
            idSet.iterator().next().setValue(object,keys.getInt(1) );

    }

    public void update(T object) throws SQLException, NoSuchFieldException {
        FieldInfoMap fieldInfoMap = FieldInfoMap.of(object);

        if (fieldInfoMap.size() == 0)
            return;

        ColumnMap columnMap = new ColumnMap(fieldInfoMap);
        columnMap.selectAll();

        ColumnMap idMap = new ColumnMap(fieldInfoMap);
        idMap.deselectAll();

        @SuppressWarnings("unchecked")
        Set<FieldInfo> idSet = fieldInfoMap.getIdSet();
        idSet.iterator().forEachRemaining(f-> {
            columnMap.deselect(f.getColumnName());
            idMap.select(f.getColumnName());
        });

        String sqlStatement;
        PreparedStatement pstmt = connection.prepareStatement(
                sqlStatement = String.format("UPDATE %s SET %s WHERE %s ",
                        EntityInfo.tableNameOf(object.getClass()),
                        columnMap.getColumnValueMapCsv(),
                        idMap.getSelectedColumn().stream().map(s -> String.format("%s=?", s)).collect(Collectors.joining(" AND "))
                )
        );
        Debug.println(sqlStatement);


        Iterator<String> selectedColumn = columnMap.getSelectedColumn().iterator();
        int i;
        for(i = 0; selectedColumn.hasNext(); ++i) {
            FieldInfo fieldInfo = fieldInfoMap.getByColumnName(selectedColumn.next());
            Object value = fieldInfo.getValue(object);
            pstmt.setObject(1 + i, value);
            Debug.printf(" ?[%d] %s (%s.%s) =  %s \n",
                    1+i, fieldInfo.getColumnName(), fieldInfo.getEntityName(), fieldInfo.getFieldName(), value);
        }

        Iterator<String> id = idMap.getSelectedColumn().iterator();

        for(; id.hasNext(); ++i) {
            FieldInfo fieldInfo = fieldInfoMap.getByColumnName(id.next());
            Object value = fieldInfo.getValue(object);
            pstmt.setObject(1 + i, value);
            Debug.printf(" ?[%d] %s (%s.%s) =  %s \n",
                    1+i, fieldInfo.getColumnName(),
                    fieldInfo.getEntityName(), fieldInfo.getFieldName(), value);
        }

        pstmt.executeUpdate();

    }

    public void delete(T object) throws SQLException, NoSuchFieldException {
        FieldInfoMap fieldInfoMap = FieldInfoMap.of(object);

        ColumnMap idMap = new ColumnMap(fieldInfoMap);
        idMap.deselectAll();

        @SuppressWarnings("unchecked")
        Set<FieldInfo> idSet = fieldInfoMap.getIdSet();
        idSet.iterator().forEachRemaining(f-> {
            idMap.select(f.getColumnName());
        });

        String sqlStatement;
        PreparedStatement pstmt = connection.prepareStatement(
                sqlStatement = String.format("DELETE FROM %s WHERE %s ",
                        EntityInfo.tableNameOf(object.getClass()),
                        idMap.getSelectedColumn().stream().map(s -> String.format("%s=?", s)).collect(Collectors.joining(" AND "))
                )
        );
        Debug.println(sqlStatement);

        Iterator<String> id = idMap.getSelectedColumn().iterator();

        for(int i = 0; id.hasNext(); ++i) {
            FieldInfo fieldInfo = fieldInfoMap.getByColumnName(id.next());
            Object value = fieldInfo.getValue(object);
            pstmt.setObject(1 + i, value);
            Debug.printf(" ?[%d] %s (%s.%s) =  %s \n",
                    1+i, fieldInfo.getColumnName(),
                    fieldInfo.getEntityName(), fieldInfo.getFieldName(), value);
        }

        pstmt.executeUpdate();


    }

    public <R> List<R> read(Class<R> object, Table table, Condition... filter) {

        String sqlStatement;

        List<Where> where = new ArrayList<>();
        for (Condition eachCondition : filter) {
            if (eachCondition instanceof Where) {
                where.add((Where) eachCondition);
            }
        }

        // TODO: Finish this method
        return null;
    }

    // TODO: Cleanup
    public List<T> read(Condition... filter) throws NoSuchFieldException, SQLException {

        String sqlStatement;

        List<Where> where = new ArrayList<>();
        for (Condition eachCondition : filter) {
            if (eachCondition instanceof Where) {
                where.add((Where) eachCondition);
            }
        }

        String WhereClause = where.size()>0?"WHERE "+where.stream().map(Condition::getMapper).collect(Collectors.joining(" AND ")):"";

        PreparedStatement pstmt = connection.prepareStatement(
                sqlStatement = String.format("SELECT * FROM %s %s",
                        EntityInfo.tableNameOf(cls), WhereClause));
        Debug.println(sqlStatement);

        if (where.size() > 0) {
            int i = 0;
            for (Where w : where) {
                pstmt.setObject(++i, w.getValue());
                Debug.printf(" ?[%d] = %s \n", i, w.getValue());
            }
        }

        ResultSet resultSet = pstmt.executeQuery();
        ResultSetMetaData rsmd = resultSet.getMetaData();

        List<T> objectList = new ArrayList<>();
        FieldInfoMap fieldInfoMap = FieldInfoMap.of(cls);
        while(resultSet.next()) {
            T object = null;
            try {
                object = cls.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                String columnName = rsmd.getColumnName(i);
                FieldInfo fieldInfo = fieldInfoMap.getByColumnName(columnName);
                fieldInfo.setValue(object,
                            resultSet.getObject(columnName, fieldInfo.getField().getType())
                        );
            }
            objectList.add(object);
        }

        return objectList;
    }

    public void purge() throws SQLException {
        String sqlStatement;
        PreparedStatement pstmt = connection.prepareStatement(
                sqlStatement = String.format("DELETE FROM %s",
                        EntityInfo.tableNameOf(cls)));
        Debug.println(sqlStatement);
        pstmt.executeUpdate();
            pstmt = connection.prepareStatement(
                sqlStatement = String.format("ALTER TABLE %s AUTO_INCREMENT = 1;",
                        EntityInfo.tableNameOf(cls)));
        Debug.println(sqlStatement);
        pstmt.executeUpdate();
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws IOException {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
