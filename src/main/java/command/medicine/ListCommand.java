package command.medicine;

import command.Command;
import command.CommandParameters;
import command.CommandSyntax;
import comparators.StockComparator;
import inventory.Medicine;
import inventory.Stock;
import parser.DateParser;
import ui.Ui;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Helps to process the list command together with filters and sort.
 */

public class ListCommand extends Command {

    @Override
    public void execute(Ui ui, HashMap<String, String> parameters, ArrayList<Medicine> medicines) {
        String[] requiredParameter = {};
        String[] optionalParameters = {CommandParameters.STOCK_ID, CommandParameters.NAME, CommandParameters.PRICE,
            CommandParameters.QUANTITY, CommandParameters.EXPIRY_DATE, CommandParameters.DESCRIPTION,
            CommandParameters.MAX_QUANTITY, CommandParameters.SORT, CommandParameters.REVERSED_SORT};

        if (parameters.size() != 0) { // Check for valid parameters only when they are provided
            boolean isInvalidParameter = CommandSyntax.containsInvalidParameters(ui, parameters, requiredParameter,
                    optionalParameters, CommandSyntax.LIST_COMMAND, "list");
            if (isInvalidParameter) {
                return;
            }

            boolean isInvalidParameterValues = CommandSyntax.containsInvalidParameterValues(ui, parameters, medicines);
            if (isInvalidParameterValues) {
                return;
            }
        }

        ArrayList<Medicine> filteredMedicines = new ArrayList<>();
        for (Medicine medicine : medicines) {
            if (medicine instanceof Stock) { // Ensure that it is a medicine object
                filteredMedicines.add(medicine);
            }
        }
        for (String parameter : parameters.keySet()) {
            String parameterValue = parameters.get(parameter);
            switch (parameter) {
            case CommandParameters.STOCK_ID:
                filteredMedicines = (ArrayList<Medicine>) filteredMedicines.stream()
                        .filter((m) -> ((Stock) m).getStockID() == Integer.parseInt(parameterValue))
                        .collect(Collectors.toList());
                break;
            case CommandParameters.NAME:
                filteredMedicines = (ArrayList<Medicine>) filteredMedicines.stream()
                        .filter((m) -> m.getMedicineName().equals(parameterValue))
                        .collect(Collectors.toList());
                break;
            case CommandParameters.PRICE:
                filteredMedicines = (ArrayList<Medicine>) filteredMedicines.stream()
                        .filter((m) -> ((Stock) m).getPrice() == Double.parseDouble(parameterValue))
                        .collect(Collectors.toList());
                break;
            case CommandParameters.QUANTITY:
                filteredMedicines = (ArrayList<Medicine>) filteredMedicines.stream()
                        .filter((m) -> m.getQuantity() == Integer.parseInt(parameterValue))
                        .collect(Collectors.toList());
                break;
            case CommandParameters.EXPIRY_DATE:
                try {
                    Date expiryDate = DateParser.stringToDate(parameterValue);
                    filteredMedicines = (ArrayList<Medicine>) filteredMedicines.stream()
                            .filter((m) -> ((Stock) m).getExpiry().equals(expiryDate))
                            .collect(Collectors.toList());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case CommandParameters.DESCRIPTION:
                filteredMedicines = (ArrayList<Medicine>) filteredMedicines.stream()
                        .filter((m) -> ((Stock) m).getDescription().equals(parameterValue))
                        .collect(Collectors.toList());
                break;
            case CommandParameters.MAX_QUANTITY:
                filteredMedicines = (ArrayList<Medicine>) filteredMedicines.stream()
                        .filter((m) -> ((Stock) m).getMaxQuantity() == Integer.parseInt(parameterValue))
                        .collect(Collectors.toList());
                break;
            case CommandParameters.SORT:
                filteredMedicines.sort(new StockComparator(parameterValue.toUpperCase(), false));
                break;
            case CommandParameters.REVERSED_SORT:
                filteredMedicines.sort(new StockComparator(parameterValue.toUpperCase(), true));
                break;
            default:
                ui.printInvalidParameter(parameter, CommandSyntax.LIST_COMMAND);
                return;
            }
        }
        ui.printStocks(filteredMedicines);
    }
}