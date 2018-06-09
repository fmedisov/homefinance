package ru.medisov.home_finance.service;

import ru.medisov.home_finance.dao.DaoConfig;

public class ServiceInit {
    public void init() {
        DaoConfig.initConfig();
    }
}
