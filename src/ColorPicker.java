import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColorPicker extends JFrame {

    Model model;
    JColorChooser colorPicker = new JColorChooser();

    public ColorPicker(Model model) {
        super("Color Palette");
        this.model = model;

        this.add(colorPicker);
        this.setSize(500, 300);
        this.setVisible(true);
        addColorChangeListener();
    }

    private void addColorChangeListener() {
        colorPicker.getSelectionModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (model.getCurTool() != Tool.SELECT) {
                    model.setCurrentColor(colorPicker.getColor());
                } else {
                    model.setSelectedShapeColor(colorPicker.getColor());
                }
            }
        });
    }
}
