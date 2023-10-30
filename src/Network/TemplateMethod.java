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
            System.err.println("Error al leer los datos del archivo: " + filePath);
        }
    }
}

class CsvDataProcessor extends DataProcessor {

    private List<CSVRecord> records;

    @Override
    protected Iterable<CSVRecord> readData(String filePath) throws IOException {
        System.out.println("Leyendo datos CSV desde el archivo: " + filePath);
        return CSVParser.parse(new FileReader(filePath), CSVFormat.DEFAULT).getRecords();
    }

    @Override
    protected void parseData(Iterable<?> data) {
        System.out.println("Analizando datos CSV");
        records = new ArrayList<>();
        for (Object record : data) {
            records.add((CSVRecord) record);
        }
    }

    @Override
    protected void processParsedData() {
        System.out.println("Procesando datos CSV");
        // Operación específica para datos CSV: Imprimir cada registro
        for (CSVRecord record : records) {
            System.out.println("Registro CSV: " + record.toString());
        }
    }

    @Override
    protected void saveData() {
        System.out.println("Guardando los datos procesados en un archivo CSV");
        // Operación específica para datos CSV: Guardar los datos procesados en un nuevo archivo CSV
        try (FileWriter writer = new FileWriter("output.csv");
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            for (CSVRecord record : records) {
                csvPrinter.printRecord(record);
            }
        } catch (IOException e) {
            System.err.println("Error al guardar los datos CSV: " + e.getMessage());
        }
    }
}

class XmlDataProcessor extends DataProcessor {

    private Element rootElement;

    @Override
    protected Iterable<Element> readData(String filePath) throws IOException {
        System.out.println("Leyendo datos XML desde el archivo: " + filePath);
        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            rootElement = saxBuilder.build(filePath).getRootElement();
        } catch (JDOMException e) {
            System.err.println("Error al analizar los datos XML: " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void parseData(Iterable<?> data) {
        System.out.println("Analizando datos XML");
        // Operación específica para datos XML: Imprimir el elemento raíz
        XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
        System.out.println("Datos XML:\n" + xmlOutput.outputString(rootElement));
    }

    @Override
    protected void processParsedData() {
        System.out.println("Procesando datos XML");
        // Operación específica para datos XML: Implemente su lógica de procesamiento de datos XML aquí
    }

    @Override
    protected void saveData() {
        System.out.println("Guardando los datos procesados en un archivo XML");
        // Operación específica para datos XML: Guardar los datos XML procesados en un nuevo archivo XML
        try (FileWriter writer = new FileWriter("output.xml")) {
            XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
            xmlOutput.output(rootElement, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar los datos XML: " + e.getMessage());
        }
    }
}

public class TemplateMethod {

    public static void main(String[] args) {
        DataProcessor csvProcessor = new CsvDataProcessor();
        DataProcessor xmlProcessor = new XmlDataProcessor();

        System.out.println("Procesando datos CSV:");
        csvProcessor.processData("C:\\Users\\M4nu3h\\Documents\\NetBeansProjects\\Template Method\\CSV\\tblAprendicescon - tblAprendicescon.csv.csv");

        System.out.println("\nProcesando datos XML:");
        xmlProcessor.processData("C:\\Users\\M4nu3h\\Documents\\NetBeansProjects\\Template Method\\XML\\code_editor.xml");
    }
}