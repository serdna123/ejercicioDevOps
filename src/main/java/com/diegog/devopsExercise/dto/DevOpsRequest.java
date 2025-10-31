package com.diegog.devopsExercise.dto;

import jakarta.validation.constraints.*;


public class DevOpsRequest {
	  @NotBlank
	  private String message;
	  @NotBlank
	  private String to;
	  @NotBlank
	  private String from;
	  @Min(1) @Max(3600)
	  private int timeToLifeSec;

	  // getters/setters
	  public String getMessage() { return message; }
	  public void setMessage(String message) { this.message = message; }
	  public String getTo() { return to; }
	  public void setTo(String to) { this.to = to; }
	  public String getFrom() { return from; }
	  public void setFrom(String from) { this.from = from; }
	  public int getTimeToLifeSec() { return timeToLifeSec; }
	  public void setTimeToLifeSec(int timeToLifeSec) { this.timeToLifeSec = timeToLifeSec; }
}
