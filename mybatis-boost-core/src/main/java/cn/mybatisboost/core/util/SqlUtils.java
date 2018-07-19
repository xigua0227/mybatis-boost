package cn.mybatisboost.core.util;

import org.apache.ibatis.session.RowBounds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public abstract class SqlUtils {

    private static final Pattern PATTERN_PLACEHOLDER = Pattern.compile("(?<!')\\B\\?\\B(?!')");
    private static final Pattern PATTERN_COLUMN = Pattern.compile("(\\w+).*?(?<!')\\B\\?\\B(?!')");

    private static ConcurrentMap<String, List<String>> columnsCache = new ConcurrentHashMap<>();

    public static StringBuilder appendWhere(StringBuilder sqlBuilder, Stream<String> stream) {
        sqlBuilder.append(" WHERE ");
        stream.forEach(c -> sqlBuilder.append(c).append(" = ? AND "));
        sqlBuilder.setLength(sqlBuilder.length() - 5);
        return sqlBuilder;
    }

    public static String appendLimit(String sql, RowBounds rowBounds) {
        if (rowBounds.getOffset() != RowBounds.NO_ROW_OFFSET || rowBounds.getLimit() != RowBounds.NO_ROW_LIMIT) {
            sql += " LIMIT " + rowBounds.getOffset() + ", " + rowBounds.getLimit();
        }
        return sql;
    }

    public static String appendLimitOffset(String sql, RowBounds rowBounds) {
        if (rowBounds.getOffset() != RowBounds.NO_ROW_OFFSET || rowBounds.getLimit() != RowBounds.NO_ROW_LIMIT) {
            sql += " LIMIT " + rowBounds.getLimit() + " OFFSET " + rowBounds.getOffset();
        }
        return sql;
    }

    public static int countPlaceholders(String sql) {
        int count = 0;
        Matcher matcher = PATTERN_PLACEHOLDER.matcher(sql);
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    public static List<String> findColumnsFromSQL(String sql) {
        return columnsCache.computeIfAbsent(sql, k -> {
            List<String> columns = new ArrayList<>();
            Matcher matcher = PATTERN_COLUMN.matcher(sql);
            while (matcher.find()) {
                columns.add(matcher.group(1));
            }
            return Collections.unmodifiableList(columns);
        });
    }
}
