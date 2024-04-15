package com.cognicx.AppointmentRemainder.Dto;

public class RetryCountDto {
	
	private Integer retryOne;
	private Integer retryTwo;
	private Integer retryThree;
	private Integer retryFour;
	private Integer retryFive;
	private Integer retrySix;
	private Integer retrySeven;
	private Integer retryEight;
	private Integer above;
	
	public RetryCountDto(){
		retryOne = 0;
		retryTwo = 0;
		retryThree = 0;
		retryFour = 0;
		retryFive = 0;
		retrySix = 0;
		retrySeven = 0;
		retryEight = 0;
		above = 0;
	}
	
	public Integer getRetryOne() {
		return retryOne;
	}
	public void setRetryOne(Integer retryOne) {
		this.retryOne = retryOne;
	}
	public Integer getRetryTwo() {
		return retryTwo;
	}
	public void setRetryTwo(Integer retryTwo) {
		this.retryTwo = retryTwo;
	}
	public Integer getRetryThree() {
		return retryThree;
	}
	public void setRetryThree(Integer retryThree) {
		this.retryThree = retryThree;
	}
	public Integer getRetryFour() {
		return retryFour;
	}
	public void setRetryFour(Integer retryFour) {
		this.retryFour = retryFour;
	}
	public Integer getRetryFive() {
		return retryFive;
	}
	public void setRetryFive(Integer retryFive) {
		this.retryFive = retryFive;
	}
	public Integer getRetrySix() {
		return retrySix;
	}
	public void setRetrySix(Integer retrySix) {
		this.retrySix = retrySix;
	}
	public Integer getRetrySeven() {
		return retrySeven;
	}
	public void setRetrySeven(Integer retrySeven) {
		this.retrySeven = retrySeven;
	}
	public Integer getRetryEight() {
		return retryEight;
	}
	public void setRetryEight(Integer retryEight) {
		this.retryEight = retryEight;
	}
	public Integer getAbove() {
		return above;
	}
	public void setAbove(Integer above) {
		this.above = above;
	}
}
