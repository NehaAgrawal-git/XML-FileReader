/**
 * Sheet data class which contains the sheetName and Row Count.
 */
public class Sheet {
    String sheetName = "";

    int countOfRows = 0;

    @Override
    public String toString() {
        return sheetName + " " + countOfRows;
    }
}
