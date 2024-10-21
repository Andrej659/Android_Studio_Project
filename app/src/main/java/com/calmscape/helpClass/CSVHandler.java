package com.calmscape.helpClass;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * This is CSVHandler class, the only usage for this class currently is saving and fetching data from
 * a local csv file.
 */
public class CSVHandler {

    /**
     * This is method fetches the data from the csv file.
     *
     * @param context provides context about where is the method used
     * @param fileName the name of the csv file
     * @return returns a list of tasks we set up
     */
    public static List<String> loadFromCSV(Context context, String fileName) {

        List<String> data = new ArrayList<>();  //storing data into this list
        try {

            File file = new File(context.getFilesDir(), fileName);
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;

            while ((line = reader.readLine()) != null) {  //going through all data
                data.add(line); //adding it to the list
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    /**
     * This is method saves the data to the csv file.
     *
     * @param context provides context about where is the method used
     * @param fileName the name of the csv file
     * @param data the data we want to add to the csv file
     */
    public static void saveToCSV(Context context, String fileName, String data) {

        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_APPEND)) {

            fos.write((data + "\n").getBytes());

        } catch (IOException e) {

            e.printStackTrace();

        }
    }


    /**
     * This is method deletes the data from the csv file.
     *
     * @param context provides context about where is the method used
     * @param fileName the name of the csv file
     * @param itemToDelete the data we want to delete from the csv file
     */
    public static void deleteItemFromCSV(Context context, String fileName, String itemToDelete) {

        List<String> data = loadFromCSV(context, fileName);  //firstly we get the data
        data.remove(itemToDelete);   //removing unwanted data

        try {

            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fos);

            for (String line : data) {  //saving the data again
                writer.write(line + "\n");
            }

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
