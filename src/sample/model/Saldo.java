package sample.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Saldo {
    SimpleIntegerProperty income;
    SimpleIntegerProperty expense;
    SimpleIntegerProperty profit;
    SimpleDoubleProperty profittaxed;

    public Saldo(Integer income, Integer expense, Integer profit, Double profittaxed) {
        this.income = new SimpleIntegerProperty(income);
        this.expense = new SimpleIntegerProperty(expense);
        this.profit = new SimpleIntegerProperty(profit);
        this.profittaxed = new SimpleDoubleProperty(profittaxed);
    }

    public int getIncome() {
        return income.get();
    }

    public SimpleIntegerProperty incomeProperty() {
        return income;
    }

    public void setIncome(int income) {
        this.income.set(income);
    }

    public int getExpense() {
        return expense.get();
    }

    public SimpleIntegerProperty expenseProperty() {
        return expense;
    }

    public void setExpense(int expense) {
        this.expense.set(expense);
    }

    public int getProfit() {
        return profit.get();
    }

    public SimpleIntegerProperty profitProperty() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit.set(profit);
    }

    public double getProfittaxed() {
        return profittaxed.get();
    }

    public SimpleDoubleProperty profittaxedProperty() {
        return profittaxed;
    }

    public void setProfittaxed(int profittaxed) {
        this.profittaxed.set(profittaxed);
    }
}
