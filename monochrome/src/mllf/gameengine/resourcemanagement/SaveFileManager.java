package mllf.gameengine.resourcemanagement;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created By: Max Lesser
 * Date: 11/18/13
 * Time: 12:16 AM
 */
public class SaveFileManager {

    // Writes a save to saveGame.txt based on the input hashmap
    public static void writeSave(HashMap<String, String> saveInfo)
    {
//        try {
//            File f = new File("saveGame.txt");
//            if(!f.isFile())
//                f.createNewFile();
//
//            BufferedWriter writer = null;
//            writer = new BufferedWriter(new FileWriter(f));
//            for(String s : saveInfo.keySet())
//            {
//                writer.write(s);
//                writer.write(":");
//                writer.write(saveInfo.get(s));
//            }
//            writer.close();
//
//        } catch (Exception e)
//        {
//            System.out.println("Error: " + e.getMessage());
//        }
    }

    // Reads the player save from saveGame.txt (contains current level/score/global settings)
    public static HashMap<String, String> readPlayerSave()
    {
        HashMap<String, String> saveInfo = new HashMap<>();
//        try {
//            Scanner s = new Scanner(new File("saveGame.txt")).useDelimiter("\n");
//            while(s.hasNextLine())
//            {
//                String[] pair = s.nextLine().split(":");
//                saveInfo.put(pair[0], pair[1]);
//            }
//        } catch (Exception e)
//        {
//            System.out.println("Error reading player save: " + e.getCause() + e.getMessage());
//        }

        saveInfo.put("currentLevel", "110");

        return saveInfo;
    }

    // Reads an arbitrary map file (used for individual level details/properties)
    public static HashMap<String, String> readMap(String loc)
    {
        HashMap<String, String> saveInfo = new HashMap<>();
        try {
            Scanner s = new Scanner(new File(loc)).useDelimiter("\n");
            while(s.hasNextLine())
            {
                String[] pair = s.nextLine().split(":");
                saveInfo.put(pair[0], pair[1]);
            }
        } catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }

        return saveInfo;
    }
}
