package sample.utils;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;

public class EditUtil {
    public static void onEditProperty(TableView<?> tableView) {
        tableView.setEditable(true);
        tableView.getSelectionModel().cellSelectionEnabledProperty().set(true);

        tableView.setOnKeyPressed(event -> {
            if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
                editFocusedCell(tableView);
            } else if (event.getCode() == KeyCode.RIGHT ||
                    event.getCode() == KeyCode.TAB) {
                tableView.getSelectionModel().selectNext();
                event.consume();
            } else if (event.getCode() == KeyCode.LEFT) {
                selectPrevious(tableView);
                event.consume();
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static void editFocusedCell(TableView<?> tableView) {
        final TablePosition focusedCell = tableView
                .focusModelProperty().get().focusedCellProperty().get();
        tableView.edit(focusedCell.getRow(), focusedCell.getTableColumn());
    }

    @SuppressWarnings("unchecked")
    public static void selectPrevious(TableView<?> tableView) {
        if (tableView.getSelectionModel().isCellSelectionEnabled()) {
            TablePosition pos = tableView.getFocusModel()
                    .getFocusedCell();
            if (pos.getColumn() - 1 >= 0) {

                tableView.getSelectionModel().select(pos.getRow(),
                        getTableColumn(pos.getTableColumn(), -1, tableView));
            }
        } else {
            int focusIndex = tableView.getFocusModel().getFocusedIndex();
            if (focusIndex == -1) {
                tableView.getSelectionModel().select(tableView.getItems().size() - 1);
            } else if (focusIndex > 0) {
                tableView.getSelectionModel().select(focusIndex - 1);
            }
        }
    }

    private static TableColumn getTableColumn(
            final TableColumn column, int offset, TableView<?> tableView) {
        int columnIndex = tableView.getVisibleLeafIndex(column);
        int newColumnIndex = columnIndex + offset;
        return tableView.getVisibleLeafColumn(newColumnIndex);
    }


}
