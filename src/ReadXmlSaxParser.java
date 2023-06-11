import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * The main function which needs to be run with the directory name set is present in this class.
 */
public class ReadXmlSaxParser {

    public static void main(String[] args) {
        System.out.println("Please enter the directory where you want to search : ");
        Scanner sc = new Scanner(System.in);
        String directory = sc.nextLine();
        if (!directory.isEmpty()) {
            try {
                listFilesForFolder(new File(directory));
            } catch (NullPointerException e) {
                System.out.println("Entered directory is not valid. Please enter a valid directory path");
            }
        } else {
            System.out.println("Empty directory path not allowed !!");
        }
    }


    /**
     * Function which reads the xml file and lists the project name along with the count of each row.
     * @param file The XML File
     */
    public static void countAndListInXML(File file) {
        System.out.println("Starting to read file " + file.getAbsolutePath());
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {

            SAXParser saxParser = factory.newSAXParser();

            // count elements name known as "staff"
            CountElementHandlerSax countStaffHandler =
                    new CountElementHandlerSax();
            DatabaseHelper db = new DatabaseHelper();
            Boolean conn = db.openConnection();
            if (!conn) {
                System.out.println("Db issue occurred. Data is not getting stored");
            }

            // To set the which Row's 1st cell contains the project Name.
            countStaffHandler.setProjectNameRow(2);
            saxParser.parse(file.getPath(), countStaffHandler);

            System.out.println("The Project name : " + countStaffHandler.getProjectName());
            System.out.println("The spId : " + countStaffHandler.getSpId());
            System.out.println();
            List<Sheet> sheetList = countStaffHandler.getSheetList();
            int size = sheetList.size();
            ArrayList<Integer> listCount = new ArrayList<>();
            for (int i = 1; i < size; i++) {
                System.out.println("The sheet name is : " + sheetList.get(i).sheetName);
                System.out.println("The count of rows in this sheet : " + (sheetList.get(i).countOfRows - 1));
                listCount.add((sheetList.get(i).countOfRows - 1));
                if (i != size - 1) {
                    System.out.println("--------------------------------------------------");
                }
            }
            db.insertData(countStaffHandler.getProjectName(), countStaffHandler.getSpId(), listCount);
            db.getData();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println("Error while reading the file. Please check the file.");
            e.printStackTrace();
        }
        System.out.println("================================================");
    }

    /**
     * To go through the list of all files present in the folder
     *
     * @param folder The Directory
     */
    public static void listFilesForFolder(final File folder) {
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else if (isFileXml(fileEntry)) {
                countAndListInXML(fileEntry);
            }
        }
    }

    /**
     * To check if the given file is a xml or not.
     *
     * @param file The file
     * @return true if xml else false
     */
    public static boolean isFileXml(File file) {
        return file.getAbsolutePath().endsWith(".xml");
    }
}

