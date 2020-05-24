//
//
// Jeśli chodzi o dodawanie ilości logowań jak i wiadomości to postanowiłem zrobić je razem ( w tym samym wątku ) w tej samej formie
// tzn godzina : licznba logowań/wiadomości.
// nie wiem dokładnie czy tego chciałeś.
// btw, poprzedno źle policzyłem godziny i minuty na ms więc jeśli użyłeś poprzedniego kodu to podmień wartości


package InzOp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class RaportGenerator implements Runnable {

    long sleeping_time;
    String fileNameLogInCounter;
    String fileNameMessageCounter;

    RaportGenerator(){
        this.fileNameLogInCounter =  ("raports/statistics/log_in_counter_" + java.time.LocalDate.now() + ".txt");
        this.fileNameMessageCounter =  ("raports/statistics/message_counter_" + java.time.LocalDate.now() + ".txt");

        File logInCounterFile = new File(fileNameLogInCounter);
        try {
            if( logInCounterFile.createNewFile() ) {
                System.out.println("utworzono nowy plik " + fileNameLogInCounter + ".txt");
            } else {
                System.out.println("plik " + fileNameLogInCounter + " już istanieje");
            }
        }catch (Exception e){}

        File messageCounterFile = new File(fileNameMessageCounter);
        try {
            if( messageCounterFile.createNewFile() ) {
                System.out.println("utworzono nowy plik " + fileNameMessageCounter + ".txt");
            } else {
                System.out.println("plik " + fileNameMessageCounter + " już istanieje");
            }
        }catch (Exception e){}


        Date date = new Date();
        this.sleeping_time =  3600000 - (date.getMinutes()*60000 + date.getSeconds()*1000);
    }


    @Override
    public void run() {

        while(true){
            try {
                Thread.sleep(sleeping_time);
            }catch (Exception e) {}

            save();

            this.sleeping_time = 3600000;

            MainSerwer.logInCounter = 0;
            MainSerwer.messageCounter = 0;
            update_file_name_date();
        }
    }

    void update_file_name_date(){
        this.fileNameLogInCounter =  ("raports/statistics/log_in_counter_" + java.time.LocalDate.now() + ".txt");
        this.fileNameMessageCounter =  ("raports/statistics/message_counter_" + java.time.LocalDate.now() + ".txt");
    }



    public void save()
    {
        try
        {
            Date date = new Date();
            FileWriter fw = new FileWriter(fileNameLogInCounter,true); //the true will append the new data
            //BufferedWriter bw = new BufferedWriter(fw);
            fw.write(  date.getHours() +  "-" + (date.getHours()+1) + " : " + MainSerwer.logInCounter  + "\n");//appends the string to the file
            //bw.newLine();
            fw.flush();
            fw.close();

            System.out.println("Wpisano: " + MainSerwer.logInCounter + " do pliku" + " o:" + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds());
        }
        catch(IOException ioe)
        {
            System.err.println("cosik nie wyszło przy dopisywaniu ilości logowań do pliku");
        }

        try
        {
            Date date = new Date();
            FileWriter fw = new FileWriter(fileNameMessageCounter,true); //the true will append the new data
            //BufferedWriter bw = new BufferedWriter(fw);
            fw.write(  date.getHours() +  "-" + (date.getHours()+1) + " : " + MainSerwer.messageCounter  + "\n");//appends the string to the file
            //bw.newLine();
            fw.flush();
            fw.close();

            System.out.println("Wpisano: " + MainSerwer.messageCounter + " do pliku" + " o:" + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() );
        }
        catch(IOException ioe)
        {
            System.err.println("cosik nie wyszło przy dopisywaniu ilości logowań do pliku");
        }
    }
}
