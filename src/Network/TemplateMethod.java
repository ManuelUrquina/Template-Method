package Network;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVParser;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

abstract class DataProcessor {

    protected abstract Iterable<?> readData(String filePath) throws IOException;

    protected abstract void parseData(Iterable<?> data);

    protected abstract void processParsedData();

    protected abstract void saveData();

    public void processData(String filePath) {
        try {
            Iterable<?> data = readData(filePath);
            parseData(data);
            processParsedData();
            saveData();
        } catch (IOException e) {
            System.err.println("Error reading data from file: " + filePath);
        }
    }
}

class CsvDataProcessor extends DataProcessor {

    private List<CSVRecord> records;

    @Override
    protected Iterable<CSVRecord> readData(String filePath) throws IOException {
        System.out.println("Reading CSV data from file: " + filePath);
        return CSVParser.parse(new FileReader(filePath), CSVFormat.DEFAULT).getRecords();
    }

    @Override
    protected void parseData(Iterable<?> data) {
        System.out.println("Parsing CSV data");
        records = new ArrayList<>();
        for (Object record : data) {
            records.add((CSVRecord) record);
        }
    }

    @Override
    protected void processParsedData() {
        System.out.println("Processing CSV data");
        // Specific operation for CSV data: Print each record
        for (CSVRecord record : records) {
            System.out.println("CSV Record: " + record.toString());
        }
    }

    @Override
    protected void saveData() {
        System.out.println("Saving processed data to CSV file");
        // Specific operation for CSV data: Save the processed data to a new CSV file
        try (FileWriter writer = new FileWriter("output.csv");
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            for (CSVRecord record : records) {
                csvPrinter.printRecord(record);
            }
        } catch (IOException e) {
            System.err.println("Error saving CSV data: " + e.getMessage());
        }
    }
}


class XmlDataProcessor extends DataProcessor {

    private Element rootElement;

    @Override
    protected Iterable<Element> readData(String filePath) throws IOException {
        System.out.println("Reading XML data from file: " + filePath);
        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            rootElement = saxBuilder.build(filePath).getRootElement();
        } catch (JDOMException e) {
            System.err.println("Error parsing XML data: " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void parseData(Iterable<?> data) {
        System.out.println("Parsing XML data");
        // Specific operation for XML data: Print the root element
        XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
        System.out.println("XML Data:\n" + xmlOutput.outputString(rootElement));
    }

    @Override
    protected void processParsedData() {
        System.out.println("Processing XML data");
        // Specific operation for XML data: Implement your XML data processing logic here
    }

    @Override
    protected void saveData() {
        System.out.println("Saving processed data to XML file");
        // Specific operation for XML data: Save the processed XML data to a new XML file
        try (FileWriter writer = new FileWriter("output.xml")) {
            XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
            xmlOutput.output(rootElement, writer);
        } catch (IOException e) {
            System.err.println("Error saving XML data: " + e.getMessage());
        }
    }
}

public class TemplateMethod {

    public static void main(String[] args) {
        DataProcessor csvProcessor = new CsvDataProcessor();
        DataProcessor xmlProcessor = new XmlDataProcessor();

        System.out.println("Processing CSV data:");
        csvProcessor.processData("C:\\Users\\M4nu3h\\Documents\\NetBeansProjects\\Template Method\\CSV\\tblAprendicescon - tblAprendicescon.csv.csv");

        System.out.println("\nProcessing XML data:");
        xmlProcessor.processData("C:\\Users\\M4nu3h\\Documents\\NetBeansProjects\\Template Method\\XML\\code_editor.xml");
    }
}