package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;


public class CustomTable extends JScrollPane {

    private DefaultTableModel tableModel;
    private JTable table;

    private static final Color TABLE_HEADER_BG = Color.WHITE;
    private static final Color TABLE_BG = Color.WHITE;
    private static final Color PAGE_BG = new Color(238, 238, 238);
    private static final Color HEADER_FONT_COLOR = new Color(30, 30, 30);
    private static final Color CELL_FONT_COLOR = new Color(50, 50, 50);
    private static final Color BORDER_COLOR = new Color(220, 220, 220);
    private static final Color[] HEADER_LINE_COLORS = {
        new Color(0, 122, 255), 
        new Color(52, 199, 89), 
        new Color(255, 149, 0), 
        new Color(88, 86, 214), 
        new Color(175, 82, 222), 
        new Color(255, 45, 85)  
    };


    public CustomTable(String[] columnNames, int[] columnWidths) {
        super();


        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };


        table = new JTable(tableModel);


        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(40); 
        table.setBackground(TABLE_BG);
        table.setForeground(CELL_FONT_COLOR);
        table.setGridColor(BORDER_COLOR);
        table.setShowGrid(false); 
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);


        table.getTableHeader().setDefaultRenderer(new ModernHeaderRenderer());
        table.setDefaultRenderer(Object.class, new ModernCellRenderer());


        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        TableColumnModel columnModel = table.getColumnModel();
        int totalWidth = 0;
        for (int i = 0; i < columnWidths.length; i++) {
            columnModel.getColumn(i).setPreferredWidth(columnWidths[i]);
            totalWidth += columnWidths[i];
        }
        

        this.setViewportView(table);
        this.getViewport().setBackground(TABLE_BG); 
        this.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        

        this.setPreferredSize(new Dimension(totalWidth + 2, 400)); 
    }

    public DefaultTableModel getModel() {
        return this.tableModel;
    }


    private static class DottedBottomBorder extends AbstractBorder {
        private final Stroke stroke;
        private final Color color;

        public DottedBottomBorder(Color color) {
            this.color = color;
            this.stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[]{1, 2}, 0);
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(color);
            g2.setStroke(stroke);
            g2.drawLine(x, y + height - 1, x + width, y + height - 1);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(0, 0, 1, 0);
        }
    }


    private static class ModernHeaderRenderer extends DefaultTableCellRenderer {
        public ModernHeaderRenderer() {
            setOpaque(true);
            setHorizontalAlignment(SwingConstants.CENTER);
            setBackground(TABLE_HEADER_BG);
            setForeground(HEADER_FONT_COLOR);
            setFont(new Font("Arial", Font.BOLD, 14));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                      boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            Color lineColor = HEADER_LINE_COLORS[column % HEADER_LINE_COLORS.length];
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createMatteBorder(0, 0, 3, 0, lineColor)
            ));
            return this;
        }
    }

    private static class ModernCellRenderer extends DefaultTableCellRenderer {
        private final Border dottedBorder = new DottedBottomBorder(BORDER_COLOR);

        public ModernCellRenderer() {
            super();
            setOpaque(true);
            setHorizontalAlignment(SwingConstants.LEFT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                      boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            c.setBackground(TABLE_BG);
            c.setFont(new Font("Arial", Font.PLAIN, 14));
            setForeground(CELL_FONT_COLOR);

            setBorder(BorderFactory.createCompoundBorder(
                dottedBorder,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            
            if (isSelected) {
                c.setBackground(new Color(245, 245, 245)); 
                c.setForeground(CELL_FONT_COLOR);
            }

            return c;
        }
    }
}