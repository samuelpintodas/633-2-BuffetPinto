package Server;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log
    {
        //initialize variable
        private Logger myLogger;
        private String path;
        private int getMonth;
        private int getYear;
        private Calendar cal = Calendar.getInstance();
        private FileHandler fh;
        File f;

        // method to create a logfile
        public void createLogger()
        {
            myLogger = Logger.getLogger("Logger");
            getMonth = cal.get(cal.MONTH)+1;
            getYear = cal.get(cal.YEAR);

            // create filename
            if(getMonth>10){
                 f = new File("Logger\\logs-" + getMonth +"-"+getYear + ".txt");
            }
            else{
                f = new File("Logger\\logs-" + "0"+ getMonth +"-"+getYear + ".txt");
            }

            path = f.getName();

            try
            {
                if (!f.getParentFile().exists())
                    f.getParentFile().mkdir();

                if (!f.exists())
                {
                    f.createNewFile();
                }
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //create filehandler
            try
            {
                fh = new FileHandler("./"+path,true);
            }
            catch (SecurityException | IOException e)
            {
                e.printStackTrace();
            }

            myLogger.addHandler(fh);
            SimpleFormatter formater = new SimpleFormatter();
            fh.setFormatter(formater);
        }

        // methode to write log in file with the type of message
        public void write(String text, String severity)
        {
            if (severity.equals("info"))
            {
                myLogger.setLevel(Level.INFO);
                myLogger.info(text);
            }
            else if (severity.equals("warning"))
            {
                myLogger.setLevel(Level.WARNING);
                myLogger.warning(text);
            }
            else if (severity.equals("severe"))
            {
                myLogger.setLevel(Level.SEVERE);
                myLogger.severe(text);
            }
        }
    }
