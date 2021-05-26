package sample.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Client {
    SimpleIntegerProperty id;
    SimpleStringProperty clientFullName;
    SimpleStringProperty clientType;

    public Client(String fullname, String type, Integer id) {
        this.id = new SimpleIntegerProperty(id);
        this.clientFullName = new SimpleStringProperty(fullname);
        this.clientType = new SimpleStringProperty(type);
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

    public String getClientFullName() {
        return clientFullName.get();
    }

    public SimpleStringProperty clientFullNameProperty() {
        return clientFullName;
    }

    public void setClientFullName(String clientFullName) {
        this.clientFullName.set(clientFullName);
    }

    public String getClientType() {
        return clientType.get();
    }

    public SimpleStringProperty clientTypeProperty() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType.set(clientType);
    }
}
