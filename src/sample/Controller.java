package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import sample.model.Employee;

import java.lang.reflect.Member;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static sample.dbconnection.DBConnection.establishConnection;

public class Controller implements Initializable {

    @FXML
    private TableView<Employee> employTable;
    @FXML
    private TableColumn<Employee, String> name;
    @FXML
    private TableColumn<Employee, Integer> salary;
    @FXML
    private TextField employeeFullName;
    @FXML
    private TextField employeeSalary;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
    }

    @FXML
    public void onAddEmployee(){
        System.out.println("Adding employee " + employeeSalary.getText());
        try {
            Connection connection =  establishConnection();
            Statement ps = connection.createStatement();


            ps.executeUpdate("INSERT INTO employee (fullname, salary) VALUES('"
                    +employeeFullName.getText()+"', '"+ Integer.parseInt(employeeSalary.getText()) +"')");

            loadData();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    public void onDeleteEmployee(){
        try {
            Connection connection =  establishConnection();
            Statement ps = connection.createStatement();


            ps.executeUpdate("DELETE FROM employee WHERE id='"
                    +employTable.getSelectionModel().getSelectedItem().getId() +"'");

            loadData();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    @FXML
    public void onEditEmployee(){
        employTable.setEditable(true);
        // allows the individual cells to be selected
        employTable.getSelectionModel().cellSelectionEnabledProperty().set(true);
        // when character or numbers pressed it will start edit in editable
        // fields
        employTable.setOnKeyPressed(event -> {
        if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
            editFocusedCell();
        } else if (event.getCode() == KeyCode.RIGHT ||
                event.getCode() == KeyCode.TAB) {
            employTable.getSelectionModel().selectNext();
            event.consume();
        } else if (event.getCode() == KeyCode.LEFT) {
            // work around due to
            // TableView.getSelectionModel().selectPrevious() due to a bug
            // stopping it from working on
            // the first column in the last row of the table
            selectPrevious();
            event.consume();
        }
        });
    }

    @SuppressWarnings("unchecked")
    public void editFocusedCell() {
        final TablePosition< Employee, ? > focusedCell = employTable
                .focusModelProperty().get().focusedCellProperty().get();
        employTable.edit(focusedCell.getRow(), focusedCell.getTableColumn());
    }

    @SuppressWarnings("unchecked")
    public void selectPrevious() {
        if (employTable.getSelectionModel().isCellSelectionEnabled()) {
            // in cell selection mode, we have to wrap around, going from
            // right-to-left, and then wrapping to the end of the previous line
            TablePosition < Employee, ? > pos = employTable.getFocusModel()
                    .getFocusedCell();
            if (pos.getColumn() - 1 >= 0) {
                // go to previous row
                employTable.getSelectionModel().select(pos.getRow(),
                        getTableColumn(pos.getTableColumn(), -1));
            } else if (pos.getRow() < employTable.getItems().size()) {
                // wrap to end of previous row
                employTable.getSelectionModel().select(pos.getRow() - 1,
                        employTable.getVisibleLeafColumn(
                                employTable.getVisibleLeafColumns().size() - 1));
            }
        } else {
            int focusIndex = employTable.getFocusModel().getFocusedIndex();
            if (focusIndex == -1) {
                employTable.getSelectionModel().select(employTable.getItems().size() - 1);
            } else if (focusIndex > 0) {
                employTable.getSelectionModel().select(focusIndex - 1);
            }
        }
    }

    private TableColumn < Employee, ? > getTableColumn(
            final TableColumn < Employee, ? > column, int offset) {
        int columnIndex = employTable.getVisibleLeafIndex(column);
        int newColumnIndex = columnIndex + offset;
        return employTable.getVisibleLeafColumn(newColumnIndex);
    }


    public void loadData() {
        name.setCellValueFactory(new PropertyValueFactory<Employee, String>("fullName"));
        salary.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("salary"));
        try {
            Connection connection =  establishConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM employee");
            ResultSet rs = ps.executeQuery();


            List<Employee> employeeList = new ArrayList<>();
            while (rs.next()) {
                Employee employee = new Employee(rs.getString("fullname"), rs.getInt("salary"),
                        rs.getInt("id"));
                employeeList.add(employee);
            }


            ObservableList<Employee> data = FXCollections.observableArrayList(employeeList);
            employTable.setItems(data);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
