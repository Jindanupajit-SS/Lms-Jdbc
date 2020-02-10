package com.smoothstack.jan2020.LmsJDBC.entity;

import java.util.*;
import java.util.stream.Collectors;

public class ColumnMap {

    // Order matter, use LinkedHashSet
    private final Set<String> selectedColumn = new LinkedHashSet<>();
    private final FieldInfoMap fieldInfoMap;

    public ColumnMap(FieldInfoMap fieldInfoMap) {
        this.fieldInfoMap = fieldInfoMap;
    }

    public void selectAll() {
        selectedColumn.addAll(fieldInfoMap.columnNameSet());
    }

    public void deselectAll() {
        selectedColumn.clear();
    }

    public boolean select(String name) {
        if (fieldInfoMap.columnNameSet().contains(name)) {
            selectedColumn.add(name);
            return true;
        }

        return false;
    }

    public boolean deselect(String name) {
        return selectedColumn.remove(name);
    }

    public String getColumnMapCsv() {
        return String.join(",", selectedColumn);
    }

    public String getValueMapCsv() {
        return new String(new byte[selectedColumn.size()]).replace("\0",",?").substring(1);
    }

    public String getColumnValueMapCsv() {
        return selectedColumn.stream().map(k->String.format("%s=?",k)).collect(Collectors.joining(","));
    }

    public Set<String> getSelectedColumn() {
        return selectedColumn;
    }


}
