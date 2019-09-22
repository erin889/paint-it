import java.awt.*;
import java.awt.geom.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.ListIterator;

import static java.lang.Math.*;

interface View {
    void repaintView();
}

enum Tool {
    SELECT,
    ERASER,
    LINE,
    CIRCLE,
    RECT,
    FILL
}

public class Model {
    private Shape2D drawing = null;
    private Shape2D selected = null;
    private int curStroke = 1;
    private Tool curTool = Tool.SELECT;
    private Color curColor = Color.BLACK;
    private ArrayList<Shape2D> shapes = new ArrayList<>();
    private ArrayList<View> views = new ArrayList<>();

    public double relX = 0, relY = 0;
    public boolean isCopy = false;

    public Shape2D getDrawing() {
        return drawing;
    }
    public Shape2D getSelected() {
        return selected;
    }

    public ArrayList<Shape2D> getShapes() {
        return shapes;
    }

    public void addView(View v) {
        views.add(v);
        v.repaintView();
    }

    public Tool getCurTool() {
        return curTool;
    }
    public Color getCurColor() {
        return curColor;
    }
    public int getCurStroke() {
        return curStroke;
    }

    private void update() {
        for (View v : views) {
            v.repaintView();
        }
    }

    public void newFile() {
        shapes = new ArrayList<>();
        update();
    }

    public void setSelectedShapeColor(Color color) {
        if(selected != null) { selected.shapeColor = color; }
        update();
    }

    public void setSelectedStroke(int stroke) {
        if(selected != null) { selected.shapeStroke = stroke; }
        update();
    }

    public void setCurTool(Tool t) {
        curTool = t;
        update();
    }

    public void setCurrentColor(Color c) {
        curColor = c;
        update();
    }

    public void setCurStroke(int stroke) {
        curStroke = stroke;
        update();
    }

    public void read(ArrayList<Shape2D> listOfShapes) {
        shapes = listOfShapes;
        update();

    }

    public void add() {
        shapes.add(drawing);
        drawing = null;
        update();
    }

    public void deselectShape() {
        selected = null;
        update();
    }

    public void copyCanvas() {
        isCopy = true;
        update();
    }

    public void select(int mouseX, int mouseY) {
        int size = shapes.size();
        int i = size-1;

        ListIterator li = shapes.listIterator(size);

        while(li.hasPrevious()) {
            Shape2D s2 = (Shape2D)li.previous();
            if (s2.shape.contains(mouseX, mouseY) ||
                    s2.shape.intersects(mouseX-2, mouseY-2, 5, 5)) {
                curColor = s2.shapeColor;
                curStroke = s2.shapeStroke;
                selected = s2;
                Shape2D top = shapes.remove(i);
                shapes.add(top);
                break;
            }
            i--;
        }
        if (selected != null) {
            relX = selected.shape.getBounds().getX()-mouseX; // mouse pos relative to left corner of shape
            relY = selected.shape.getBounds().getX()-mouseY;
        }
        update();
    }


    public void eraser(int x, int y) {
        int size = shapes.size();
        int i = size-1;
        ListIterator li = shapes.listIterator(size);
        while (li.hasPrevious()) {
            Shape2D s2 = (Shape2D)li.previous();
            if (s2.shape.contains(x, y) || s2.shape.intersects(x-2,y-2,5,5)) {
                shapes.remove(i);
                break;
            }
            i--;
        }
        update();
    }

    public void fill(int x, int y) {
        int size = shapes.size();
        ListIterator li = shapes.listIterator(size);
        while (li.hasPrevious()) {
            Shape2D s2 = (Shape2D)li.previous();
            if (s2.shape.contains(x, y)) {
                s2.shapeColor = curColor;
                break;
            }
        }
        update();
    }

    public void move(int x, int y) {
        if (selected == null) return;
        double preX = selected.shape.getBounds().getX();
        double preY = selected.shape.getBounds().getY();
        AffineTransform at = new AffineTransform();
        at.translate(x - preX + relX, y - preY + relY);
        selected.shape = at.createTransformedShape(selected.shape);
        update();
    }

    public void draw(int x1, int y1, int x2, int y2) {
        int width = abs(x1 - x2);
        int height = abs(y1 - y2);

        if(curTool == Tool.LINE) {
            drawing = new Shape2D(curColor, new Line2D.Float(x1,y1,x2,y2), curStroke);
            drawing.shapeBorderColor = curColor;
        } else if(curTool == Tool.RECT) {
            drawing = new Shape2D(curColor, new Rectangle2D.Float(min(x1, x2), min(y1, y2),
                    width, height), curStroke);
        }
        else if(curTool == Tool.CIRCLE) {
            drawing = new Shape2D(curColor, new Ellipse2D.Float(min(x1, x2), min(y1, y2),
                    min(width, height), min(width, height)), curStroke);
        }
        update();
    }
}

class Shape2D implements Serializable {
    Color shapeColor;
    Color shapeBorderColor = Color.BLACK;
    Shape shape;
    int shapeStroke = 1;

    Shape2D(Color c, Shape s, int stroke) {
        shapeColor = c;
        shape = s;
        shapeStroke = stroke;
    }
}