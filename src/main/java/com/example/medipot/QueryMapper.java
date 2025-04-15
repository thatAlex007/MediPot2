package com.example.medipot;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface QueryMapper<T> {
    T map(ResultSet resultSet) throws SQLException;
}