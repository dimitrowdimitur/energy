package sample.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Property {
    SimpleIntegerProperty id;
    SimpleStringProperty address;
    SimpleIntegerProperty maintenance;

    public Property(String address, Integer maintenance, Integer id) {
        this.id = new SimpleIntegerProperty(id);
        this.address = new SimpleStringProperty(address);
        this.maintenance = new SimpleIntegerProperty(maintenance);
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

    public String getAddress() {
        return address.get();
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public int getMaintenance() {
        return maintenance.get();
    }

    public SimpleIntegerProperty maintenanceProperty() {
        return maintenance;
    }

    public void setMaintenance(int maintenance) {
        this.maintenance.set(maintenance);
    }
}
