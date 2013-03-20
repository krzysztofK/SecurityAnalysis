package pl.edu.agh.security.deps.warehouse;

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
