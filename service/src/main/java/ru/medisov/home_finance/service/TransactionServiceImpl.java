package ru.medisov.home_finance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.medisov.home_finance.common.model.*;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.dao.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Service
public class TransactionServiceImpl extends AbstractService implements TransactionService {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Override
    public Optional<TransactionModel> findByName(String name) {
        try {
            Optional<TransactionModel> optional = repository.findByName(name);
            TransactionModel model = optional.orElseThrow(HomeFinanceServiceException::new);
            List<TagModel> tagModels = tagService.findByTransaction(model.getId());
            model.setTags(tagModels);
            validate(model);

            return Optional.of(model);
        } catch (HomeFinanceDaoException e) {
            throw new HomeFinanceServiceException(e);
        } catch (HomeFinanceServiceException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<TransactionModel> findById(Long aLong) {
        try {
            Optional<TransactionModel> optional = repository.findById(aLong);
            TransactionModel model = optional.orElseThrow(HomeFinanceDaoException::new);
            List<TagModel> tagModels = tagService.findByTransaction(model.getId());
            model.setTags(tagModels);
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
    public Collection<TransactionModel> findAll() {
        Collection<TransactionModel> models = repository.findAll();
        models.forEach(m -> m.setTags(tagService.findByTransaction(m.getId())));
        models.forEach(this::validate);

        return models;
    }

    @Override
    public boolean remove(Long id) {
        tagService.removeByTransaction(id);
        repository.deleteById(id);
        return true;
    }

    @Override
    public TransactionModel save(TransactionModel model) {
        TransactionModel newModel = new TransactionModel();
        if (validate(model)) {
            newModel = repository.save(model);
        }

        List<TagModel> tagModels = tagService.saveUpdateByTransaction(model.getTags(), newModel.getId());
        newModel.setTags(tagModels);

        return newModel;
    }

    @Override
    public TransactionModel update(TransactionModel model) {
        TransactionModel newModel = new TransactionModel();
        if (validate(model)) {
            newModel = repository.saveAndFlush(model);
        }

        List<TagModel> tagModels = tagService.saveUpdateByTransaction(model.getTags(), newModel.getId());
        newModel.setTags(tagModels);

        return newModel;
    }

    @Override
    public TransactionModel saveUpdate(TransactionModel model) {
        TransactionModel result;

        if (model.getId() == null) {
            result = save(model);
        } else {
            result = update(model);
        }

        return result;
    }

    @Override
    public Collection<TransactionModel> findByPeriod(LocalDateTime dateFrom, LocalDateTime upToDate) {
        Collection<TransactionModel> models = repository.findByPeriod(getDateFrom(dateFrom), getUpToDate(upToDate));
        models.forEach(m -> m.setTags(tagService.findByTransaction(m.getId())));
        models.forEach(this::validate);

        return models;
    }

    @Override
    public Collection<TransactionModel> findByCategory(CategoryTransactionModel category) {
        Collection<TransactionModel> models;

        if (category != null) {
            models = repository.findByCategory(category.getId());
        } else {
            models = repository.findAll();
        }
        models.forEach(m -> m.setTags(tagService.findByTransaction(m.getId())));
        models.forEach(this::validate);

        return models;
    }

    @Override
    public Collection<TransactionModel> incomeByPeriod(LocalDateTime dateFrom, LocalDateTime upToDate) {
        Collection<TransactionModel> models = repository.incomeByPeriod(getDateFrom(dateFrom), getUpToDate(upToDate));
        models.forEach(m -> m.setTags(tagService.findByTransaction(m.getId())));
        models.forEach(this::validate);

        return models;
    }

    @Override
    public Collection<TransactionModel> expenseByPeriod(LocalDateTime dateFrom, LocalDateTime upToDate) {
        Collection<TransactionModel> models = repository.expenseByPeriod(getDateFrom(dateFrom), getUpToDate(upToDate));
        models.forEach(m -> m.setTags(tagService.findByTransaction(m.getId())));
        models.forEach(this::validate);

        return models;
    }

    @Override
    public Map<String, IncomeExpense> sumByPeriodNoCategories(LocalDateTime dateFrom, LocalDateTime upToDate) {
        Map<String, IncomeExpense> sumByPeriod = new HashMap<>();

        Collection<TransactionModel> incomeTransactions = incomeByPeriod(getDateFrom(dateFrom), getUpToDate(upToDate));
        Collection<TransactionModel> expenseTransactions = expenseByPeriod(getDateFrom(dateFrom), getUpToDate(upToDate));

        BigDecimal commonIncome = getCommonAmount(incomeTransactions);
        BigDecimal commonExpense = getCommonAmount(expenseTransactions);

        IncomeExpense incomeExpense = new IncomeExpense().setIncome(commonIncome).setExpense(commonExpense);
        sumByPeriod.put("Без учета категорий", incomeExpense);

        return sumByPeriod;
    }

    @Override
    public Map<CategoryTransactionModel, IncomeExpense> sumByPeriodByCategories(LocalDateTime dateFrom, LocalDateTime upToDate) {
        Map<CategoryTransactionModel, IncomeExpense> defaultData = new HashMap<>();
        defaultData.put(null, new IncomeExpense().setIncome(BigDecimal.ZERO).setExpense(BigDecimal.ZERO));

        Map<CategoryTransactionModel, IncomeExpense> result = new HashMap<>();
        List<CategoryTransactionModel> categories = new ArrayList<>(categoryService.findAll());
        Collection<TransactionModel> transactionModels = findByPeriod(getDateFrom(dateFrom), getUpToDate(upToDate));

        for (CategoryTransactionModel category : categories) {
            Collection<TransactionModel> incomeTransactions = byCategoryAndType(transactionModels, category, TransactionType.INCOME);
            Collection<TransactionModel> expenseTransactions = byCategoryAndType(transactionModels, category, TransactionType.EXPENSE);

            BigDecimal commonIncome = getCommonAmount(incomeTransactions);
            BigDecimal commonExpense = getCommonAmount(expenseTransactions);

            IncomeExpense incomeExpense = new IncomeExpense().setIncome(commonIncome).setExpense(commonExpense);
            result.put(category, incomeExpense);
        }

        result.putAll(withoutCategories(transactionModels));

        return !result.equals(new HashMap<>()) ? result : defaultData;
    }

    private Map<CategoryTransactionModel, IncomeExpense> withoutCategories(Collection<TransactionModel> transactions) {
        Map<CategoryTransactionModel, IncomeExpense> result = new HashMap<>();

        Collection<TransactionModel> incomeTransactions = byCategoryAndType(transactions, null, TransactionType.INCOME);
        Collection<TransactionModel> expenseTransactions = byCategoryAndType(transactions, null, TransactionType.EXPENSE);

        BigDecimal income = getCommonAmount(incomeTransactions);
        BigDecimal expense = getCommonAmount(expenseTransactions);

        result.put(null, new IncomeExpense().setIncome(income).setExpense(expense));

        return result;
    }

    private Collection<TransactionModel> byCategoryAndType(Collection<TransactionModel> transactions,
                                                            CategoryTransactionModel category, TransactionType type) {
        return transactions.stream()
                .filter(t -> filterCondition(t, category) && t.getTransactionType().equals(type))
                .collect(Collectors.toList());
    }

    private boolean filterCondition(TransactionModel t, CategoryTransactionModel category) {
        if (category == null) {
            return t.getCategory() == null;
        } else {
            return category.equals(t.getCategory());
        }
    }

    public boolean removeByAccount(Long accountId) {
        return repository.removeByAccount(accountId);
    }

    private BigDecimal getCommonAmount(Collection<TransactionModel> transactions) {
        final BigDecimal[] result = {BigDecimal.ZERO};

        transactions.forEach(t -> result[0] = result[0].add(t.getAmount()));

        return result[0];
    }

    private LocalDateTime getDateFrom(LocalDateTime dateFrom) {
        return dateFrom != null ? dateFrom : LocalDateTime.MIN;
    }

    private LocalDateTime getUpToDate(LocalDateTime upToDate) {
        return upToDate != null ? upToDate : LocalDateTime.MAX;
    }

    public Collection<TransactionModel> getByPeriodAndType(LocalDateTime dateFrom, LocalDateTime upToDate, String transactionTypeString) {
        TransactionType parsed = TransactionType.findByName(transactionTypeString).orElse(null);
        if (TransactionType.INCOME.equals(parsed)) {
            return incomeByPeriod(dateFrom, upToDate);
        } else if (TransactionType.EXPENSE.equals(parsed)) {
            return expenseByPeriod(dateFrom, upToDate);
        } else {
            return findByPeriod(dateFrom, upToDate);
        }
    }
}
