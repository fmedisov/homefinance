package ru.medisov.home_finance.dao.model;

public class CategoryTransactionModel extends IdModel {
    private String name;
    private CategoryTransactionModel parent;

    public CategoryTransactionModel() {}

    public CategoryTransactionModel(String name, CategoryTransactionModel parent) {
        this.name = name;
        this.parent = parent;
    }

    public CategoryTransactionModel(long id, String name, CategoryTransactionModel parent) {
        this(name, parent);
        super.setId(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryTransactionModel getParent() {
        return parent;
    }

    public void setParent(CategoryTransactionModel parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "CategoryTransactionModel{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", parent=" + parent +
                '}';
    }
}
