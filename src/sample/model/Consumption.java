package sample.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Consumption {
    SimpleIntegerProperty id;
    SimpleIntegerProperty clientId;
    SimpleIntegerProperty consumption;
    SimpleStringProperty clientName;

    public Consumption(Integer clientId, Integer consumption, Integer id, String clientName) {
        this.id = new SimpleIntegerProperty(id);
        this.clientId = new SimpleIntegerProperty(clientId);
        this.consumption = new SimpleIntegerProperty(consumption);
        this.clientName = new SimpleStringProperty(clientName);
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

    public int getClientId() {
        return clientId.get();
    }

    public SimpleIntegerProperty clientIdProperty() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId.set(clientId);
    }

    public int getConsumption() {
        return consumption.get();
    }

    public SimpleIntegerProperty consumptionProperty() {
        return consumption;
    }

    public void setConsumption(int consumption) {
        this.consumption.set(consumption);
    }

    public String getClientName() {
        return clientName.get();
    }

    public SimpleStringProperty clientNameProperty() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName.set(clientName);
    }
}
