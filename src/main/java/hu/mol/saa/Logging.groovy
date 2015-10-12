package hu.mol.saa

import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * Created by TaTurcsek on 10/12/2015.
 */
class Logging {

    private final static File FILE = new File("eventlog.log");
    static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    static Date date = new Date();

    public static void successLogon(String username) {
        String data = "" + new Date(dateFormat.format(date)) + " - Successful logon with user: [" + username.trim() + "]\n";
        writeToFile(data)
    }

    public static void failedLogon(String username) {
        String data = "" + new Date(dateFormat.format(date)) + " - Failed logon with user: [" + username.trim() + "]\n";
        writeToFile(data)
    }

    public static void successLogoff(String username) {
        String data = "" + new Date(dateFormat.format(date)) + " - Successful logoff with user: [" + username.trim() + "]\n";
        writeToFile(data)
    }

    public static void removeRelation(String username, String relation) {
        String data = "" + new Date(dateFormat.format(date)) + " - Removed relation: [" + relation.trim() + "] by user: [" + username.trim() + "]\n";
        writeToFile(data)
    }

    public static void addRelation(String username, String relation) {
        String data = "" + new Date(dateFormat.format(date)) + " - Added relation: [" + relation.trim() + "] by user: [" + username.trim() + "]\n";
        writeToFile(data)
    }

    public static void removeApp(String username, String app) {
        String data = "" + new Date(dateFormat.format(date)) + " - Removed application: [" + app.trim() + "] by user: [" + username.trim() + "]\n";
        writeToFile(data)
    }

    public static void addApp(String username, String app) {
        String data = "" + new Date(dateFormat.format(date)) + " - Added application: [" + app.trim() + "] by user: [" + username.trim() + "]\n";
        writeToFile(data)
    }

    private static void writeToFile(String data) {
        try {
            if (!FILE.exists()) {
                FILE.createNewFile();
            }

            //true = append file
            FileWriter fileWritter = new FileWriter(FILE.getName(), true);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.write(data);
            bufferWritter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


