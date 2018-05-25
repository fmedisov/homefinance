package ru.medisov.home_finance.dao.repository;

import java.sql.Connection;

public interface ConnectionBuilder
{
    Connection getConnection();
}