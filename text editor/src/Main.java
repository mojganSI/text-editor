import javax.swing.*;

public class Main {

    public final static JFrame APP_FRAME = new JFrame("TextEditor");

    public static void main(String[] args) {
        APP_FRAME.setJMenuBar(MyMenuBar.createMenuBar());
        APP_FRAME.add(MyPanel.createPanel());
        APP_FRAME.setBounds(550, 150, 900, 600);
        APP_FRAME.setVisible(true);
        APP_FRAME.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}

//  1- open file from system directories and edit
//  2- create a new file and save in specified location
//  ** ctrl + S shortcut