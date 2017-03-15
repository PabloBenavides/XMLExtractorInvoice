/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Comparator;

/**
 *
 * @author Grucas
 */
public class Factura implements Comparable<Factura>{
    String rfc;
    String razon_social;
    String file;
    String serie_factura;
    String folio_fisca;
    String fecha_emision;
    Double importe;
    Double subtotal;
    Double iva;
    Double total;

    public Factura() {
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getSerie_factura() {
        return serie_factura;
    }

    public void setSerie_factura(String serie_factura) {
        this.serie_factura = serie_factura;
    }

    public String getFolio_fisca() {
        return folio_fisca;
    }

    public void setFolio_fisca(String folio_fisca) {
        this.folio_fisca = folio_fisca;
    }

    public String getFecha_emision() {
        return fecha_emision;
    }

    public void setFecha_emision(String fecha_emision) {
        this.fecha_emision = fecha_emision;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getRazon_social() {
        return razon_social;
    }

    public void setRazon_social(String razon_social) {
        this.razon_social = razon_social;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public int compareTo(Factura o) {
        return Comparators.RFC.compare(this, o);
    }
    
    public static class Comparators {
        public static Comparator<Factura> RFC = new Comparator<Factura>() {
            @Override
            public int compare(Factura o1, Factura o2) {
                return o1.rfc.compareTo(o2.rfc);
            }
        };
    }
    
}
