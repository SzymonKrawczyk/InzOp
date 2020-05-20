package InzOp;

import javafx.scene.control.Button;
import javafx.scene.image.Image;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageEntity {

    int id;
    String from;
    String to;
    String messageText;
    String timestamp;
    long timestampConvertedToMilliSeconds;



    public MessageEntity(int id, String from, String to, String messageText, String timestamp){
        this.id = id;
        this.from = from;
        this.to = to;
        this.messageText = messageText;
        this.timestamp = timestamp;
        this.timestampConvertedToMilliSeconds = getMS(timestamp);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public long gettimestampConvertedToMilliSeconds() {
        return timestampConvertedToMilliSeconds;
    }

    public void setTimestampConvertedToMilliSeconds(long timestampConvertedToMilliSecounds) {
        this.timestampConvertedToMilliSeconds = timestampConvertedToMilliSecounds;
    }

    //


    @Override
    public String toString() {
        return "MessageEntity{" +
                "id=" + id +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", messageText='" + messageText + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", timestampConvertedToMilliSeconds=" + timestampConvertedToMilliSeconds +
                '}' + "\n";
    }

    private long getMS(String date ) {

        long millis=0;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dateInMs = sdf.parse(date);
            millis = dateInMs.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //System.out.println(millis);

        return millis;   ///
    }
}
