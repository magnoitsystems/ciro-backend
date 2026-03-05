package com.ciro.backend.service;

import com.ciro.backend.dto.BillResponseDTO;
import com.ciro.backend.dto.ReceiptResponseDTO;
import com.ciro.backend.entity.Bill;
import com.ciro.backend.entity.CashMovement;
import com.ciro.backend.enums.CashMovementType;
import com.ciro.backend.enums.CurrencyType;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
            totalGeneralCell.setBackgroundColor(new java.awt.Color(230, 230, 230));
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

    public byte[] generateCashReportPdf(List<CashMovement> movements, String reportTitle) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4.rotate(), 30, 30, 30, 30);
            PdfWriter.getInstance(document, baos);
            document.open();

            try {
                URL imageUrl = getClass().getResource("/images/logo.png");
                if (imageUrl != null) {
                    Image logo = Image.getInstance(imageUrl);
                    logo.scaleToFit(100, 100);
                    logo.setAlignment(Element.ALIGN_CENTER);
                    document.add(logo);
                }
            } catch (Exception e) {}

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph(reportTitle, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.5f, 1f, 1.5f, 3f, 1f, 1.5f});

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            Font rowFont = FontFactory.getFont(FontFactory.HELVETICA, 9);

            String[] headers = {"Fecha", "Tipo", "Método", "Observaciones", "Moneda", "Monto"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headFont));
                cell.setBackgroundColor(new java.awt.Color(240, 240, 240));
                table.addCell(cell);
            }

            Map<CurrencyType, BigDecimal> ingresosPorMoneda = new HashMap<>();
            Map<CurrencyType, BigDecimal> egresosPorMoneda = new HashMap<>();
            Map<String, BigDecimal> totalPorMetodo = new TreeMap<>();

            for (CashMovement m : movements) {
                String fechaStr = (m.getMovementDate() != null) ? m.getMovementDate().toLocalDate().toString() : "S/F";
                table.addCell(new Phrase(fechaStr, rowFont));

                String tipoStr = (m.getType() != null) ? (m.getType() == CashMovementType.EGRESO ? "SALIDA" : "ENTRADA") : "S/T";
                table.addCell(new Phrase(tipoStr, rowFont));

                String metodoStr = (m.getPaymentMethod() != null) ? m.getPaymentMethod().name() : "NO ESPECIF.";
                table.addCell(new Phrase(metodoStr, rowFont));

                table.addCell(new Phrase(m.getObservations() != null ? m.getObservations() : "-", rowFont));

                String monedaStr = (m.getCurrencyType() != null) ? m.getCurrencyType().name() : "S/M";
                table.addCell(new Phrase(monedaStr, rowFont));

                BigDecimal amount = (m.getAmount() != null) ? m.getAmount() : BigDecimal.ZERO;
                String symbol = (m.getCurrencyType() == CurrencyType.DOLARES) ? "U$D " : "$ ";
                table.addCell(new Phrase(symbol + amount.toString(), rowFont));

                if (m.getCurrencyType() != null && m.getType() != null) {
                    CurrencyType curr = m.getCurrencyType();
                    if (m.getType() == CashMovementType.INGRESO) {
                        ingresosPorMoneda.put(curr, ingresosPorMoneda.getOrDefault(curr, BigDecimal.ZERO).add(amount));
                    } else {
                        egresosPorMoneda.put(curr, egresosPorMoneda.getOrDefault(curr, BigDecimal.ZERO).add(amount));
                    }

                    String metodoKey = curr.name() + " - " + metodoStr;
                    totalPorMetodo.put(metodoKey, totalPorMetodo.getOrDefault(metodoKey, BigDecimal.ZERO)
                            .add(m.getType() == CashMovementType.INGRESO ? amount : amount.negate()));
                }
            }

            document.add(table);
            document.add(new Paragraph("\n"));

            Paragraph resTitle = new Paragraph("RESUMEN DE SALDOS", headFont);
            resTitle.setSpacingAfter(10);
            document.add(resTitle);

            PdfPTable summaryTable = new PdfPTable(4);
            summaryTable.setWidthPercentage(100);
            summaryTable.addCell(new PdfPCell(new Phrase("Moneda", headFont)));
            summaryTable.addCell(new PdfPCell(new Phrase("Total Entradas (+)", headFont)));
            summaryTable.addCell(new PdfPCell(new Phrase("Total Salidas (-)", headFont)));
            summaryTable.addCell(new PdfPCell(new Phrase("SALDO FINAL", headFont)));

            for (CurrencyType ct : CurrencyType.values()) {
                BigDecimal in = ingresosPorMoneda.getOrDefault(ct, BigDecimal.ZERO);
                BigDecimal out = egresosPorMoneda.getOrDefault(ct, BigDecimal.ZERO);
                if (in.compareTo(BigDecimal.ZERO) == 0 && out.compareTo(BigDecimal.ZERO) == 0) continue;

                summaryTable.addCell(new Phrase(ct.name(), rowFont));
                summaryTable.addCell(new Phrase(in.toString(), rowFont));
                summaryTable.addCell(new Phrase(out.toString(), rowFont));

                PdfPCell totalCell = new PdfPCell(new Phrase(in.subtract(out).toString(), headFont));
                totalCell.setBackgroundColor(new java.awt.Color(235, 235, 235));
                summaryTable.addCell(totalCell);
            }
            document.add(summaryTable);

            document.add(new Paragraph("\nDESGLOSE POR MÉTODO DE PAGO:", rowFont));
            for (Map.Entry<String, BigDecimal> entry : totalPorMetodo.entrySet()) {
                document.add(new Paragraph("• " + entry.getKey() + ": " + entry.getValue(), rowFont));
            }

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al generar PDF de caja: " + e.getMessage(), e);
        }
    }

    public byte[] generateCurrentAccountPdf(com.ciro.backend.dto.CurrentAccountResponseDTO account) {
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
                System.out.println("No se pudo cargar el logo: " + e.getMessage());
            }

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("ESTADO DE CUENTA CORRIENTE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingBefore(15);
            title.setSpacingAfter(10);
            document.add(title);

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            Font rowFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
            Font highlightFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);

            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingAfter(20);

            infoTable.addCell(new Phrase("Paciente:", headFont));
            infoTable.addCell(new Phrase(account.getPatientFullName(), rowFont));

            infoTable.addCell(new Phrase("Deuda actual (Pesos):", headFont));
            PdfPCell pesosCell = new PdfPCell(new Phrase("$ " + account.getDebtInPesos(), highlightFont));
            pesosCell.setBackgroundColor(new java.awt.Color(255, 240, 240)); // Un rojito muy claro
            infoTable.addCell(pesosCell);

            infoTable.addCell(new Phrase("Deuda actual (Dólares):", headFont));
            PdfPCell usdCell = new PdfPCell(new Phrase("U$D " + account.getDebtInDollars(), highlightFont));
            usdCell.setBackgroundColor(new java.awt.Color(240, 255, 240)); // Un verdecito muy claro
            infoTable.addCell(usdCell);

            for (int i = 0; i < infoTable.getRows().size(); i++) {
                for (int j = 0; j < infoTable.getRow(i).getCells().length; j++) {
                    if (infoTable.getRow(i).getCells()[j] != null) {
                        infoTable.getRow(i).getCells()[j].setBorder(Rectangle.NO_BORDER);
                        infoTable.getRow(i).getCells()[j].setPaddingBottom(8);
                    }
                }
            }
            document.add(infoTable);

            Paragraph subtitle = new Paragraph("HISTORIAL DE MOVIMIENTOS", headFont);
            subtitle.setSpacingAfter(10);
            document.add(subtitle);

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.5f, 1.2f, 3.5f, 1f, 1.5f, 1.5f}); // Anchos de columnas

            String[] headers = {"Fecha", "Tipo", "Detalle", "Moneda", "Monto", "Saldo Resultante"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headFont));
                cell.setBackgroundColor(new java.awt.Color(230, 230, 230));
                cell.setPaddingBottom(5);
                table.addCell(cell);
            }

            if (account.getMovements() != null && !account.getMovements().isEmpty()) {
                for (com.ciro.backend.dto.CurrentAccountMovementDTO mov : account.getMovements()) {
                    String fechaStr = mov.getDate() != null ? mov.getDate().toString() : "S/F";
                    table.addCell(new Phrase(fechaStr, rowFont));

                    String tipoStr = mov.getType() != null ? mov.getType().name() : "";
                    table.addCell(new Phrase(tipoStr, rowFont));

                    String detalleStr = mov.getDetail() != null ? mov.getDetail() : "";
                    table.addCell(new Phrase(detalleStr, rowFont));

                    String monedaStr = mov.getCurrency() != null ? mov.getCurrency().name() : "";
                    table.addCell(new Phrase(monedaStr, rowFont));

                    BigDecimal monto = mov.getTransactionAmount() != null ? mov.getTransactionAmount() : BigDecimal.ZERO;
                    table.addCell(new Phrase(monto.toString(), rowFont));

                    BigDecimal saldo = mov.getBalance() != null ? mov.getBalance() : BigDecimal.ZERO;
                    table.addCell(new Phrase(saldo.toString(), rowFont));
                }
            } else {
                PdfPCell emptyCell = new PdfPCell(new Phrase("No se registraron movimientos.", rowFont));
                emptyCell.setColspan(6);
                emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                emptyCell.setPadding(10);
                table.addCell(emptyCell);
            }

            document.add(table);

            Paragraph footer = new Paragraph("Documento generado el " + java.time.LocalDate.now().toString(), FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8));
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(30);
            document.add(footer);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF de la cuenta corriente", e);
        }
    }
}