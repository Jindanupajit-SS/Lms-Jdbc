package com.smoothstack.jan2020.LmsJDBC.DataAccess.Utils;

public class JoinTable extends Table {

    private JoinType type = JoinType.INNER;
    private Table left;
    private Table right;
    private String on;

    protected JoinTable(Table left, JoinType type, Table right) {
        this.type = type;
        this.left = left;
        this.right =  right;
    }

    public static JoinTable of(Table left, JoinType type, Table right) {
        return new JoinTable(left, type, right);
    }

    public static JoinTable inner(Table left, Table right) {
        return new JoinTable(left, JoinType.INNER, right);
    }

    public static JoinTable left(Table left, Table right) {
        return new JoinTable(left, JoinType.LEFT, right);
    }

    public static JoinTable right(Table left, Table right) {
        return new JoinTable(left, JoinType.RIGHT, right);
    }

    public String getOn() {
        return on;
    }

    public void setOn(String on) {
        this.on = on;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("( ") ;
        sb.append(left.toString());
        sb.append(String.format(" %s JOIN ", type));
        sb.append(right.toString());
        sb.append(" )");
        if (on != null) {
            sb.append(" ON ");
            sb.append(on);
            sb.append(" ");
        }

        return sb.toString();
    }
}
