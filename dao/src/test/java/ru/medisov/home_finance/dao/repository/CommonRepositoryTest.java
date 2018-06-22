package ru.medisov.home_finance.dao.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.medisov.home_finance.dao.DaoConfig;

import java.sql.Connection;
import java.sql.SQLException;

public class CommonRepositoryTest implements RepositoryTest {

    @BeforeAll
    static void initConfig()  {
        DaoConfig.initConfig();
    }

    @BeforeEach
    public void truncateAllTables() {
        String sqlQuery =
                "SET FOREIGN_KEY_CHECKS=0; " +
                "TRUNCATE TABLE `currency_tbl`; " +
                "TRUNCATE TABLE `account_tbl`; " +
                "TRUNCATE TABLE `category_tbl`; " +
                "TRUNCATE TABLE `transaction_tbl`; " +
                "TRUNCATE TABLE `tag_tbl`; " +
                "TRUNCATE TABLE `tag_relation_tbl`; " +
                "SET FOREIGN_KEY_CHECKS=1;";
        try (Connection connection = new DbConnectionBuilder().getConnection()) {
            connection.prepareStatement(sqlQuery).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
