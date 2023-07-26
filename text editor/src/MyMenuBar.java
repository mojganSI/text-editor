import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class MyMenuBar extends JMenuBar {
    private static MyMenuBar menuBar = null;

    public static MyMenuBar createMenuBar() {
        if (menuBar == null) {
            menuBar = new MyMenuBar();
        }
        return menuBar;
    }

    private JMenu fileMenu;
    private JMenuItem newFileItem;
    private JMenuItem saveItem;
    private JMenuItem loadItem;
    private JMenuItem saveAsItem;
    private JMenuItem closeFileItem;

    private JMenu toolsMenu;
    private JMenuItem fontSizeItem;
    private JMenuItem fontColorItem;
    private JMenuItem fontStyleItem;

    private Font itemFont;

    private MyMenuBar() {
        itemFont = new Font(null, Font.PLAIN, 15);
        initFileMenu();
        initToolsMenu();
    }

    private void initFileMenu() {
        fileMenu = new JMenu("File");
        fileMenu.setFont(new Font(null, Font.BOLD, 14));


        newFileItem = new JMenuItem("new file");
        saveItem = new JMenuItem("save");
        saveAsItem = new JMenuItem("save as");
        loadItem = new JMenuItem("load");
        closeFileItem = new JMenuItem("close file");

        newFileItem.setFont(itemFont);
        saveItem.setFont(itemFont);
        loadItem.setFont(itemFont);
        saveAsItem.setFont(itemFont);
        closeFileItem.setFont(itemFont);

        newFileItem.addActionListener(createListener);
        saveItem.addActionListener(saveAsListener);
        saveAsItem.addActionListener(saveAsListener);
        loadItem.addActionListener(loadListener);
        closeFileItem.addActionListener(closeListener);

        fileMenu.add(newFileItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(loadItem);
        fileMenu.add(closeFileItem);
        add(fileMenu);
    }

    private void initToolsMenu() {
        toolsMenu = new JMenu("Tools");
        toolsMenu.setFont(new Font(null, Font.BOLD, 14));

        fontSizeItem = new JMenuItem("font size");
        fontColorItem = new JMenuItem("font color");
        fontStyleItem = new JMenuItem("font style");

        fontSizeItem.setFont(itemFont);
        fontColorItem.setFont(itemFont);
        fontStyleItem.setFont(itemFont);

        toolsMenu.add(fontColorItem);
        toolsMenu.add(fontSizeItem);
        toolsMenu.add(fontStyleItem);

        add(toolsMenu);
    }

    ActionListener fastSaveListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            MyPanel panel = MyPanel.createPanel();
            File file = panel.getFile();
            String text = panel.getText();
            try {
                writeInFile(file, text);
                panel.setFileContent(text);
                panel.setSaveState(true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    };

    ActionListener saveAsListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
            chooser.setFileFilter(filter);
            int res = chooser.showSaveDialog(null);
            File file = chooser.getSelectedFile();

            if (file == null || res == 1)
                return;


            if (!file.exists()) {
                try {
                    file = new File(file.getAbsolutePath() + ".txt");
                    file.createNewFile();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else if (!file.getName().split("[.]")[1].equals("txt")) {
                JOptionPane.showMessageDialog(null,
                        "You cant select non txt files!",
                        "Attention!",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            String inputText = MyPanel.createPanel().getText();
            try {
                writeInFile(file, inputText);
                MyPanel panel = MyPanel.createPanel();
                panel.setFile(file);
                panel.setFileContent(inputText);
                panel.setSaveState(true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    };

    ActionListener loadListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            MyPanel panel = MyPanel.createPanel();
            boolean flag = false;
            if (!panel.isSaveState()) {
                int res = JOptionPane.showConfirmDialog(null,
                        "Do you want to save this file ?",
                        "Attention!",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                while (!panel.isSaveState()) {
                    switch (res) {
                        case JOptionPane.YES_OPTION:
                            saveItem.doClick();
                            if (!panel.isSaveState()) {
                                JOptionPane.showMessageDialog(null
                                        , "Save dont happened! Save again."
                                        , "Warning", JOptionPane.WARNING_MESSAGE);
                            }
                            break;
                        case JOptionPane.NO_OPTION:
                            flag = true;
                            panel.setSaveState(true);
                            break;
                        default:
                            return;
                    }
                }
            }
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
            chooser.setFileFilter(filter);
            int res = chooser.showOpenDialog(null);

            File file = chooser.getSelectedFile();
            if (file == null || res == 1) {
                if (flag) {
                    panel.setSaveState(false);
                }
                return;
            }

            if (!file.exists()) {
                JOptionPane.showMessageDialog(null, "The selected file dose not exist!!"
                        , "Warning", JOptionPane.WARNING_MESSAGE);
                if (flag) {
                    panel.setSaveState(false);
                }
                return;
            }

            if (!file.getName().split("[.]")[1].equals("txt")) {
                // Todo : show Error on dialog box
                System.out.println("File is not txt!!");
                return;
            }

            try {
                String text = readFromFile(file);
                panel.setText(text);
                panel.setFile(file);
                panel.setFileContent(text);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        }
    };

    ActionListener createListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            MyPanel panel = MyPanel.createPanel();
            if (!panel.isSaveState()) {
                //Todo : show error
                System.out.println("Error");
                return;
            }
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text File", "txt");
            chooser.setFileFilter(filter);
            chooser.showSaveDialog(null);

            File file = chooser.getSelectedFile();
            if (file == null || file.exists()) {
                //Todo : show error for exists file
                System.out.println("this file exists!!");
                return;
            }

            try {
                file = new File(file.getAbsolutePath() + ".txt");
                if (file.exists()) {
                    //Todo : show error
                    System.out.println("this file exists!!");
                    return;
                }
                file.createNewFile();
                panel.setText("");
                panel.setFile(file);
                panel.setFileContent("");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    };

    ActionListener closeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            MyPanel panel = MyPanel.createPanel();
            if (!panel.isSaveState()) {
                // Todo : show error
                System.out.println("Error");
                return;
            }

            panel.setFile(null);
            panel.setText("");
        }
    };

    private void writeInFile(File file, String text) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(text);
        writer.flush();
        writer.close();
    }

    private String readFromFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        String text = "";
        while (scanner.hasNext()) {
            text += scanner.nextLine();
        }
        return text;
    }

    public void doSave() {
        saveItem.doClick();
    }

    public void setSaveItemListener(boolean isFastSave) {
        if (isFastSave) {
            saveItem.removeActionListener(saveAsListener);
            saveItem.addActionListener(fastSaveListener);
        } else {
            saveItem.removeActionListener(fastSaveListener);
            saveItem.addActionListener(saveAsListener);
        }
    }
}