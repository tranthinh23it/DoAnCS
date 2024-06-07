package DTO;

import java.util.Objects;

public class Customers {
    private int customerid;
    private String customername;

    public int getCustomerid() {
        return customerid;
    }

    public void setCustomerid(int customerid) {
        this.customerid = customerid;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customers customers = (Customers) o;
        return customerid == customers.customerid && Objects.equals(customername, customers.customername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerid, customername);
    }
}
