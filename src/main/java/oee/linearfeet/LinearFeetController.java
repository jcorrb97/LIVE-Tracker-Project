package oee.linearfeet;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

@RestController
public class LinearFeetController {
	
	//SQL Connection Setup
	String connectionUrl = 
			"jdbc:sqlserver://mmcsapprod;"
			        + "integratedSecurity=true;"
					//+ "database=MTP;"
					+ "encrypt=true;"
					+ "trustServerCertificate=true;";
					//+ "loginTimeout=30;";
	
	ResultSet resultSet = null;
	
	@Autowired
	LinearFeetRepository linearFeetRepository;
	
	

	//Shows all items in the internal memory h2 database on the home page
	@GetMapping("/")
	List<LinearFeet> getAllEntrys(){
		return linearFeetRepository.findAll();
	}
	
	//Added 07/06/2022 - Attempt to connect to SAP Database and execute query to search for manufacturing order
	@PutMapping("/orderInfo/{id}/{orderNumber}")
	LinearFeet getOrderInfo(@PathVariable long id, @PathVariable String orderNumber) throws ClassNotFoundException {
		int missingChars = 12 - orderNumber.length();
		switch (missingChars) {
		case 5:
			orderNumber = "00000" + orderNumber;
			break;
		case 4:
			orderNumber = "0000" + orderNumber;
			break;
		}
		
		LinearFeet orderInfoUpdate = linearFeetRepository.getReferenceById(id);
		try (Connection connection = DriverManager.getConnection(connectionUrl);
				Statement statement = connection.createStatement();){
			
			String selectSql = "USE MTP SELECT GAMNG, GMEIN, PLNBEZ FROM mtp.AFKO WHERE AUFNR = " + orderNumber;
			resultSet = statement.executeQuery(selectSql);
			
			orderInfoUpdate.setOrderQuantity(resultSet.getInt("GAMNG"));
			orderInfoUpdate.setUnit(resultSet.getString("GMEIN"));
			orderInfoUpdate.setItemNumber(resultSet.getString("PLNBEZ"));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		linearFeetRepository.saveAndFlush(orderInfoUpdate);
		return orderInfoUpdate;
	}
	
	//Returns item that has their job status value as live
	@CrossOrigin
	@GetMapping("/getLive")
	List<LinearFeet> getLive(){
		return linearFeetRepository.findByStatusIsLive();
	}
	
	//This goes with the Arduino HTTP request that once the job is clocked in, it creates an ID and stores in the arduino memory to be able to update values by id
	@GetMapping("/getID")
	long getEntryID() {
		return linearFeetRepository.count();
	}

	///Updates the linear feet colum value for the designated ID. Arduino sends a request every 500 miliseconds to update once it is clocked into a job
	@PutMapping("/entry/linearFeetUpdate/{id}/{lf}")
	LinearFeet UpdateLinearFeet(@PathVariable long id, @PathVariable double lf) {
		LinearFeet linearFeetUpdate = linearFeetRepository.getReferenceById(id);
		linearFeetUpdate.setLinearFeet(lf);
		linearFeetRepository.saveAndFlush(linearFeetUpdate);
		return linearFeetUpdate;
	}
	
	
	//Changes the job status column to CLOSED
	@PutMapping("/entry/endTimeUpdate/{id}")
	LinearFeet UpdateEndTime(@PathVariable long id) {
		LinearFeet newEndTime = linearFeetRepository.getReferenceById(id);
		
		newEndTime.setEndTime(localDateTime());
		newEndTime.setStatus("CLOSED");
		linearFeetRepository.saveAndFlush(newEndTime);
		return newEndTime;
	}
	
	//This initializes a job and creates a row in the internal H2 database
	@PostMapping("/entry/post/{department}/{wcid}/{lf}")
	LinearFeet PostEntry(@PathVariable String department, 
			@PathVariable String wcid, @PathVariable double lf) {
		LinearFeet newEntry = new LinearFeet();
		
		newEntry.setStartTime(localDateTime());
		newEntry.setStatus("LIVE");
		newEntry.setDepartment(department);
		newEntry.setWorkCenterID(wcid);
		newEntry.setLinearFeet(lf);
		linearFeetRepository.saveAndFlush(newEntry);
		return newEntry;
	}
	
	//Sends request to update current time and returns in specific format
	String localDateTime() {
		LocalDateTime DateTime = LocalDateTime.now();
		DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("MM-dd-yyy HH:mm:ss");
		String formattedDateTime = DateTime.format(myFormat);
		return formattedDateTime;
	}
}
