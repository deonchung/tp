package command.medicine;

import command.Command;
import command.CommandParameters;
import command.CommandSyntax;
import inventory.Stock;
import inventory.Medicine;
import parser.DateParser;
import parser.MedicineManager;
import parser.StockValidator;

import ui.Ui;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Add medication based on user input.
 * User input include name, price, quantity, expiry date, description and maximum quantity of medication.
 */
public class AddCommand extends Command {

    @Override
    public void execute(Ui ui, HashMap<String, String> parameters, ArrayList<Medicine> medicines) {

        String nameToAdd = parameters.get(CommandParameters.NAME);
        String priceToAdd = parameters.get(CommandParameters.PRICE);
        String quantityToAdd = parameters.get(CommandParameters.QUANTITY);
        String expiryToAdd = parameters.get(CommandParameters.EXPIRY_DATE);
        String descriptionToAdd = parameters.get(CommandParameters.DESCRIPTION);
        String maxQuantityToAdd = parameters.get(CommandParameters.MAX_QUANTITY);

        try {
            double price = Double.parseDouble(priceToAdd);
            int quantity = Integer.parseInt(quantityToAdd);
            Date formatExpiry = DateParser.stringToDate(expiryToAdd);
            int totalStock = MedicineManager.getTotalStockQuantity(medicines, nameToAdd);

            for (Medicine medicine : medicines) {
                Stock existingStock = (Stock) medicine;
                String existingName = existingStock.getMedicineName().toUpperCase();
                String existingDescription = existingStock.getDescription();
                int existingMaxQuantity = existingStock.getMaxQuantity();
                boolean isValidQuantity =
                        StockValidator.quantityValidityChecker(ui, (totalStock + quantity), existingMaxQuantity);
                boolean nameExist = existingName.equals(nameToAdd.toUpperCase());
                boolean isValidDate = StockValidator.dateValidityChecker(ui, medicines, formatExpiry, nameToAdd);
                if (nameExist) {
                    String[] requiredParameters = {CommandParameters.NAME, CommandParameters.PRICE,
                                                   CommandParameters.QUANTITY, CommandParameters.EXPIRY_DATE};
                    String[] optionalParameters = {CommandParameters.DESCRIPTION, CommandParameters.MAX_QUANTITY};

                    if (checkValidParametersAndValues(ui, parameters, medicines,
                            requiredParameters, optionalParameters)) {
                        return;
                    }

                    if (!isValidDate) {
                        return;
                    }

                    if (!isValidQuantity) {
                        return;
                    }
                    addMedicine(ui, medicines, nameToAdd, existingDescription, price,
                            quantity, formatExpiry, existingMaxQuantity);
                    return;

                }

            }

            String[] requiredParameters = {CommandParameters.NAME, CommandParameters.PRICE, CommandParameters.QUANTITY,
                                           CommandParameters.EXPIRY_DATE, CommandParameters.DESCRIPTION,
                                           CommandParameters.MAX_QUANTITY};
            String[] optionalParameters = {};

            if (checkValidParametersAndValues(ui, parameters, medicines, requiredParameters, optionalParameters)) {
                return;
            }

            int maxQuantity = Integer.parseInt(maxQuantityToAdd);

            addMedicine(ui, medicines, nameToAdd, descriptionToAdd, price, quantity, formatExpiry, maxQuantity);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private boolean checkValidParametersAndValues(Ui ui, HashMap<String, String> parameters,
                                                  ArrayList<Medicine> medicines, String[] requiredParameters,
                                                  String[] optionalParameters) {

        if (CommandSyntax.containsInvalidParameters(ui, parameters, requiredParameters, optionalParameters,
                CommandSyntax.ADD_COMMAND, false)) {
            return true;
        }

        if (CommandSyntax.containsInvalidParameterValues(ui, parameters, medicines)) {
            return true;
        }

        return false;
    }

    private void addMedicine(Ui ui, ArrayList<Medicine> medicines, String name, String description,
                             double price, int quantity, Date expiryDate, int maxQuantity) {
        Stock stock = new Stock(name, price, quantity, expiryDate, description, maxQuantity);
        medicines.add(stock);
        ui.print("Medication added: " + name);
        ui.printStock(stock);
    }

}


