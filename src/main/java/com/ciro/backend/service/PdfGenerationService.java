package com.ciro.backend.service;

import com.ciro.backend.dto.ReceiptResponseDTO;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.net.URL;

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
}