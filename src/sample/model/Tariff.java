package sample.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Tariff {
    SimpleIntegerProperty id;
    SimpleStringProperty priceType;
    SimpleIntegerProperty price;

    public Tariff(String priceType, Integer price, Integer id) {
        this.id = new SimpleIntegerProperty(id);
        this.price = new SimpleIntegerProperty(price);
        this.priceType = new SimpleStringProperty(priceType);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getPriceType() {
        return priceType.get();
    }

    public SimpleStringProperty priceTypeProperty() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType.set(priceType);
    }

    public int getPrice() {
        return price.get();
    }

    public SimpleIntegerProperty priceProperty() {
        return price;
    }

    public void setPrice(int price) {
        this.price.set(price);
    }
}
