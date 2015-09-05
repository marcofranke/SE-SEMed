package de.biba.informationrequestservice;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.biba.informationrequestservice.datatypes.DataSource;
import de.biba.informationrequestservice.datatypes.InformationServices;
import de.biba.informationrequestservice.datatypes.Service;
import de.biba.mediator.IMediator;
import de.biba.mediator.ReasoningMediator;
import de.biba.wrapper.DataSourceV2;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;


/**[InformationRequestService. This is a mediation based data integration approach which aggregate data sources via Wrapper]
Copyright (C) [2015  [Marco Franke (BIBA-Bremer Institut für Produktion und Logistik GmbH)]

This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    **/

public class InformationServiceManager {

	private static InformationServiceManager Instance = null;
	private ArrayList<IMediator> allMediators = new ArrayList<IMediator>();//Diese Liste wird einmal beim starten initailisiet
	private Scheduler scheduler = null;
	private InformationServices myServices = null;
	private JAXBContext context = null;
	private List<Boolean> whichServicesAreClosable = new ArrayList<Boolean>();
	private ArrayList<String> allStartedServices = new ArrayList<String>();
	private final String file = "./Resources/ExampleServices.xml";

		
	public int getNextAvailableID(){
		if (myServices !=null){
		return myServices.getService().size()+1;
		}
		else{ return -1;}
	}
	
	public static InformationServiceManager getInstance() throws Exception {
		if (Instance == null) {
			new InformationServiceManager();
		}
		return Instance;
	}

	public  ArrayList<IMediator> getAllMediators() {
		return allMediators;
	}

	private InformationServiceManager() throws Exception {

		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
		} catch (Exception ex) {
			System.err.println(ex);
			throw( ex);
		}

		try {

			try {
				File f = new File("./Resources/ExampleServices.xml");
				Logger.getAnonymousLogger().log(Level.INFO, "Loading file: " + f.getAbsolutePath());
				System.out.println("Loading file: " + f.getAbsolutePath());
				FileReader schemaReader = new FileReader(
						"./Resources/ServiceDataDescription.xsd");
				FileReader mappingReader = new FileReader(
						"./Resources/ExampleServices.xml");
				Validator validator = SchemaFactory
						.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
						.newSchema(new StreamSource(schemaReader))
						.newValidator();
				validator.validate(new StreamSource(mappingReader));
				schemaReader.close();
				mappingReader.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error: " + e);
				throw( e);
			}

			context = JAXBContext
					.newInstance("de.biba.informationrequestservice.datatypes");
			Unmarshaller m = context.createUnmarshaller();
			File f = new File(file);
			if (f.exists()) {
				Object ob = m.unmarshal(f);
				myServices = (InformationServices) ob;
				for (Service service: myServices.getService()){
					addAMediator(service);
					whichServicesAreClosable.add(false);
				}

			} else {
				System.err.println("Sorry, your config file was not found: "
						+ f.getAbsolutePath());
				throw (new FileNotFoundException());
			}

		} catch (Exception e) {
			System.err.println("Error: " + e);
			throw( e);
		}
		Instance = this;
	}
	
	private void addAMediator(Service service) throws Exception{
		IMediator mediator = new ReasoningMediator();
		allMediators.add(mediator);
		for (DataSource source: service.getDataSource()){
			Class<?> c = null;
			try{
				String className = source.getJavaClassForWrapper();
				c=Class.forName(className);
				Object o = c.newInstance();
				de.biba.wrapper.DataSource wrapper = (de.biba.wrapper.DataSource) o;
				wrapper.initialize(source.getPathToConfigFile());
				boolean failureFree = true;
				if (wrapper instanceof DataSourceV2){
				if (((DataSourceV2) wrapper).validateConfiguration().isConsistent()==false){
					failureFree = false;
					System.err.println( ((DataSourceV2) wrapper).validateConfiguration().getErrorMessage());
					failureFree = true;//TODO this have to implement
				}
				if (failureFree){
					mediator.addDataSource(wrapper, 0);
				}
				}
				else{
					mediator.addDataSource(wrapper, 0);
				}
			}catch(Exception e){
				e.printStackTrace();
				throw (e);
			}
		}
	}
	
	public List<Boolean> getWhichServicesAreClosable() {
		return whichServicesAreClosable;
	}

	public boolean canBeClosed(){
		
		for (Boolean closeable: whichServicesAreClosable){
			if (!closeable){
				return false;
			}
		}
		
		return true;
	}
	
	public void removeAService(int index){
		myServices.getService().remove(index);
		allMediators.remove(index);
		whichServicesAreClosable.remove(index);
				try {
					Marshaller m = context.createMarshaller();
					m.marshal(myServices, new File(file));
				} catch (JAXBException e) {
					e.printStackTrace();
				}
	}

	public void removeAService(Service service){
		for (int i =0; i < myServices.getService().size();i++){
			if (myServices.getService().get(i).getId()==service.getId()){
				if (myServices.getService().get(i).getQuery().equals(service.getQuery())){
				//Service gefunden, welcher gelöscht werden soll
					removeAService(i);
				break;
				}
			}
		}
		
		
	}
	
	
	public void addAService(Service service) throws Exception{
		service.setId(myServices.getService().size());
		
		myServices.getService().add(service);
		 addAMediator(service);
		 whichServicesAreClosable.add(false);
		try {
			Marshaller m = context.createMarshaller();
			m.marshal(myServices, new File(file));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
	}
	
	public List<Service> getAllServices() {
		return myServices.getService();
		
	}

	
	public void stopOneService(int index){
		if (allStartedServices .contains(String.valueOf(index))){
		JobKey key = new JobKey("job" + index, "group1");		
		try {
			scheduler.deleteJob(key);
			allStartedServices .remove(String.valueOf(index));
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		}
		else{
			System.err.println("No Servicehas been already inserted");
		}
	}
	
	public boolean hasBeenStarted(int index){
	
		return (allStartedServices.contains(String.valueOf(index)) );
		
	}
	
	
	
	public void startOneService(int index, boolean islokal) {

		if (!allStartedServices .contains(String.valueOf(index))){
		JobDetail job = newJob(ServiceJob.class)
				.withIdentity("job" + index, "group1")
				.usingJobData("ServiceIndex", index).build();
		job.getJobDataMap().put("isLocal", islokal);
		// Trigger the job to run now, and then repeat every 40 seconds
		int t = myServices.getService().get(index).getRequestInterval();
		if (t == 0){ t = 1000000000;}
		Trigger trigger = newTrigger()
				.withIdentity("trigger" + index, "group1")
				.startNow()
				.withSchedule(
						simpleSchedule().withIntervalInSeconds(t)
						.repeatForever()).build();
		try {
			scheduler.scheduleJob(job, trigger);
			allStartedServices .add(String.valueOf(index));
			
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		}
		else{
			System.err.println("Service was already inserted");
		}
	}
	public void startOneService(int index, boolean islokal, String link) {

		if (!allStartedServices .contains(String.valueOf(index))){
		JobDetail job = newJob(ServiceJob.class)
				.withIdentity("job" + index, "group1")
				.usingJobData("ServiceIndex", index).build();
		job.getJobDataMap().put("isLocal", islokal);
		job.getJobDataMap().put("WebserviceLink", link);
		// Trigger the job to run now, and then repeat every 40 seconds
		Trigger trigger = newTrigger()
				.withIdentity("trigger" + index, "group1")
				.startNow()
				.withSchedule(
						simpleSchedule().withIntervalInSeconds(myServices.getService().get(index).getRequestInterval())
						.repeatForever()).build();
		try {
			scheduler.scheduleJob(job, trigger);
			allStartedServices .add(String.valueOf(index));
			
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		}
		else{
			System.err.println("Service was already inserted");
		}
	}

	public void startAllServices() {
		// define the job and tie it to our HelloJob class
		for (int i = 0; i < myServices.getService().size(); i++) {
			startOneService(i,true);
		}

	}
}
