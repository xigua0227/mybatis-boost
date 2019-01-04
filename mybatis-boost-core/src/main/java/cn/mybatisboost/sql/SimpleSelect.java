package cn.mybatisboost.sql;

import cn.mybatisboost.core.adaptor.NoopNameAdaptor;
import cn.mybatisboost.util.EntityUtils;
import cn.mybatisboost.util.LambdaUtils;

import java.util.Arrays;
import java.util.List;

public class SimpleSelect implements Select {

    private List<LambdaUtils.LambdaInfo> columns;
    private List<Class<?>> tables;

    public SimpleSelect(List<LambdaUtils.LambdaInfo> columns) {
        this.columns = columns;
    }

    @Override
    public Select from(Class<?>... tables) {
        this.tables = Arrays.asList(tables);
        return this;
    }

    @Override
    public Select where() {
        return null;
    }

    @Override
    public Select groupBy() {
        return null;
    }

    @Override
    public Select having() {
        return null;
    }

    @Override
    public Select orderBy() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT ");
        if (columns.isEmpty()) {
            builder.append("* ");
        } else {
            for (LambdaUtils.LambdaInfo column : columns) {
                builder.append(String.format("'%s'.'%s', ",
                        EntityUtils.getTableName(column.getType(), new NoopNameAdaptor()),
                        column.getMethodName().replace("get", "")));
            }
            builder.setLength(builder.length() - 2);
        }
        builder.append(" FROM ");
        for (Class<?> table : tables) {
            builder.append(EntityUtils.getTableName(table, new NoopNameAdaptor())).append(", ");
        }
        builder.setLength(builder.length() - 2);
        return builder.toString();
    }
}
