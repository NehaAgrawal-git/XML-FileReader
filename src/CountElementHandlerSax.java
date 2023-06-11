import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Handler which has the business logic to parse the xml.
 */
public class CountElementHandlerSax extends DefaultHandler {

    private Integer currentSheetRowCount = 0;
    private boolean encounteredData = false;
    private final List<Sheet> sheetList = new ArrayList<>();
    private Sheet sheet = new Sheet();
    private String projectName = "";
    private String spId = "";
    private int projectNameRow = 2;

    private int currentCellCount = 0;

    public String getProjectName() {
        return projectName;
    }

    public String getSpId() {
        return spId;
    }

    public List<Sheet> getSheetList() {
        return sheetList;
    }

    public void setProjectNameRow(int rowNumber) {
        projectNameRow = rowNumber;
    }

    @Override
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes) {
        switch (qName) {
            case "Worksheet" -> {
                sheet = new Sheet();
                sheet.sheetName = attributes.getValue(attributes.getIndex("ss:Name"));
            }
            case "Row" -> {
                currentCellCount = 0;
                currentSheetRowCount++;
                sheet.countOfRows = currentSheetRowCount;
            }
            case "Data" -> {
                currentCellCount++;
                encounteredData = true;
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (currentSheetRowCount == projectNameRow && sheetList.size() == 0 && encounteredData) {
            String value = String.copyValueOf(ch, start, length).trim();
            if (currentCellCount == 2 && projectName.isEmpty()) {
                projectName = value;
            }
            if (currentCellCount == 3 && spId.isEmpty()) {
                spId = value;
            }
            encounteredData = false;
        }
    }

    @Override
    public void endElement(String uri, String localName,
                           String qName) {
        if (qName.equals("Worksheet")) {
            sheetList.add(sheet);
            sheet = new Sheet();
            currentSheetRowCount = 0;
        }
    }
}