
package event;
import annotation.Column;
import annotation.Table;
import java.sql.*;
import baseobject.DataObject;

/**
 *
 * @author sarobidy
 */

@Table( table="event" , database="ticketing"  )

public class Event  extends DataObject<Event>{
    
    @Column(isPrimary = true , name = "idEvent")
    String idEvent;
    @Column
    String eventName;
    @Column
    Timestamp dateEvent;
    
    int count = 0;
    
    public Event(){
        
    }
    
    public Event(String id){
        this.setIdEvent(id);
        
    }
    
    public String getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Timestamp getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(Timestamp dateEvent) {
        this.dateEvent = dateEvent;
    }
}
