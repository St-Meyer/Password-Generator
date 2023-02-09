import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.SecureRandom;

class PasswortgeneratorGUI extends JFrame {
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String Char_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String OTHER_CHAR = "!@#$%&*()_+-=[]?";

    private static final String PASSWORD_ALLOW_BASE = CHAR_LOWER + Char_UPPER + NUMBER;
    private static final String PASSWORD_ALLOW_BASE_SPECIAL = CHAR_LOWER + Char_UPPER + NUMBER + OTHER_CHAR;

    private static SecureRandom random = new SecureRandom();

    private JTextField passwordLengthField;
    private JButton generateButton;
    private JButton copyButton;
    private JCheckBox specialCharCheckbox;
    private JLabel passwordLabel;
    private JLabel passwordDisplay;


    public PasswortgeneratorGUI() {
        setTitle("Password Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        setLocationRelativeTo(null);
        setResizable(false);


        passwordLengthField = new JTextField(6);
        generateButton = new JButton("Generate");
        copyButton = new JButton("Copy");
        passwordLabel = new JLabel("Password: ");
        passwordDisplay = new JLabel();
        specialCharCheckbox = new JCheckBox("Use special characters");
        specialCharCheckbox.setSelected(true);

        DocumentFilter onlyNumbersFilter = new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                for (char c : string.toCharArray()) {
                    if (!Character.isDigit(c)) {
                        return;
                    }
                }
                super.insertString(fb, offset, string, attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
                for (char c : string.toCharArray()) {
                    if (!Character.isDigit(c)) {
                        return;
                    }
                }
                super.replace(fb, offset, length, string, attrs);
            }
        };

        ((AbstractDocument) passwordLengthField.getDocument()).setDocumentFilter(onlyNumbersFilter);

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    int length = Integer.parseInt(passwordLengthField.getText());
                    if (length <= 0) {
                        passwordDisplay.setText("Please enter a valid number");
                    }
                    if (length > 20) {
                        passwordDisplay.setText("Maximum height of 20");
                    }
                    else {
                        String password = generatePassword(length);
                        passwordDisplay.setText(password);
                    }
                }
                catch (NumberFormatException ex) {
                    passwordDisplay.setText("Please enter a valid number");
                }
            }
        });

        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = passwordDisplay.getText();
                StringSelection stringSelection = new StringSelection(password);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            }
        });

        JPanel lengthPabel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        lengthPabel.add(new JLabel("Password Length: "));
        lengthPabel.add(passwordLengthField);
        lengthPabel.add(specialCharCheckbox);
        add(lengthPabel);

        JPanel generatePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        generatePanel.add(generateButton);
        add(generatePanel);

        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordDisplay);
        add(passwordPanel);

        JPanel copyPanel = new JPanel(new FlowLayout((FlowLayout.CENTER)));
        copyPanel.add(copyButton);
        add(copyPanel);


        setSize(400, 200);
        setVisible(true);
    }

    public static void main(String[] args) {
        new PasswortgeneratorGUI();
    }

    private String generatePassword( int passwordLength) {
        StringBuilder password = new StringBuilder(passwordLength);
            if (specialCharCheckbox.isSelected()) {
                for (int i = 0; i < passwordLength; i++) {
                    password.append(PASSWORD_ALLOW_BASE_SPECIAL.charAt(random.nextInt(PASSWORD_ALLOW_BASE_SPECIAL.length())));
                }
            } else {
                for (int i = 0; i < passwordLength; i++) {
                    password.append(PASSWORD_ALLOW_BASE.charAt(random.nextInt(PASSWORD_ALLOW_BASE.length())));
                }
            }
        return password.toString();
    }
}