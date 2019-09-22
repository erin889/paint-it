import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;

public class Dionysus {

    public static void main(String[] args) {

        Model model = new Model();

        ToolPalette tools = new ToolPalette(model);
        Canvas canvas = new Canvas(model);
        model.addView(tools);
        model.addView(canvas);

        JFrame view = new JFrame("Dionysus");
        view.setMinimumSize(new Dimension(800, 700));
        view.setPreferredSize(new Dimension(900,700));
        view.setFocusable(true);
        view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JFileChooser fc = new JFileChooser();
        JMenuBar menu = new JMenuBar();
        JMenu file = new JMenu("File");
        menu.add(file);

        JMenuItem create = new JMenuItem("New");
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.newFile();
            }
        });
        file.add(create);

        // This code is inspired by StackOverflow: https://stackoverflow.com/a/40255184
        JMenuItem load = new JMenuItem("Load");
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fc.setFileFilter(new FileNameExtensionFilter("MyDay File","myday"));
                File dir = new File(System.getProperty("user.dir"));
                fc.setCurrentDirectory(dir);
                int access = fc.showOpenDialog(view);
                if (access == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                        FileInputStream fs = new FileInputStream(file);
                        ObjectInputStream ois = new ObjectInputStream(fs);
                        ArrayList<Shape2D> data = (ArrayList<Shape2D>) ois.readObject();
                        model.read(data);
                        ois.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        file.add(load);

        // This code is inspired by StackOverflow: https://stackoverflow.com/a/13996949
        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fc.setFileFilter(new FileNameExtensionFilter("MyDay File", "myday"));
                File dir = new File(System.getProperty("user.dir"));
                fc.setCurrentDirectory(dir);
                int access = fc.showSaveDialog(view);
                if (access == JFileChooser.APPROVE_OPTION) {
                    try {
                        String path = fc.getSelectedFile().getAbsolutePath();
                        if (!path.endsWith(".myday"))  path += ".myday";
                        File newFile = new File(path);
                        FileOutputStream fs = new FileOutputStream(newFile);
                        ObjectOutputStream oos = new ObjectOutputStream(fs);
                        oos.writeObject(model.getShapes());
                        oos.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        file.add(save);

        view.setJMenuBar(menu);
        Container cp = view.getContentPane();
        cp.setLayout(new BoxLayout(cp, BoxLayout.X_AXIS));
        cp.add(tools);
        cp.add(canvas);

        view.pack();
        view.setVisible(true);

        // This code is inspired by StackOverflow: https://stackoverflow.com/a/5971063
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED
                        && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    model.deselectShape();
                } else if ((e.getKeyCode() == KeyEvent.VK_C)
                        && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
                    model.copyCanvas();
                }
                return true;
            }
        });
    }
}
