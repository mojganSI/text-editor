import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

public class MyPanel extends JPanel {
    private static MyPanel myPanel = null;

    public static MyPanel createPanel() {
        if (myPanel == null) {
            myPanel = new MyPanel();
        }
        return myPanel;
    }

    private File file;
    private JTextArea textArea;
    private String fileContent = "";
    private boolean isCtrlHold = false;
    private boolean saveState = true;

    private MyPanel() {
        setBackground(Color.decode("#94B49F"));

        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 20));
        textArea.setMargin(new Insets(10, 10, 10, 10));
        textArea.setLineWrap(true);
        textArea.setBackground(Color.decode("#FCF8E8"));
        textArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 83 && isCtrlHold) {
                    MyMenuBar.createMenuBar().doSave();
                    isCtrlHold = false;
                }
                if (!isCtrlHold && e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    isCtrlHold = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                JFrame frame = Main.APP_FRAME;
                String title = frame.getTitle();
                if (!fileContent.equals(textArea.getText())) {
                    if (!title.endsWith(" *")) {
                        frame.setTitle(title + " *");
                    }
                    saveState = false;
                } else {
                    if (title.endsWith(" *")) {
                        frame.setTitle(title.replace(" *", ""));
                    }
                    saveState = true;
                }


                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    isCtrlHold = false;
                }
            }
        });
        add(textArea);

        setLayout(null);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Dimension d = getSize();
        textArea.setBounds(15, 15, Math.max(d.width - 30, 250), Math.max(d.height - 30, 150));
    }

    public String getText() {
        return textArea.getText();
    }

    public void setText(String text) {
        textArea.setText(text);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        MyMenuBar menuBar = MyMenuBar.createMenuBar();
        if (file == null) {
            menuBar.setSaveItemListener(false);
            Main.APP_FRAME.setTitle("TextEditor");
        } else {
            menuBar.setSaveItemListener(true);
            Main.APP_FRAME.setTitle(file.getName().split("[.]")[0]);
        }
        this.file = file;
    }

    public void setFileContent(String text) {
        fileContent = text;
    }

    public boolean isSaveState() {
        return saveState;
    }

    public void setSaveState(boolean saveState) {
        this.saveState = saveState;
    }
}