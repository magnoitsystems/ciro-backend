package com.ciro.backend.service;

import com.ciro.backend.dto.BillResponseDTO;
import com.ciro.backend.dto.ReceiptResponseDTO;
import com.ciro.backend.entity.Bill;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;

@Service
public class PdfGenerationService {

    public byte[] generateReceiptPdf(ReceiptResponseDTO receipt) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, baos);
            document.open();

            try {
                URL imageUrl = getClass().getResource("/images/logo.png");
                if (imageUrl != null) {
                    Image logo = Image.getInstance(imageUrl);
                    logo.scaleToFit(150, 150);
                    logo.setAlignment(Element.ALIGN_CENTER);
                    document.add(logo);
                }
            } catch (Exception e) {
                System.out.println("No se pudo cargar el logo: " + e.getMessage());
            }

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
            Paragraph title = new Paragraph("RECIBO DE PAGO", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingBefore(20);
            title.setSpacingAfter(30);
            document.add(title);

            PdfPTable table = new PdfPTable(2); // 2 columnas
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            table.addCell(new Phrase("Fecha de emisión:", boldFont));
            table.addCell(new Phrase(receipt.getReceiptDate().toString(), normalFont));

            table.addCell(new Phrase("Monto Recibido:", boldFont));
            table.addCell(new Phrase("$" + receipt.getAmount() + " " + receipt.getCurrencyType(), normalFont));

            if (receipt.getConvertedAmount() != null && receipt.getExchangeRate() != null) {
                table.addCell(new Phrase("Cotización Aplicada:", boldFont));
                table.addCell(new Phrase("$" + receipt.getExchangeRate(), normalFont));

                table.addCell(new Phrase("Total Convertido a U$D:", boldFont));
                table.addCell(new Phrase("U$D " + receipt.getConvertedAmount(), normalFont));
            }

            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            for (int i = 0; i < table.getRows().size(); i++) {
                for (int j = 0; j < table.getRow(i).getCells().length; j++) {
                    if (table.getRow(i).getCells()[j] != null) {
                        table.getRow(i).getCells()[j].setBorder(Rectangle.NO_BORDER);
                        table.getRow(i).getCells()[j].setPaddingBottom(10);
                    }
                }
            }

            document.add(table);

            Paragraph footer = new Paragraph("Gracias por confiar en nosotros.", FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10));
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(50);
            document.add(footer);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF del recibo", e);
        }
    }

    public byte[] generateExpenseReportPdf(List<BillResponseDTO> bills, String reportTitle) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 40, 40, 40, 40);
            PdfWriter.getInstance(document, baos);
            document.open();

            try {
                URL imageUrl = getClass().getResource("/images/logo.png");
                if (imageUrl != null) {
                    Image logo = Image.getInstance(imageUrl);
                    logo.scaleToFit(120, 120);
                    logo.setAlignment(Element.ALIGN_CENTER);
                    document.add(logo);
                }
            } catch (Exception e) {
                System.out.println("No se pudo cargar el logo.");
            }

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph(reportTitle, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingBefore(15);
            title.setSpacingAfter(20);
            document.add(title);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.5f, 3f, 1.5f, 1.5f, 1.5f});

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            Font rowFont = FontFactory.getFont(FontFactory.HELVETICA, 9);

            table.addCell(new PdfPCell(new Phrase("Fecha", headFont)));
            table.addCell(new PdfPCell(new Phrase("Descripción / Entidad", headFont)));
            table.addCell(new PdfPCell(new Phrase("Tipo", headFont)));
            table.addCell(new PdfPCell(new Phrase("Origen", headFont)));
            table.addCell(new PdfPCell(new Phrase("Monto", headFont)));

            BigDecimal totalSueldos = BigDecimal.ZERO;
            BigDecimal totalServicios = BigDecimal.ZERO;
            BigDecimal totalCaja = BigDecimal.ZERO;
            BigDecimal totalDoctor = BigDecimal.ZERO;
            BigDecimal totalGeneral = BigDecimal.ZERO;

            for (BillResponseDTO b : bills) {
                table.addCell(new Phrase(b.getBillDate().toString(), rowFont));

                String desc = b.getDescription() != null ? b.getDescription() : "";
                String entity = b.getEntityName();
                table.addCell(new Phrase(desc + " (" + entity + ")", rowFont));

                table.addCell(new Phrase(b.getBillType().name(), rowFont));
                table.addCell(new Phrase(b.getFrom().name(), rowFont));
                table.addCell(new Phrase("$" + b.getAmount().toString(), rowFont));

                totalGeneral = totalGeneral.add(b.getAmount());
                if (b.getBillType() == com.ciro.backend.enums.BillType.SUELDO) totalSueldos = totalSueldos.add(b.getAmount());
                if (b.getBillType() == com.ciro.backend.enums.BillType.SERVICIO) totalServicios = totalServicios.add(b.getAmount());
                if (b.getFrom() == com.ciro.backend.enums.OriginType.CAJA) totalCaja = totalCaja.add(b.getAmount());
                if (b.getFrom() == com.ciro.backend.enums.OriginType.DOCTOR) totalDoctor = totalDoctor.add(b.getAmount());
            }
            document.add(table);

            document.add(new Paragraph(" ", rowFont));

            PdfPTable summaryTable = new PdfPTable(2);
            summaryTable.setWidthPercentage(50);
            summaryTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            summaryTable.addCell(new PdfPCell(new Phrase("TOTAL SUELDOS:", headFont)));
            summaryTable.addCell(new PdfPCell(new Phrase("$" + totalSueldos, rowFont)));

            summaryTable.addCell(new PdfPCell(new Phrase("TOTAL SERVICIOS:", headFont)));
            summaryTable.addCell(new PdfPCell(new Phrase("$" + totalServicios, rowFont)));

            summaryTable.addCell(new PdfPCell(new Phrase("PAGADO DE CAJA:", headFont)));
            summaryTable.addCell(new PdfPCell(new Phrase("$" + totalCaja, rowFont)));

            summaryTable.addCell(new PdfPCell(new Phrase("PAGADO POR DOCTOR:", headFont)));
            summaryTable.addCell(new PdfPCell(new Phrase("$" + totalDoctor, rowFont)));

            PdfPCell totalGeneralCell = new PdfPCell(new Phrase("TOTAL GENERAL:", headFont));
            totalGeneralCell.setBackgroundColor(new java.awt.Color(230, 230, 230)); // Gris clarito
            summaryTable.addCell(totalGeneralCell);

            PdfPCell totalAmountCell = new PdfPCell(new Phrase("$" + totalGeneral, headFont));
            totalAmountCell.setBackgroundColor(new java.awt.Color(230, 230, 230));
            summaryTable.addCell(totalAmountCell);

            document.add(summaryTable);
            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF del reporte", e);
        }
    }
}