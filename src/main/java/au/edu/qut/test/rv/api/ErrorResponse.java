package au.edu.qut.test.rv.api;

import java.time.OffsetDateTime;

public class ErrorResponse {

  private final String timestamp;
  private int status;
  private String description;

  public ErrorResponse(int status, String description) {
    this.status = status;
    this.description = description;
    this.timestamp = OffsetDateTime.now().toString();
  }

  public String getTimestamp() {
    return timestamp;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
