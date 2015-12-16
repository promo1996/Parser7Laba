package sisairis7;

import javax.swing.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;

import org.w3c.dom.*;


public class View extends JFrame {

    private static final int INIT_INPUT_COLUMN = 10;

    JPanel mainPanel;
    JPanel controlPanel;
    JPanel inputPanel;

    JList<Student> studentJList;
    DefaultListModel<Student> studentListModel;

    JTextField idField;
    JTextField nameField;
    JTextField markField;

    JButton addButton;
    JButton deleteButton;

    JFileChooser fileChooser;

    public View() {
        super("XML Parser Lab");

        createMenu();

        createGUI();

        setControls();

        this.add(mainPanel);

        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.pack();
        this.setVisible(true);

    }

    private void createMenu() {

        JMenuBar menuBar = new JMenuBar();

        JMenu file = new JMenu("File");

        JMenuItem openMenuItem = new JMenuItem("Open");

        fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new XMLFilter());
        fileChooser.setAcceptAllFileFilterUsed(false);

        openMenuItem.addActionListener(al -> {

            int returnVal = fileChooser.showDialog(View.this, "Select file");

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File fXmlFile = fileChooser.getSelectedFile();

                studentListModel.removeAllElements();

                try {

                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(fXmlFile);

                    doc.getDocumentElement().normalize();

                    Students students = new Students();

                    students.parse(doc.getElementsByTagName("student"));


                    students.getStudents().forEach(studentListModel::addElement);

                } catch (Exception e) {
                    e.printStackTrace();

                    JOptionPane.showMessageDialog(mainPanel,
                            "Wrong file!", "Error", JOptionPane.ERROR_MESSAGE);

                }

            }

            fileChooser.setSelectedFile(null);
        });


        JMenuItem saveMenuItem = new JMenuItem("Save");

        saveMenuItem.addActionListener( al -> {

            fileChooser.setDialogTitle("Specify a file to save");

            int userSelection = fileChooser.showSaveDialog(View.this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {

                File fileToSave = fileChooser.getSelectedFile();

                try {

                    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

                    Document doc = docBuilder.newDocument();
                    Element rootElement = doc.createElement("university");
                    doc.appendChild(rootElement);


                    for (int i = 0; i < studentListModel.size(); ++i) {
                        Student student = studentListModel.elementAt(i);
                        rootElement.appendChild(student.toElement(doc));
                    }

                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                    DOMSource source = new DOMSource(doc);
                    StreamResult result = new StreamResult(fileToSave);

                    transformer.transform(source, result);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            fileChooser.setSelectedFile(null);
        });


        JMenuItem exitMenuItem = new JMenuItem("Close");
        exitMenuItem.addActionListener( al -> System.exit(0) );

        file.add(openMenuItem);
        file.add(saveMenuItem);
        file.add(exitMenuItem);
        menuBar.add(file);
        setJMenuBar(menuBar);
    }

    private void setControls() {

        addButton.addActionListener(al -> {

            if (idField.getText().isEmpty() ||
                    nameField.getText().isEmpty() ||
                    markField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel,
                        "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String name = nameField.getText();

            int id;
            try {
                id = Integer.parseInt(idField.getText());
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(mainPanel,
                        "Invalid id format!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double mark;
            try {
                mark = Double.parseDouble(markField.getText());
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(mainPanel,
                        "Invalid mark format!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            studentListModel.addElement(new Student(id, name, mark));


            idField.setText("");
            nameField.setText("");
            markField.setText("");

        });


        deleteButton.addActionListener( al -> {

            int index = studentJList.getSelectedIndex();

            if (index != -1) {
                studentListModel.remove(index);
            } else {
                JOptionPane.showMessageDialog(mainPanel,
                        "Need to select element!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        });

    }

    private void createGUI() {

        mainPanel = new JPanel(new BorderLayout());

        studentListModel = new DefaultListModel<>();
        studentJList = new JList<>(studentListModel);
        studentJList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);

        inputPanel = new JPanel(new BorderLayout());

        idField = new JTextField(INIT_INPUT_COLUMN);
        JPanel idPanel = new JPanel(new BorderLayout());
        idPanel.add(new JLabel("id:"), BorderLayout.LINE_START);
        idPanel.add(idField, BorderLayout.LINE_END);

        nameField = new JTextField(INIT_INPUT_COLUMN);
        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.add(new JLabel("name:"), BorderLayout.LINE_START);
        namePanel.add(nameField, BorderLayout.LINE_END);

        markField = new JTextField(INIT_INPUT_COLUMN);
        JPanel markPanel = new JPanel(new BorderLayout());
        markPanel.add(new JLabel("mark:"), BorderLayout.LINE_START);
        markPanel.add(markField, BorderLayout.LINE_END);


        inputPanel.add(idPanel, BorderLayout.PAGE_START);
        inputPanel.add(namePanel, BorderLayout.CENTER);
        inputPanel.add(markPanel, BorderLayout.PAGE_END);

        addButton = new JButton("Add item");
        deleteButton  = new JButton("Delete item");

        controlPanel = new JPanel(new BorderLayout());
        controlPanel.add(inputPanel, BorderLayout.PAGE_START);
        controlPanel.add(addButton, BorderLayout.CENTER);
        controlPanel.add(deleteButton, BorderLayout.PAGE_END);


        mainPanel.add(new JScrollPane(studentJList), BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.LINE_END);
    }
}
