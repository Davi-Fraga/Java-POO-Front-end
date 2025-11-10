package br.com.view;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

public class CupomFiscalPrinter implements Printable {

    private final String textoCupom;

    public CupomFiscalPrinter(String textoCupom) {
        this.textoCupom = textoCupom;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        Font font = new Font("Monospaced", Font.PLAIN, 10);
        g2d.setFont(font);

        String[] lines = textoCupom.split("\n");
        int y = 15;
        for (String line : lines) {
            g2d.drawString(line, 10, y);
            y += 15;
        }

        return PAGE_EXISTS;
    }
}
