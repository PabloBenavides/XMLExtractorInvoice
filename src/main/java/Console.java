/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.poi.hpsf.HPSFException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
//import org.w3c.dom.Node;

/**
 *
 * @author Grucas
 */
public class Console {

    static File file = new File("C:\\data\\Dropbox\\Documents\\Pablo Benavides Molina\\Facturas\\2017\\Recibidos\\Febrero");
    static ArrayList<Factura> facturas = new ArrayList<Factura>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws HPSFException {
        if (file != null) {
            FilenameFilter filter = new FilenameFilter() {
                public boolean accept(File directory, String fileName) {
                    if(fileName.endsWith(".xml") || fileName.endsWith(".XML")){
                        return true;
                    }else{
                        return false;
                    }
                }
            };
            File[] fileArraY = file.listFiles(filter);

            for (int i = 0; i < fileArraY.length; i++) {
                File xmlTemp = fileArraY[i];
                System.out.println(i + "----------------------------------- ARCHIVO : " + xmlTemp.getAbsolutePath());
                try {

                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(xmlTemp);
                    doc.getDocumentElement().normalize();

                    NamedNodeMap nodeMapComprobante = doc.getElementsByTagName("cfdi:Comprobante").item(0).getAttributes();
                    NamedNodeMap nodeMapEmisor = doc.getElementsByTagName("cfdi:Emisor").item(0).getAttributes();
                    NamedNodeMap nodeMapImpuestos = doc.getElementsByTagName("cfdi:Impuestos").item(0).getAttributes();
                    NamedNodeMap nodeMapFolioFiscal = doc.getElementsByTagName("tfd:TimbreFiscalDigital").item(0).getAttributes();
                    
                    Factura f = new Factura();
                    f.setFile(xmlTemp.getAbsolutePath());
                    f.setFecha_emision(nodeMapComprobante.getNamedItem("fecha").getTextContent());
                    f.setImporte(new Double(nodeMapComprobante.getNamedItem("subTotal").getTextContent()));
                    f.setSubtotal(new Double(nodeMapComprobante.getNamedItem("subTotal").getTextContent()));
                    f.setTotal(new Double(nodeMapComprobante.getNamedItem("total").getTextContent()));
                    f.setRfc(nodeMapEmisor.getNamedItem("rfc").getTextContent());
                    f.setRazon_social(nodeMapEmisor.getNamedItem("nombre").getTextContent());
                    if (nodeMapImpuestos.getNamedItem("totalImpuestosTrasladados") != null) {
                        f.setIva(new Double(nodeMapImpuestos.getNamedItem("totalImpuestosTrasladados").getTextContent()));
                    }else{
                        f.setIva(new Double(0.0));
                    }
                    f.setFolio_fisca(nodeMapFolioFiscal.getNamedItem("UUID").getTextContent());
                    facturas.add(f);
                    
//                    System.out.println("FECHA= " + nodeMapComprobante.getNamedItem("fecha").getTextContent());
//                    System.out.println("Importe= " + nodeMapComprobante.getNamedItem("subTotal").getTextContent());
//                    System.out.println("subtotal= " + nodeMapComprobante.getNamedItem("subTotal").getTextContent());
//                    System.out.println("total= " + nodeMapComprobante.getNamedItem("total").getTextContent());
//                    System.out.println("RFC= " + nodeMapEmisor.getNamedItem("rfc").getTextContent());
//                    System.out.println("NOMBRE= " + nodeMapEmisor.getNamedItem("nombre").getTextContent());                    
//                    if (nodeMapImpuestos.getNamedItem("totalImpuestosTrasladados") != null) {
//                        System.out.println("Impuestos IVA = " + nodeMapImpuestos.getNamedItem("totalImpuestosTrasladados").getTextContent());
//                    } else {
//                        System.out.println("Impuestos IVA = 0.0");
//                    }
//                    System.out.println("UUID = " + nodeMapFolioFiscal.getNamedItem("UUID").getTextContent());
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            ArrayList headers = new ArrayList();
//            headers.add("File"); 
           headers.add("Rfc");
            headers.add("Razon_social");
            headers.add("Fecha_emision");
            headers.add("Importe");
            headers.add("Impuestos");
            headers.add("Subtotal");
            headers.add("Total");

            
            Collections.sort(facturas, Factura.Comparators.RFC);
            ArrayList data = new ArrayList();
            for (int j = 0; j < facturas.size(); j++) {
                ArrayList cells = new ArrayList();
//                cells.add(facturas.get(j).getFile() + "");
                cells.add(facturas.get(j).getRfc() + "");
                cells.add(facturas.get(j).getRazon_social() + "");
                cells.add(facturas.get(j).getFecha_emision() + "");
                cells.add(facturas.get(j).getImporte());
                cells.add(facturas.get(j).getIva());
                cells.add(facturas.get(j).getSubtotal());
                cells.add(facturas.get(j).getTotal());
                data.add(cells);
            }
            
            exportToExcel("Resumen", headers, data, new File(file.getName() + "_resume.xls"));
        }
    }
    
    static public void exportToExcel(String sheetName, ArrayList headers,
            ArrayList data, File outputFile) throws HPSFException {

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(sheetName);
        
        int rowIdx = 0;
        short cellIdx = 0;
        
        // Header
        HSSFRow hssfHeader = sheet.createRow(rowIdx);
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        for (Iterator cells = headers.iterator(); cells.hasNext();) {
            HSSFCell hssfCell = hssfHeader.createCell(cellIdx++);
            hssfCell.setCellStyle(cellStyle);
            hssfCell.setCellValue((String) cells.next());
        }
        // Data
        rowIdx = 1;
        for (Iterator rows = data.iterator(); rows.hasNext();) {
            ArrayList row = (ArrayList) rows.next();
            HSSFRow hssfRow = sheet.createRow(rowIdx++);
            cellIdx = 0;
            for (Iterator cells = row.iterator(); cells.hasNext();) {
                HSSFCell hssfCell = hssfRow.createCell(cellIdx++);
                Object o = cells.next();
                if("class java.lang.Double".equals(o.getClass().toString())){
                    hssfCell.setCellValue((Double) o);
                }else{
                    hssfCell.setCellValue((String) o);
                }
                
            }
        }
        
        wb.setSheetName(0,sheetName);
        try {
            FileOutputStream outs = new FileOutputStream(outputFile);
            wb.write(outs);
            outs.close();
//            System.out.println("Archivo creado correctamente en " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            throw new HPSFException(e.getMessage());
        }
    }

}
