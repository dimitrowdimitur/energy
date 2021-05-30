package sample.model;

import javafx.beans.property.SimpleStringProperty;

public class BillUnder {
    SimpleStringProperty clientName;

    public BillUnder(String clientName) {
        this.clientName = new SimpleStringProperty(clientName);
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
