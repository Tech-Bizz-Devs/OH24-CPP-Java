import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Main extends JFrame {
    private JTabbedPane tabbedPane;
    private JTextArea currentTextArea;

    public Main() {
        setTitle("Versatile Text Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        createMenuBar();
        createToolbar();
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        JMenuItem newFileItem = new JMenuItem("New");
        JMenuItem openFileItem = new JMenuItem("Open");
        JMenuItem saveFileItem = new JMenuItem("Save");
        JMenuItem saveAsFileItem = new JMenuItem("Save As");
        JMenuItem closeFileItem = new JMenuItem("Close");

        fileMenu.add(newFileItem);
        fileMenu.add(openFileItem);
        fileMenu.add(saveFileItem);
        fileMenu.add(saveAsFileItem);
        fileMenu.addSeparator();
        fileMenu.add(closeFileItem);

        newFileItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createNewFile();
            }
        });

        openFileItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });

        saveFileItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        });

        saveAsFileItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveFileAs();
            }
        });

        closeFileItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeFile();
            }
        });

        menuBar.add(fileMenu);
    }

    private void createToolbar() {
        JToolBar toolBar = new JToolBar();
        add(toolBar, BorderLayout.NORTH);

        JButton newButton = new JButton(new ImageIcon("out/production/TextEditor/new.png.png"));
        JButton openButton = new JButton(new ImageIcon("out/production/TextEditor/open.png.png"));
        JButton saveButton = new JButton(new ImageIcon("out/production/TextEditor/save.png.png"));

        toolBar.add(newButton);
        toolBar.add(openButton);
        toolBar.add(saveButton);

        newButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createNewFile();
            }
        });

        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        });
    }

    private void createNewFile() {
        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        tabbedPane.addTab("Untitled", scrollPane);
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                FileReader fileReader = new FileReader(selectedFile);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                StringBuilder content = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line).append("\n");
                }

                JTextArea textArea = new JTextArea(content.toString());
                JScrollPane scrollPane = new JScrollPane(textArea);
                tabbedPane.addTab(selectedFile.getName(), scrollPane);
                tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);

                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveFile() {
        if (currentTextArea != null) {
            int tabIndex = tabbedPane.getSelectedIndex();
            String title = tabbedPane.getTitleAt(tabIndex);

            if (title.equals("Untitled")) {
                saveFileAs();
            } else {
                try {
                    FileWriter fileWriter = new FileWriter(title);
                    fileWriter.write(currentTextArea.getText());
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveFileAs() {
        if (currentTextArea != null) {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
            fileChooser.setFileFilter(filter);

            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                try {
                    FileWriter fileWriter = new FileWriter(selectedFile);
                    fileWriter.write(currentTextArea.getText());
                    fileWriter.close();

                    tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), selectedFile.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void closeFile() {
        int tabIndex = tabbedPane.getSelectedIndex();
        if (tabIndex != -1) {
            tabbedPane.remove(tabIndex);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}
