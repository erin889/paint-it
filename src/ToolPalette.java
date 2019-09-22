import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.util.ArrayList;

public class ToolPalette extends JPanel implements View{

    private Model model;
    private ColorPicker colorPicker;
    private JToggleButton c1, c2, c3, c4, c5, c6;
    private JToggleButton l1, l2, l3, l4;
    private ArrayList<JToggleButton> colorButtons = new ArrayList<>();
    private ArrayList<JToggleButton> strokeButtons = new ArrayList<>();


    public void repaintView() {
        for (JToggleButton cbt: colorButtons) {
            if (cbt.getBackground() == model.getCurColor()) {
                cbt.setSelected(true);
                cbt.setBorder(new LineBorder(Color.BLACK, 3));
            } else {
                cbt.setSelected(false);
                cbt.setBorder(new LineBorder(Color.LIGHT_GRAY));
            }
        }

        for (JToggleButton lbt: strokeButtons) {
            if (lbt.getText().equals(Integer.toString(model.getCurStroke()))) {
                lbt.setSelected(true);
                lbt.setBorder(new LineBorder(Color.BLACK, 3));
            } else {
                lbt.setSelected(false);
                lbt.setBorder(new LineBorder(Color.LIGHT_GRAY));
            }
        }
    }

    private JToggleButton createToolButton(Boolean focus, Tool t, Icon ic, Icon ics) {
        JToggleButton bt = new JToggleButton(ic, focus);
        bt.setSelectedIcon(ics);
        bt.setBorder(new LineBorder(Color.LIGHT_GRAY));
        bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.deselectShape();
                model.setCurTool(t);
            }
        });
        return bt;
    }

    private JToggleButton createLineButton(Boolean focus, int size, Icon ic) {
        JToggleButton bt = new JToggleButton(ic, focus);
        bt.setText(Integer.toString(size));
        bt.setBorder(new LineBorder(Color.LIGHT_GRAY));
        bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setCurStroke(size);
                if(model.getCurTool() == Tool.SELECT)  model.setSelectedStroke(size);
            }
        });
        return bt;
    }

    private JToggleButton createColorButton(Boolean focus, Color c) {
        JToggleButton bt = new JToggleButton("", focus);
        bt.setBackground(c);
        bt.setBorderPainted(false);
        bt.setOpaque(true);
        bt.setBorderPainted(true);
        bt.setBorder(new LineBorder(Color.LIGHT_GRAY));
        bt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.setCurrentColor(c);
                if(model.getCurTool() == Tool.SELECT)  model.setSelectedShapeColor(c);
            }
        });
        return bt;
    }


    ToolPalette(Model model) {
        this.model = model;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(160, 100));

        JToolBar toolPalette = new JToolBar(JToolBar.VERTICAL);
        JToolBar colorPalette = new JToolBar(JToolBar.VERTICAL);
        JToolBar linePalette = new JToolBar(JToolBar.VERTICAL);
        linePalette.setPreferredSize(new Dimension(100, 170));
        linePalette.setMinimumSize(new Dimension(100, 170));

        this.add(toolPalette);
        this.add(colorPalette);
        this.add(linePalette);

        ButtonGroup tbts = new ButtonGroup();
        ButtonGroup lbts = new ButtonGroup();
        ButtonGroup cbts = new ButtonGroup();

        JPanel allTools = new JPanel(new GridLayout(3,2,4,4));
        allTools.setPreferredSize(new Dimension(100, 150));

        JPanel allColors = new JPanel(new GridLayout(3,2,4,4));
        allColors.setPreferredSize(new Dimension(100, 150));

        JPanel allLines = new JPanel(new GridLayout(4,1,4,4));
        allLines.setPreferredSize(new Dimension(100, 150));


        try {
            JToggleButton select = createToolButton(true, Tool.SELECT,
                    new ImageIcon(ImageIO.read(new FileInputStream("resources/select.png"))),
                    new ImageIcon(ImageIO.read(new FileInputStream("resources/select_s.png"))));
            JToggleButton eraser = createToolButton(false, Tool.ERASER,
                    new ImageIcon(ImageIO.read(new FileInputStream("resources/eraser.png"))),
                    new ImageIcon(ImageIO.read(new FileInputStream("resources/eraser_s.png"))));
            JToggleButton line = createToolButton(false, Tool.LINE,
                    new ImageIcon(ImageIO.read(new FileInputStream("resources/line.png"))),
                    new ImageIcon(ImageIO.read(new FileInputStream("resources/line_s.png"))));
            JToggleButton circle = createToolButton(false, Tool.CIRCLE,
                    new ImageIcon(ImageIO.read(new FileInputStream("resources/circle.png"))),
                    new ImageIcon(ImageIO.read(new FileInputStream("resources/circle_s.png"))));
            JToggleButton rect = createToolButton(false, Tool.RECT,
                    new ImageIcon(ImageIO.read(new FileInputStream("resources/rectangle.png"))),
                    new ImageIcon(ImageIO.read(new FileInputStream("resources/rectangle_s.png"))));
            JToggleButton fill = createToolButton(false, Tool.FILL,
                    new ImageIcon(ImageIO.read(new FileInputStream("resources/fill.png"))),
                    new ImageIcon(ImageIO.read(new FileInputStream("resources/fill_s.png"))));
            tbts.add(select);
            tbts.add(eraser);
            tbts.add(line);
            tbts.add(circle);
            tbts.add(rect);
            tbts.add(fill);

            allTools.add(select);
            allTools.add(eraser);
            allTools.add(line);
            allTools.add(circle);
            allTools.add(rect);
            allTools.add(fill);
            toolPalette.add(allTools);
        } catch (Exception e) {
            e.printStackTrace();
        }

        c1 = createColorButton(false, Color.GREEN);
        c2 = createColorButton(false,Color.CYAN);
        c3 = createColorButton(false,Color.YELLOW);
        c4 = createColorButton(true,Color.BLUE);
        c5 = createColorButton(false,Color.ORANGE);
        c6 = createColorButton(false,Color.PINK);
        cbts.add(c1);
        cbts.add(c2);
        cbts.add(c3);
        cbts.add(c4);
        cbts.add(c5);
        cbts.add(c6);
        allColors.add(c1);
        allColors.add(c2);
        allColors.add(c3);
        allColors.add(c4);
        allColors.add(c5);
        allColors.add(c6);
        colorButtons.add(c1);
        colorButtons.add(c2);
        colorButtons.add(c3);
        colorButtons.add(c4);
        colorButtons.add(c5);
        colorButtons.add(c6);
        colorPalette.add(allColors);

        JButton pickColorButton = new JButton("Custom...");
        pickColorButton.setSize(100, 40);
        pickColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                colorPicker = new ColorPicker(model);
            }
        });

        pickColorButton.setAlignmentX(0.5f);
        colorPalette.add(pickColorButton);

        try {
            l1 = createLineButton(true,1,
                    new ImageIcon(ImageIO.read(new FileInputStream("resources/line/line1.png"))));
            l2 = createLineButton(false,2,
                    new ImageIcon(ImageIO.read(new FileInputStream("resources/line/line2.png"))));
            l3 = createLineButton(false,6,
                    new ImageIcon(ImageIO.read(new FileInputStream("resources/line/line6.png"))));
            l4 = createLineButton(false,12,
                    new ImageIcon(ImageIO.read(new FileInputStream("resources/line/line12.png"))));

            lbts.add(l1);
            lbts.add(l2);
            lbts.add(l3);
            lbts.add(l4);
            allLines.add(l1);
            allLines.add(l2);
            allLines.add(l3);
            allLines.add(l4);
            strokeButtons.add(l1);
            strokeButtons.add(l2);
            strokeButtons.add(l3);
            strokeButtons.add(l4);
            linePalette.add(allLines);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
