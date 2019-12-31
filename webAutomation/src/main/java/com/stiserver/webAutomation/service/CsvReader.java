package com.stiserver.webAutomation.service;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.util.*;

/**
 * STILL TESTING
 * Open most recently downloaded reports and edit them
 * Save edit report to another dir location
 * Set the instance variable of this modified file.
 */
public class CsvReader {

    public CsvReader(){}

    /**
     * USED TO READ CSV FILE
     * read .csv and allocate new location in memory.
     * @param f f
     * @return new instance of .csv
     * @throws IOException t
     */
    public List<String []>readCsv(FileReader f) throws IOException, CsvValidationException {
       CSVReader reader = new CSVReader(f);
       List<String[]> temp = new ArrayList<>();

       //READ .CSV
       String[] nextLine;
       while ((nextLine = reader.readNext()) != null) {
           //   System.out.println("-" +Arrays.toString(nextLine));
               temp.add(nextLine);
        }

       //CREATE NEW INSISTENCE IN MEMORY.
       List<String []> temp2 = new ArrayList<>();
        for (String[] strings : temp) {
            String[] s = new String[temp.get(0).length];
            System.arraycopy(strings, 0, s, 0, strings.length);
            temp2.add(s);
        }

        //REPLACE ALL NULL VALUES WITH ""
        List<String []> allRows = new ArrayList<>();

        for (String[] strings : temp2) {
            String[] t = new String[strings.length]; //temp location for removing null values
            for (int p = 0; p < strings.length; p++) {
                if (strings[p] == null) {
                    // System.out.println("fdfdfdfdf");
                    t[p] = "";
                } else {
                    t[p] = strings[p];
                }
            }
            allRows.add(t);
        }

   /*     for(String [] s: allRows)
        {
            System.out.println(Arrays.toString(s));
        }*/

        reader.close();
        return allRows;
    }

    /**
     * USED TO REMOVE COLUMN IN .CSV BY HEADER NAME
     * @return NEW INSTANCE
     */
    public List<String[]> removeColumn(List<String []> temp, String headerName){

        //MAKE LINKED HASH MAP
        LinkedHashMap<String, ArrayList<String>> map = makeLinkedHashMap(temp);

        //REMOVE HEADER
        map.remove(headerName);

        //PUT MAKE BACK TOGETHER AND RETURN
        return makeList(map);
    }

    /**
     * USED TO REMOVE COLUMN IN .CSV BY COLUMN NUMBER
     * @return NEW INSTANCE
     */ // -------------------------------testing
    public List<String[]> removeColumn(List<String []> temp, int position){
        //MAKE LINKED HASH MAP
        LinkedHashMap<String, ArrayList<String>> map = makeLinkedHashMap(temp);

        //TEMP LIST OF HEADER
        String [] s = temp.get(position);

        //REPLACE HEADER NAME WITH NEW NAME
        String[] newHeader = new String[temp.get(0).length];
        for(int i = 0; i < map.size(); i++){

        }

        //PUT MAKE BACK TOGETHER AND RETURN
        return makeList(map);
    }

    /**
     * CHANGE COLUMN NAME
     * @param temp t
     * @param removeName h
     * @param  newName n
     * @return r
     */
    public List<String[]> changeColumnName(List<String[]> temp, String removeName, String newName){

        //TEMP LIST OF HEADER
        String [] s = temp.get(0);

        //REPLACE HEADER NAME WITH NEW NAME
       String[] newHeader = new String[temp.get(0).length];
       for(int i = 0; i < s.length; i++){
           if(!s[i].equals(removeName)){
               newHeader[i] = s[i];}
           else{
               newHeader[i] = newName;}
       }

       //REMOVE OLD HEADER AND ADD NEW HEADER TO TEMP
        temp.set(0, newHeader);

       return temp;
    }

    /**
     * MAKE LIST OUT OF LINKED HASH MAP.
     * @param map m
     * @return list
     */
    private List<String[]> makeList(LinkedHashMap<String, ArrayList<String>> map){
        //SAMPLE PRINT.
      /*  for (Map.Entry<String, ArrayList<String>> entry : map.entrySet()) {
              System.out.println(entry.getKey() +"----" + entry.getValue() + entry.getKey().length());
             System.out.print("");
        }*/

        //LIST TO RETURN
        List<String []> allRows = new ArrayList<>();

        //PUT MAP BACK TOGETHER
        //MAIN LIST TO BE READ TO .CSV FILE.
        //GET SIZE OF VALUES IN MAP
        int sizeOfValues = 0;
        for (Map.Entry<String, ArrayList<String>> entery : map.entrySet()) {
            sizeOfValues = entery.getValue().size();
        }

        //PULL DATA FROM MAP
        for (int mapIndex = 0; mapIndex < sizeOfValues; mapIndex++) { //length of values in map X654

            int indexOfTemp = 0;
            String[] temp2 = new String[map.size()];

            for (Map.Entry<String, ArrayList<String>> entry : map.entrySet()) //X9
            {
                //System.out.println("--+" + indexOfTemp);
                //System.out.println("[" + mapIndex + "] " + entry.getValue().get(mapIndex));
                temp2[indexOfTemp] = entry.getValue().get(mapIndex) ;
                //      System.out.println("{" + mapIndex + "} " + temp[indexOfTemp] + "---");
                indexOfTemp++;
            }
            allRows.add(temp2);
        }
        return allRows;
    }

    /**
     * CRATE LINKED HASH MAP
     */
    private LinkedHashMap<String, ArrayList<String>> makeLinkedHashMap(List<String[]> temp){

        LinkedHashMap<String, ArrayList<String>> map = new LinkedHashMap<>();

        //GET HEADER AS KEYS
        String[] header;
        List<String> list = new ArrayList<>(Arrays.asList(temp.get(0)));
        header = list.toArray(new String[0]);

        //System.out.println("HEADER--"+ " Length " + header.length +"\n"+ Arrays.toString(header));//9x

        //READ HEADER AS KEY AND VALUES AS ROW.
        for (int head = 0; head < header.length; head++)//loops 9x
        {
            //Will be the list of row values per the column it is in.
            ArrayList<String> rowData = new ArrayList<>();

            //Loop to add row to header with lambda
            int finalHead = head;
            temp.forEach(l-> rowData.add(l[finalHead]));

            map.put(header[head], rowData); //add to map
        }

        //SAMPLE PRINT.
        /*for (Map.Entry<String, ArrayList<String>> entry : map.entrySet()) {
            System.out.println(entry.getKey() +"----" + entry.getValue() + entry.getKey().length());
            // System.out.print("");
        }*/
        return map;
    }

    private List<String[]> changeColumnValuesSensus(LinkedHashMap<String, ArrayList<String>> map, String header, String value) {
        //MAIN LIST TO BE READ TO .CSV FILE.
        List<String[]> allRows = new ArrayList<>();

        //GET HEADER KEYS FROM MAP.
        List<String> tempHeadersFromMap = new ArrayList<>(map.keySet());
        String[] headerForCsvWriter = new String[map.size()];

        allRows.add(headerForCsvWriter);

        for (int i = 0; i < headerForCsvWriter.length; i++) { //X9
           //    System.out.println("Header Key: " + tempHeadersFromMap.get(i));
            headerForCsvWriter[i] = tempHeadersFromMap.get(i);
        }

        //GET SIZE OF VALUES IN MAP
        int sizeOfValues = 0;
        for (Map.Entry<String, ArrayList<String>> entery : map.entrySet()) {
            sizeOfValues = entery.getValue().size();
        }

        //REPLACE VALUES IN COLUMN WITH NEW VALUES
        ArrayList<String> tempS = new ArrayList<>();
        for (int i = 0; i < sizeOfValues; i++) {
            tempS.add(value);
        }
        map.put(header, tempS);


        return allRows;
    }

}

