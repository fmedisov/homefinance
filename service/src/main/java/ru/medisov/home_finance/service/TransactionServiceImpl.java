package ru.medisov.home_finance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.medisov.home_finance.common.model.*;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.dao.repository.TransactionRepository;
import ru.medisov.home_finance.service.exception.HomeFinanceServiceException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Service
//todo @Transactional and tests
public class TransactionServiceImpl extends CommonService implements TransactionService {

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
            validate(model);

            return Optional.of(model);
        } catch (HomeFinanceDaoException e) {
            throw new HomeFinanceServiceException(e);
        } catch (HomeFinanceServiceException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<TransactionModel> findByNameAndCurrentUser(String name) {
        try {
            Optional<TransactionModel> optional = repository.findByNameAndUserModel(name, getCurrentUser());
            TransactionModel model = optional.orElseThrow(HomeFinanceServiceException::new);
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
            TransactionModel model = optional.orElseThrow(HomeFinanceServiceException::new);
            validate(model);

            return Optional.of(model);
        } catch (HomeFinanceDaoException e) {
            throw new HomeFinanceServiceException(e);
        } catch (HomeFinanceServiceException e) {
            return Optional.empty();
        }
    }

    @Override
    public Collection<TransactionModel> findAll() {
        Collection<TransactionModel> models = repository.findAll();
        models.forEach(this::validate);

        return models;
    }

    @Override
    public boolean remove(Long id) {
        TransactionModel transaction = findById(id).orElse(null);

        if (transaction != null && transaction.getTags() != null && transaction.getTags().size() > 0) {
            Set<TagModel> tags = transaction.getTags();
            tags.forEach(t -> tagService.update(t.setCount(t.getCount() - 1)));
        }

        boolean isExist = repository.existsById(id);
        repository.deleteById(id);
        return isExist;
    }

    @Override
    public TransactionModel save(TransactionModel model) {
        if (model != null && model.getTags() != null && model.getTags().size() > 0) {
            Set<TagModel> tags = model.getTags();
            tags.forEach(t -> tagService.update(t.setCount(t.getCount() + 1)));
        }

        TransactionModel newModel = new TransactionModel();
        if (validate(model)) {
            if (model != null) {
                model.setUserModel(getCurrentUser());
            }
            newModel = repository.save(model);
        }

        return newModel;
    }

    @Override
    public TransactionModel update(TransactionModel model) {
        updateTagCount(model);

        TransactionModel newModel = new TransactionModel();
        if (validate(model)) {
            model.setUserModel(getCurrentUser());
            newModel = repository.saveAndFlush(model);
        }

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
        models.forEach(this::validate);

        return models;
    }

    @Override
    public Collection<TransactionModel> findByPeriodAndCurrentUser(LocalDateTime dateFrom, LocalDateTime upToDate) {
        Collection<TransactionModel> models = repository.findByDateTimeBetweenAndUserModel(getDateFrom(dateFrom), getUpToDate(upToDate), getCurrentUser());
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

        models.forEach(this::validate);

        return models;
    }

    @Override
    public Collection<TransactionModel> findByCategoryAndCurrentUser(CategoryTransactionModel category) {
        Collection<TransactionModel> models;

        if (category != null) {
            models = repository.findByCategory(category.getId());
        } else {
            models = repository.findAllByUserModel(getCurrentUser());
        }

        models.forEach(this::validate);

        return models;
    }

    @Override
    public Collection<TransactionModel> incomeByPeriod(LocalDateTime dateFrom, LocalDateTime upToDate) {
        Collection<TransactionModel> models = repository.incomeByPeriod(getDateFrom(dateFrom), getUpToDate(upToDate));
        models.forEach(this::validate);

        return models;
    }

    @Override
    public Collection<TransactionModel> incomeByPeriodAndCurrentUser(LocalDateTime dateFrom, LocalDateTime upToDate) {
        Collection<TransactionModel> models = repository.findAllByTransactionTypeAndDateTimeBetweenAndUserModel(
                TransactionType.INCOME,
                getDateFrom(dateFrom),
                getUpToDate(upToDate),
                getCurrentUser());
        models.forEach(this::validate);

        return models;
    }

    @Override
    public Collection<TransactionModel> expenseByPeriod(LocalDateTime dateFrom, LocalDateTime upToDate) {
        Collection<TransactionModel> models = repository.expenseByPeriod(getDateFrom(dateFrom), getUpToDate(upToDate));
        models.forEach(this::validate);

        return models;
    }

    @Override
    public Collection<TransactionModel> expenseByPeriodAndCurrentUser(LocalDateTime dateFrom, LocalDateTime upToDate) {
        Collection<TransactionModel> models = repository.findAllByTransactionTypeAndDateTimeBetweenAndUserModel(
                TransactionType.EXPENSE,
                getDateFrom(dateFrom),
                getUpToDate(upToDate),
                getCurrentUser());
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
    public Map<String, IncomeExpense> sumByPeriodNoCategoriesByCurrentUser(LocalDateTime dateFrom, LocalDateTime upToDate) {
        Map<String, IncomeExpense> sumByPeriod = new HashMap<>();

        Collection<TransactionModel> incomeTransactions = incomeByPeriodAndCurrentUser(getDateFrom(dateFrom), getUpToDate(upToDate));
        Collection<TransactionModel> expenseTransactions = expenseByPeriodAndCurrentUser(getDateFrom(dateFrom), getUpToDate(upToDate));

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

        incomeExpenseByCategories(result, categories, transactionModels);

        result.putAll(withoutCategories(transactionModels));

        return !result.equals(new HashMap<>()) ? result : defaultData;
    }

    @Override
    public Map<CategoryTransactionModel, IncomeExpense> sumByPeriodByCategoriesAndCurrentUser(LocalDateTime dateFrom, LocalDateTime upToDate) {
        Map<CategoryTransactionModel, IncomeExpense> defaultData = new HashMap<>();
        defaultData.put(null, new IncomeExpense().setIncome(BigDecimal.ZERO).setExpense(BigDecimal.ZERO));

        Map<CategoryTransactionModel, IncomeExpense> result = new HashMap<>();
        List<CategoryTransactionModel> categories = new ArrayList<>(categoryService.findAllByCurrentUser());
        Collection<TransactionModel> transactionModels = findByPeriodAndCurrentUser(getDateFrom(dateFrom), getUpToDate(upToDate));

        incomeExpenseByCategories(result, categories, transactionModels);

        result.putAll(withoutCategories(transactionModels));

        return !result.equals(new HashMap<>()) ? result : defaultData;
    }

    private void incomeExpenseByCategories(Map<CategoryTransactionModel, IncomeExpense> result, List<CategoryTransactionModel> categories, Collection<TransactionModel> transactionModels) {
        for (CategoryTransactionModel category : categories) {
            Collection<TransactionModel> incomeTransactions = byCategoryAndType(transactionModels, category, TransactionType.INCOME);
            Collection<TransactionModel> expenseTransactions = byCategoryAndType(transactionModels, category, TransactionType.EXPENSE);

            BigDecimal commonIncome = getCommonAmount(incomeTransactions);
            BigDecimal commonExpense = getCommonAmount(expenseTransactions);

            IncomeExpense incomeExpense = new IncomeExpense().setIncome(commonIncome).setExpense(commonExpense);
            result.put(category, incomeExpense);
        }
    }

    @Override
    public Collection<TransactionModel> findAllByCurrentUser() {
        Collection<TransactionModel> models = repository.findAllByUserModel(getCurrentUser());
        models.forEach(this::validate);

        return models;
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

    @Override
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

    @Override
    public Collection<TransactionModel> getByPeriodAndTypeAndCurrentUser(LocalDateTime dateFrom, LocalDateTime upToDate, String transactionTypeString) {
        TransactionType parsed = TransactionType.findByName(transactionTypeString).orElse(null);
        if (TransactionType.INCOME.equals(parsed)) {
            return incomeByPeriodAndCurrentUser(dateFrom, upToDate);
        } else if (TransactionType.EXPENSE.equals(parsed)) {
            return expenseByPeriodAndCurrentUser(dateFrom, upToDate);
        } else {
            return findByPeriodAndCurrentUser(dateFrom, upToDate);
        }
    }

    private void updateTagCount(TransactionModel model) {
        TransactionModel oldModel = findById(model.getId()).orElse(null);
        //if the tag is removed from the model
        if (oldModel != null && oldModel.getTags() != null) {
            Set<TagModel> oldModelTags = oldModel.getTags();
            oldModelTags.forEach(t -> {
                if (model.getTags() != null && !model.getTags().contains(t)) {
                    tagService.update(t.setCount(t.getCount() - 1));
                }
            });
        }

        //if the tag appeared in the model
        if (model.getTags() != null) {
            Set<TagModel> modelTags = model.getTags();
            modelTags.forEach(t -> {
                if (oldModel != null && oldModel.getTags() != null && !oldModel.getTags().contains(t)) {
                    tagService.update(t.setCount(t.getCount() + 1));
                }
            });
        }
    }

    private Long getUserId() {
        Long userId;
        if (getCurrentUser() != null) {
            userId = getCurrentUser().getUserId();
        } else {
            userId = null;
        }

        return userId;
    }
}
