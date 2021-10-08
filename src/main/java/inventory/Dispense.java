package inventory;

import java.util.Date;

/**
 * Represents a Dispensed object. A Dispensed object is represented by medicine name, quantity, customer's NRIC,
 * date and staff name.
 */

public class Dispense extends Medicine {
    protected String customerNric;
    protected Date date;
    protected String staffName;

    public Dispense(String medicineName, int quantity, String customerName, Date date, String staffName) {
        super(medicineName, quantity);
        this.customerNric = customerName;
        this.date = date;
        this.staffName = staffName;
    }

    public String getCustomerNric() {
        return customerNric;
    }

    public void setCustomerNric(String customerNric) {
        this.customerNric = customerNric;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
}