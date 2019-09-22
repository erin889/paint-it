import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Canvas extends JPanel implements View, ClipboardOwner {

    private int startX, startY;
    private Model model;

    BufferedImage bf = new BufferedImage(1200, 900, BufferedImage.TYPE_INT_RGB);
    final BasicStroke dashed = new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);

    public void repaintView() {
        if (model.isCopy == true) toClipboard();
        canvas.repaint();
    }

    public void toClipboard() {
        Graphics2D g = bf.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 1200, 900);
        drawGraphics(g);
        TransferableImage img = new TransferableImage(bf);
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        cb.setContents(img, this);
    }

    public void lostOwnership(Clipboard clip, Transferable tr) {}

    private	JPanel canvas = new JPanel(){
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            drawGraphics(g2);
        }
    };

    private void drawGraphics(Graphics2D g2) {
        ArrayList<Shape2D> shapeList = model.getShapes();
        for (Shape2D shape: shapeList) {

            if (shape.shapeColor != null) {
                g2.setColor(shape.shapeColor);
                g2.fill(shape.shape);
            }

            g2.setStroke(new BasicStroke(shape.shapeStroke));
            if(shape == model.getSelected())  g2.setStroke(dashed);
            g2.setColor(shape.shapeBorderColor);
            g2.draw(shape.shape);
            g2.setStroke(new BasicStroke(model.getCurStroke()));
        }

        if(model.getDrawing() != null) {
            g2.setColor(model.getCurColor());
            g2.setStroke(new BasicStroke(model.getCurStroke()));
            g2.draw(model.getDrawing().shape);
        }
    }

    Canvas (Model model) {
        this.model = model;
        this.setLayout(new CardLayout());

        JPanel container = new JPanel(new BorderLayout());
        JPanel vContainer = new JPanel();
        JPanel hContainer = new JPanel();

        container.setPreferredSize(new Dimension(1200,900));
        container.setMaximumSize(new Dimension(1200,900));
        container.setMinimumSize(new Dimension(1200,900));
        container.add(canvas, BorderLayout.CENTER);

        vContainer.setBackground(Color.LIGHT_GRAY);
        vContainer.setLayout(new BoxLayout(vContainer, BoxLayout.Y_AXIS));
        vContainer.add(container);
        vContainer.add(Box.createVerticalGlue());

        hContainer.setBackground(Color.LIGHT_GRAY);
        hContainer.setLayout(new BoxLayout(hContainer, BoxLayout.X_AXIS));
        hContainer.add(vContainer);
        hContainer.add(Box.createHorizontalGlue());

        JScrollPane scroller = new JScrollPane(hContainer);
        scroller.setSize(new Dimension(650,500));
        this.add(scroller);

        canvas.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if(model.getCurTool() == Tool.LINE
                        || model.getCurTool() == Tool.RECT
                        || model.getCurTool() == Tool.CIRCLE) {
                    model.draw(startX, startY, x, y);
                }
                else if (model.getCurTool() == Tool.SELECT){
                    model.move(x, y);
                }
            }
        });

        canvas.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                startX = e.getX();
                startY = e.getY();

                if (model.getCurTool() == Tool.SELECT){
                    model.select(e.getX(),e.getY());
                } else if(model.getCurTool() == Tool.LINE
                        || model.getCurTool() == Tool.RECT
                        || model.getCurTool() == Tool.CIRCLE) {
                    model.draw(0,0,0,0);
                } else if (model.getCurTool() == Tool.ERASER){
                    model.eraser(e.getX(),e.getY());
                } else if (model.getCurTool() == Tool.FILL){
                    model.fill(e.getX(),e.getY());
                }
            }
            public void mouseReleased(MouseEvent e) {
                if(model.getCurTool() == Tool.LINE
                        || model.getCurTool() == Tool.RECT
                        || model.getCurTool() == Tool.CIRCLE) {
                    model.add();
                }
            }
        });
    }
}
