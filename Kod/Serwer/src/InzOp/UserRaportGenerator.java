package InzOp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserRaportGenerator implements Runnable{

    long sleeping_time;
    String fileNameLogInCounter;
    String fileNameMessageCounter;

    UserRaportGenerator(){
        // policzenine czasu do końca dnia
        Date date = new Date();
        this.sleeping_time = 86400000 - ( date.getHours()*3600000 - date.getMinutes()*60000 + date.getSeconds()*1000);
    }

    @Override
    public void run() {

        while(true){
            try {
                Thread.sleep(sleeping_time);
            }catch (Exception e) {}

            this.sleeping_time = 86400000;
            save();
        }
    }


    public void save()
    {
        Date date = new Date();

        for(int i=0; i<MainSerwer.ClientList.size(); i++){
            try
            {
                if (!MainSerwer.ClientList.get(i).isGroup() && MainSerwer.ClientList.get(i).isActive()) {

                    FileWriter fw = new FileWriter("raports/users/" + MainSerwer.ClientList.get(i).getName() + ".txt",true);
                    //BufferedWriter bw = new BufferedWriter(fw);
                    SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
                    fw.write(  SDF.format(date) + ": " + (MainSerwer.ClientList.get(i).getTimeOnlineForRaport() / 60000 )  + "\n");
                    //bw.newLine();
                    fw.flush();
                    fw.close();
                    System.out.println("Wpisano: " + (MainSerwer.ClientList.get(i).getTimeOnlineForRaport() / 60000 ) + " do pliku "+MainSerwer.ClientList.get(i).getName() + " o:" + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds());
                }
            }
            catch(IOException ioe) {}
        }
    }
}
