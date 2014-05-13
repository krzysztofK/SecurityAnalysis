package pl.edu.agh.security.warehouse.client;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "registerTransactionResponse")
@XmlType(name = "registerTransactionResponse", namespace = "http://warehouse.deps.security.agh.edu.pl/")
public enum WarehouseResponse {

    OK("Ok"), ERROR("Something has gone wrong");
    private String statusText;

    private WarehouseResponse(String status) {
        statusText = status;
    }

    public String getStatusText() {
        return statusText;
    }
}
