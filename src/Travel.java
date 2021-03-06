import java.util.ArrayList;
import java.util.Date;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;



public class Travel implements Serializable{

    private Integer ID;
    private Date date;
    private String startPoint;
    private String endPoint;
    private Integer numberOfSeats;
    private User creator;
    private Integer time;
    private ArrayList<User> passengers = new ArrayList<User>();
    private ArrayList<User> passengersRequest = new ArrayList<User>();

    public Travel(Integer ID, Date date, String startPoint, String endPoint,Integer numberOfSeats,User creator){
        this.ID=ID;
        this.date=date;
        this.startPoint=startPoint;
        this.endPoint=endPoint;
        this.numberOfSeats=numberOfSeats;
        this.creator=creator;
    }

    public Travel(Date date, String startPoint, String endPoint,Integer time){
        this.ID=null;
        this.date=date;
        this.startPoint=startPoint;
        this.endPoint=endPoint;
        this.time=time;
        this.creator=null;
    }

    public Integer getTime() {
        return time;
    }

    public boolean addPassenger(User passenger){
        if(passengers.size() >= numberOfSeats)
            return false;
        
        for(int i = 0; i < passengers.size(); i++) {
            if(passengers.get(i).getEmail().equals(passenger.getEmail()))
                return false;
        }
        boolean b = false;
        for(int i = 0; i < passengersRequest.size(); i++) {
            System.out.println("Passenger " + passenger.getEmail());
            System.out.println("Request " + passengersRequest.get(i).getEmail());
            if(passengersRequest.get(i).getEmail().equals(passenger.getEmail())) {
                passengers.add(passenger);
                passengersRequest.remove(passenger);
                passenger.addJoinTravel(this);
                passenger.deleteRequestTravel(this);
                Server.serialize_Object();
                return true;
            }
        }
        
        return false;
    }

    public ArrayList<User> getPassengers(){
        return passengers;
    }

    public ArrayList<User> getPassengersRequest(){
        return passengersRequest;
    }

    public String getStartPoint(){
        return startPoint;
    }

    public String getEndPoint(){
        return endPoint;
    }

    public Date getDate(){
        return date;
    }

    /**
    Make an int Month from a date
    */
    public int getHourInt() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
        return Integer.parseInt(dateFormat.format(date));
    }

    /**
    Make an int Month from a date
    */
    public int getDayInt() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        return Integer.parseInt(dateFormat.format(date));
    }

    /**
    Make an int Month from a date
    */
    public int getMonthInt() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        return Integer.parseInt(dateFormat.format(date));
    }

    /**
    Make an int Year from a date
    */
    public int getYearInt() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        return Integer.parseInt(dateFormat.format(date));
    }

    public boolean removePassenger(User passenger){
        if(passengers.size() == 0)
            return false;
        boolean b = passengers.remove(passenger);
        if(b) {
            passenger.deleteJoinTravel(this);
            passengersRequest.add(passenger);
            passenger.addRequestTravel(this);
            System.out.println("Send Notification to User!!!");
            Server.sendNotificationExitTravel(passenger.getEmail(), Messages.sendNotificationExitTravel(passenger.getEmail(), this.ID.toString(), creator.getEmail()));

        }
        Server.serialize_Object();
        return  b;
    }

    public boolean removePassengersRequest(User passenger){
        
        for(int i = 0; i < passengersRequest.size(); i++) {
            if(passengersRequest.get(i).getEmail().equals(passenger.getEmail())) {
                passengersRequest.remove(passenger);
                passenger.deleteRequestTravel(this);
                Server.serialize_Object();
                return true;
            }
        }
        for(int i = 0; i < passengers.size(); i++) {
            if(passengers.get(i).getEmail().equals(passenger.getEmail())) {
                Server.sendNotificationLeaveTravel(creator.getEmail(), Messages.sendNotificationLeaveTravel(creator.getEmail(), Integer.toString(ID), passengers.get(i).getEmail()));
                passengers.remove(passenger);
                passenger.deleteJoinTravel(this);

                Server.serialize_Object();

                return true;
            }
        }
        return false;
    }

    public User getCreator(){
        return creator;
    }

    public Integer getID(){
        return ID;
    }

    public boolean addPassengerRequest(User user){

        for(int i = 0; i < passengersRequest.size(); i++) {
            if(passengersRequest.get(i).getEmail().equals(user.getEmail()))
                return false;
        }
        for(int i = 0; i < passengers.size(); i++) {
            if(passengers.get(i).getEmail().equals(user.getEmail()))
                return false;
        }
        passengersRequest.add(user);
        user.addRequestTravel(this);
        Server.serialize_Object();
        return true;
    }

    public boolean checkTravel(Date date, String startPoint, String endPoint, Integer time) {
        long hour = 3600*1000;
        Date date2 = new Date(date.getTime()+ time*hour);
        if(startPoint == null || endPoint == null) {
            if(this.date==date){
                return true;
            }
            return false;
        }
        if(this.date.after(date) && this.date.before(date2) && startPoint.equals(this.startPoint) && endPoint.equals(this.endPoint)){
            return true;
        }
        return false;
    }

}