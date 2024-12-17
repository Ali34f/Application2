package com.example.application2;

/**
 * Represents a holiday request made by an employee.
 */
public class HolidayRequest {

    // Fields
    private int requestId;
    private String employeeName;
    private String holidayStartDate;
    private String endDate;
    private String reason;
    private String additionalInfo;
    private HolidayRequestStatus status;

    /**
     * Default Constructor initializes fields with default values.
     */
    public HolidayRequest() {
        this.requestId = 0;
        this.employeeName = "";
        this.holidayStartDate = "";
        this.endDate = "";
        this.reason = "";
        this.additionalInfo = "";
        this.status = HolidayRequestStatus.PENDING;
    }

    /**
     * Parameterized Constructor to initialize all fields.
     *
     * @param requestId        The ID of the holiday request.
     * @param employeeName     The name of the employee.
     * @param holidayStartDate The start date of the holiday.
     * @param endDate          The end date of the holiday.
     * @param reason           The reason for the leave.
     * @param additionalInfo   Additional information provided by the employee.
     * @param status           The status of the request (PENDING, APPROVED, REJECTED).
     */
    public HolidayRequest(int requestId, String employeeName, String holidayStartDate, String endDate,
                          String reason, String additionalInfo, HolidayRequestStatus status) {
        this.requestId = requestId;
        this.employeeName = employeeName;
        this.holidayStartDate = holidayStartDate;
        this.endDate = endDate;
        this.reason = reason;
        this.additionalInfo = additionalInfo;
        this.status = status != null ? status : HolidayRequestStatus.PENDING;
    }

    // Getters
    public int getRequestId() {
        return requestId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getHolidayStartDate() {
        return holidayStartDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getReason() {
        return reason;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public HolidayRequestStatus getStatus() {
        return status;
    }

    // Setters
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setHolidayStartDate(String holidayStartDate) {
        this.holidayStartDate = holidayStartDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public void setStatus(HolidayRequestStatus status) {
        this.status = status != null ? status : HolidayRequestStatus.PENDING;
    }

    /**
     * Returns a string representation of the HolidayRequest object.
     *
     * @return String containing all field values.
     */
    @Override
    public String toString() {
        return "HolidayRequest{" +
                "requestId=" + requestId +
                ", employeeName='" + employeeName + '\'' +
                ", holidayStartDate='" + holidayStartDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", reason='" + reason + '\'' +
                ", additionalInfo='" + additionalInfo + '\'' +
                ", status=" + status +
                '}';
    }
}
