package oee.linearfeet;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer")
public class LinearFeet {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String department;
	private String workCenterID;
	private String status;
	private double linearFeet;
	private String startTime;
	private String endTime;
	
	private String itemNumber;
	private int orderQuantity;
	private String unit;
	
	//Getter methods
	public long getId() {
		return id;
	}
	
	public Double getLinearFeet() {
		return linearFeet;
	}
	
	public String getStatus() {
		return status;
	}
	
	public String getWorkCenterID() {
		return workCenterID;
	}
	
	public String getStartTime() {
		return startTime;
	}
	
	public String getEndTime() {
		return endTime;
	}
	
	public String getDepartment() {
		return department;
	}
	
	//Setter methods
	public void setId(int id) {
		this.id = id;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	
	public void setLinearFeet(Double linearFeet) {
		this.linearFeet = linearFeet;
	}
	
	public void setWorkCenterID(String workCenterID) {
		this.workCenterID = workCenterID;
	}
	
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public int getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(int orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
}
