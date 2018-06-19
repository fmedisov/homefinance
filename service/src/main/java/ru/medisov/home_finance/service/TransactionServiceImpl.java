package ru.medisov.home_finance.service;

import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.common.model.CategoryTransactionModel;
import ru.medisov.home_finance.common.model.TransactionModel;
import ru.medisov.home_finance.dao.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class TransactionServiceImpl extends AbstractService implements TransactionService {
    private TransactionRepository repository = new TransactionRepository();

    @Override
    public Optional<TransactionModel> findByName(String name) {
        try {
            Optional<TransactionModel> optional = repository.findByName(name);
            TransactionModel model = optional.orElseThrow(HomeFinanceDaoException::new);
            validate(model);

            return Optional.of(model);
        } catch (HomeFinanceDaoException e) {
            throw new HomeFinanceServiceException(e);
        } catch (HomeFinanceServiceException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Collection<TransactionModel> findByPeriod(LocalDateTime dateFrom, LocalDateTime upToDate) {
        return repository.findByPeriod(dateFrom, upToDate);
    }

    @Override
    public Collection<TransactionModel> findAll() {
        Collection<TransactionModel> models = repository.findAll();
        models.forEach(this::validate);

        return models;
    }

    @Override
    public boolean remove(Long id) {
        return repository.remove(id);
    }

    @Override
    public TransactionModel save(TransactionModel model) {
        TransactionModel newModel = new TransactionModel();
        if (validate(model)) {
            newModel = repository.save(model);
        }
        return newModel;
    }

    @Override
    public TransactionModel update(TransactionModel model) {
        TransactionModel newModel = new TransactionModel();
        if (validate(model)) {
            newModel = repository.update(model);
        }
        return newModel;
    }

    @Override
    public Collection<TransactionModel> findByCategory(CategoryTransactionModel category) {
        return repository.findByCategory(category.getId());
    }

    @Override
    public Map<String, IncomeExpense> sumByPeriod(LocalDateTime dateFrom, LocalDateTime upToDate, Class<CategoryTransactionModel> oClass) {
        Map<String, IncomeExpense> sumByPeriod = new HashMap<>();
        if (oClass == null) {
            BigDecimal income = repository.incomeByPeriod(dateFrom, upToDate);
            BigDecimal expense = repository.expenseByPeriod(dateFrom, upToDate);
            IncomeExpense incomeExpense = new IncomeExpense().setIncome(income).setExpense(expense);
            sumByPeriod.put("Без категории", incomeExpense);

            return sumByPeriod;
        } else {
            Map<String, BigDecimal> incomes = repository.incomeByCategory(dateFrom, upToDate);
            Map<String, BigDecimal> expenses = repository.expenseByCategory(dateFrom, upToDate);

            List<CategoryTransactionModel> categories = new ArrayList<>(new CategoryServiceImpl().findAll());

            for (int i = 0; i < categories.size(); i++) {
                String name = categories.get(i).getName();
                IncomeExpense incomeExpense = new IncomeExpense().setIncome(incomes.getOrDefault(name, BigDecimal.ZERO))
                        .setExpense(expenses.getOrDefault(name, BigDecimal.ZERO));
                sumByPeriod.put(name, incomeExpense);
            }
        }

        return sumByPeriod;
    }

    public boolean removeByAccount(Long accountId) {
        return repository.removeByAccount(accountId);
    }
}
