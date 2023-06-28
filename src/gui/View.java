package gui;

import fractionexception.MixedFractionException;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
import javax.swing.Timer;

public class View extends JFrame {

    Controller controller;
    private ImageTextField inputField;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void showUI() throws IOException, FontFormatException {
        controller.view.createJFrame();
    }

    public void createJFrame() throws IOException, FontFormatException {
        JFrame jFrame = new JFrame();
        jFrame.setTitle("Fraction Calculator");
        ImageIcon icoImage = createScaledImageIcon("/assets/pics/icon.png");
        jFrame.setIconImage(icoImage.getImage());
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setContentPane(contentPane());
        jFrame.pack();
        jFrame.setMinimumSize(new Dimension(311,431));
        jFrame.setResizable(false);
        jFrame.setSize(311, 431);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }

    public JPanel contentPane() throws IOException, FontFormatException {
        JPanel contentPane = new JPanel(new BorderLayout(5, 5));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        JTextField inputField = inputField();
        JPanel buttonPanel = buttonPanel();
        contentPane.add(inputField, BorderLayout.NORTH);
        contentPane.add(buttonPanel, BorderLayout.CENTER);
        setContentPane(contentPane);
        return contentPane;
    }

    public JTextField inputField() throws IOException, FontFormatException {
        ImageIcon javaImage = createScaledImageIcon("/assets/pics/audio.png");
        inputField = new ImageTextField(javaImage);
        inputField.setEditable(false);
        inputField.setDocument((new LengthRestrictedDocument(15)));
        InputStream is = getClass().getResourceAsStream("/assets/fonts/digital-7.ttf");
        Font calculatorFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(is)).deriveFont(40f);
        inputField.setFont(calculatorFont);
        inputField.setBackground(new Color(212, 226, 227));
        inputField.setBorder(new CompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(10, 10, 10, 10)));
        inputField.setHorizontalAlignment(JTextField.RIGHT);
        return inputField;
    }

    public JPanel buttonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 5, 5));
        buttonPanel.setPreferredSize(new Dimension(325, 300));
        addButtonsToPanel(buttonPanel);
        return buttonPanel;
    }

    public void addButtonsToPanel(JPanel buttonPanel) {
        ImageIcon offImage = createScaledImageIcon("/assets/pics/switch-off.png");
        ImageIcon onImage = createScaledImageIcon("/assets/pics/switch-on.png");
        ImageIcon onVoice = createScaledImageIcon("/assets/pics/audio.png");
        ImageIcon offVoice = createScaledImageIcon("/assets/pics/no-audio.png");
        JToggleButton powerButton = new JToggleButton(offImage, false);
        JToggleButton voiceButton = new JToggleButton(onVoice, false);
        final Boolean[] checkSound = new Boolean[1];
        checkSound[0] = true;


        for (AbstractButton button : Arrays.asList(
                new JButton("7"), new JButton("8"), new JButton("9"), new JButton("÷"),
                new JButton("4"), new JButton("5"), new JButton("6"), new JButton("*"),
                new JButton("1"), new JButton("2"), new JButton("3"), new JButton("-"),
                new JButton("0"), new JButton("C"), new JButton("="), new JButton("+"),
                powerButton, voiceButton, new JButton("←"),
                new JButton("x" + diagonalFraction(1, 2))
        )) {
            button.setFont(new Font("Arial Unicode MS", Font.BOLD, 20));
            button.setForeground(Color.white);

            if(button.getText().matches("[+\\-*÷]")){
                MyDocumentListener myDocumentListener = new MyDocumentListener((JButton) button, inputField, "[+\\-*÷]");
                inputField.getDocument().addDocumentListener(myDocumentListener);
            }

            if(button.getText().equals("=")){
                MyDocumentListener myDocumentListener = new MyDocumentListener((JButton) button, inputField, "=");
                inputField.getDocument().addDocumentListener(myDocumentListener);
            }

            if(button.getText().equals("x" + diagonalFraction(1, 2))){
                MyDocumentListener myDocumentListener = new MyDocumentListener((JButton) button, inputField, "x" + diagonalFraction(1, 2));
                inputField.getDocument().addDocumentListener(myDocumentListener);
            }

            if(button.getText().matches("C")){
                MyDocumentListener myDocumentListener = new MyDocumentListener((JButton) button, inputField, "C");
                inputField.getDocument().addDocumentListener(myDocumentListener);
            }

            if(button.getText().matches("←")){
                MyDocumentListener myDocumentListener = new MyDocumentListener((JButton) button, inputField, "←");
                inputField.getDocument().addDocumentListener(myDocumentListener);
            }

            if(button.equals(voiceButton)){
                voiceButton.addItemListener(e -> {
                    if(voiceButton.isSelected()){
                        playSound("/assets/sounds/speaker-click.wav", checkSound[0]);
                        turnOffVoice();
                        checkSound[0] = false;
                    }
                    else{
                        turnOnVoice();
                        checkSound[0] = true;
                        playSound("/assets/sounds/speaker-click.wav", true);
                    }
                });
            }

            if(!(button instanceof JToggleButton)) {
                button.addActionListener(e -> {
                    String command = button.getText();
                    if (command.matches("[0-9]+|[+\\-*÷]")) {
                        if(command.equals("÷")){
                            command = "/";
                        }
                        setResult(inputField.getText() + command);
                        playSound("/assets/sounds/button-click.wav", checkSound[0]);
                    } else if (command.equals("=")) {



                       if (inputField.getText().contains("⁄")) {
                            try {
                                controller.handleFraction(inputField.getText());
                                playSound("/assets/sounds/result-click.wav", checkSound[0]);
                            }
                            catch (MixedFractionException exception){
                                setResult(exception.getMessage());
                                playSound("/assets/sounds/error-click.wav", checkSound[0]);
                            }
                        } else {
                            try {
                                controller.handleCalculation(inputField.getText());
                                playSound("/assets/sounds/result-click.wav", checkSound[0]);
                            } catch (MixedFractionException exception) {
                                setResult(exception.getMessage());
                                playSound("/assets/sounds/error-click.wav", checkSound[0]);
                            }
                        }

                    } else if (command.equals("C")) {
                        setResult("");
                        playSound("/assets/sounds/clear-click.wav", checkSound[0]);
                    }
                });
            }

            if (button.getText().equals("←")) {
                button.addActionListener(e -> {
                    String textFieldText = inputField.getText();
                    char[] chars = textFieldText.toCharArray();
                    if (chars.length > 0) {
                        StringBuilder sb = new StringBuilder(textFieldText);
                        sb.deleteCharAt(chars.length - 1);
                        setResult(sb.toString());
                        playSound("/assets/sounds/back-click.wav", checkSound[0]);
                    }
                });
            }


            if (button.getText().equals("x" + diagonalFraction(1, 2))) {
                button.addActionListener(e -> {
                    playSound("/assets/sounds/fraction-click.wav", checkSound[0]);
                    String[] options = {"OK", "Cancel"};
                    CustomJOptionPane customJOptionPane = null;
                    try {
                        customJOptionPane = new CustomJOptionPane();
                        String numeratorStr = customJOptionPane.showInputDialog(null, "Enter the numerator:", "Fraction", options);
                        playSound("/assets/sounds/fraction-click.wav", checkSound[0]);
                        String denominatorStr = customJOptionPane.showInputDialog(null, "Enter the denominator:", "Fraction", options);
                        int numerator = Integer.parseInt(numeratorStr);
                        int denominator = Integer.parseInt(denominatorStr);
                        if(denominator == 0){
                            Objects.requireNonNull(customJOptionPane).showMessageDialog(null, "Denominator cannot be zero!", "Error");
                            playSound("/assets/sounds/error-click.wav", checkSound[0]);
                        }
                        Font inputFieldFont = new Font("Arial Unicode MS", Font.PLAIN, 40);
                        inputField.setFont(inputFieldFont);
                        setResult(inputField.getText() + diagonalFraction(numerator, denominator));
                    } catch (NumberFormatException exception) {
                        playSound("/assets/sounds/error-click.wav", checkSound[0]);
                        Objects.requireNonNull(customJOptionPane).showMessageDialog(null, "Only digits are allowed!", "Error");
                    } catch (IndexOutOfBoundsException exception) {
                        playSound("/assets/sounds/error-click.wav", checkSound[0]);
                        Objects.requireNonNull(customJOptionPane).showMessageDialog(null, "Please input both numerator and denominator!", "Error");
                    }
                });
            }

            if (button.equals(powerButton)) {
                powerButton.addItemListener(e -> {
                    if (powerButton.isSelected()) {
                        playSound("/assets/sounds/off-click.wav", checkSound[0]);
                        enableButtons(buttonPanel);
                        setResult("");
                    } else {
                        playSound("/assets/sounds/on-click.wav", checkSound[0]);
                        disableButtons(buttonPanel);
                        Timer timer = new Timer(35, new ActionListener() {
                            private float alpha = 1.0f;

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                alpha -= 0.1f;
                                if (alpha <= 0) {
                                    ((Timer)e.getSource()).stop();
                                    inputField.setForeground(null);
                                    setResult("OFF");
                                } else {
                                    inputField.setForeground(new Color(inputField.getForeground().getRed(), inputField.getForeground().getGreen(), inputField.getForeground().getBlue(), (int)(alpha * 255)));
                                }
                            }
                        });
                        timer.start();
                    }
                });
            } else {
                button.setEnabled(false);
            }

            if (button.getText().equals("=")|| button.getText().equals("C")) {
                button.setBackground(new Color(0xE65100));
            } else if (button.getText().equals("+")|| button.getText().equals("-") || button.getText().equals("*") || button.getText().equals("÷")) {
                button.setBackground(new Color(243, 243, 241));
                button.setForeground(Color.BLACK);
            } else if (button.getText().equals("+/-") || button.getText().equals(("x" + diagonalFraction(1, 2)))) {
                button.setBackground(new Color(0x9E9E9E));
            }
            else if(button.getText().equals("←")) {
                button.setBackground(new Color(175, 67, 76));
            }
            else if(button.getIcon() != null && button.getIcon().equals(offImage)){
                button.setBackground(new Color(231, 139, 73));
                button.setSelectedIcon(onImage);
                setResult("OFF");
            }
            else if(button.getIcon() != null && button.getIcon().equals(onVoice)){
                button.setBackground(new Color(27, 58, 139));
                button.setSelectedIcon(offVoice);
            }
            else if (button.getText().matches("[0-9]")){
                button.setBackground(new Color(0x424242));
            }

            button.setPreferredSize(new Dimension(55, 55));
            button.setUI(new StyledButtonUI());
            buttonPanel.add(button);
        }
    }

    private void turnOffVoice() {
        ImageIcon icon = createScaledImageIcon("/assets/pics/no-audio.png");
        inputField.setImage(icon);
        inputField.revalidate();
        inputField.repaint();
    }

    private void turnOnVoice() {
        ImageIcon icon = createScaledImageIcon("/assets/pics/audio.png");
        inputField.setImage(icon);
        inputField.revalidate();
        inputField.repaint();
    }

    private void disableButtons(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            AbstractButton button = (AbstractButton) comp;
            if(comp instanceof JToggleButton){
                button.setEnabled(true);
            }
            else if (comp != null) {
                button.setEnabled(false);
            }
        }
    }

    private void enableButtons(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof AbstractButton button) {
                button.setEnabled(true);
            }
        }
    }

    private void playSound(String soundFilePath, Boolean checkSound) {
        if(checkSound) {
            try {
                InputStream inputStream = getClass().getResourceAsStream(soundFilePath);
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Objects.requireNonNull(inputStream));
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } catch (LineUnavailableException | UnsupportedAudioFileException | IOException ex) {
                System.out.println("Error playing sound file: " + ex.getMessage());
            }
        }
    }

    public String diagonalFraction(int numerator, int denominator) {
        char[] numeratorDigits = new char[]{
                '⁰', '¹', '²', '³', '⁴',
                '⁵', '⁶', '⁷', '⁸', '⁹'};
        char[] denominatorDigits = new char[]{
                '₀', '₁', '₂', '₃', '₄',
                '₅', '₆', '₇', '₈', '₉'};
        char fractionSlash = '⁄';

        if (denominator == 0) {
            return "";
        }

        if (numerator == 0) {
            return "0";
        }
        StringBuilder numeratorStr = new StringBuilder();
        while (numerator > 0) {
            numeratorStr.insert(0, numeratorDigits[numerator % 10]);
            numerator = numerator / 10;
        }
        StringBuilder denominatorStr = new StringBuilder();
        while (denominator > 0) {
            denominatorStr.insert(0, denominatorDigits[denominator % 10]);
            denominator = denominator / 10;
        }
        return " "+numeratorStr + fractionSlash + denominatorStr;
    }

    public ImageIcon createScaledImageIcon(String path) {
        ImageIcon image = new ImageIcon(Objects.requireNonNull(getClass().getResource(path)));
        Image scaledImg = image.getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }

    public void setResult(String res) {
        this.inputField.setText(res);
    }

}

class ImageTextField extends JTextField {
    private ImageIcon imageIcon;

    public ImageTextField(ImageIcon icon) {
        super();
        this.imageIcon = icon;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int imageX = 5;
        int imageY = (getHeight() - imageIcon.getIconHeight()) / 2;
        imageIcon.paintIcon(this, g, imageX, imageY);
    }

    public void setImage(ImageIcon icon) {
        this.imageIcon = icon;
    }

}

class StyledButtonUI extends BasicButtonUI {

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        AbstractButton button = (AbstractButton) c;
        button.setOpaque(false);
        button.setBorder(new EmptyBorder(5, 15, 5, 15));
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        AbstractButton b = (AbstractButton) c;
        paintBackground(g, b, b.getModel().isPressed() ? 2 : 0);
        super.paint(g, c);
    }

    private void paintBackground(Graphics g, JComponent c, int yOffset) {
        Dimension size = c.getSize();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (c instanceof JToggleButton) {
            yOffset += 2;
        }

        g.setColor(c.getBackground().darker());
        g.fillRoundRect(0, yOffset, size.width, size.height - yOffset, 10, 10);
        g.setColor(c.getBackground());
        g.fillRoundRect(0, yOffset, size.width, size.height + yOffset - 5, 10, 10);
    }
}

class LengthRestrictedDocument extends PlainDocument {

    private final int limit;

    public LengthRestrictedDocument(int limit) {
        this.limit = limit;
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a)
            throws BadLocationException {
        if (str == null)
            return;

        if ((getLength() + str.length()) <= limit) {
            super.insertString(offs, str, a);
        }
    }
}

class CustomJOptionPane {

    public ImageIcon createScaledImageIcon(String path) {
        ImageIcon image = new ImageIcon(Objects.requireNonNull(getClass().getResource(path)));
        Image scaledImg = image.getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }

    public String showInputDialog(Component parentComponent, String message, String title, String[] options) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(message);
        JTextField textField = new JTextField(10);
        textField.setDocument((new LengthRestrictedDocument(4)));

        ImageIcon icoImage = createScaledImageIcon("/assets/pics/icon.png");
        JLabel iconLabel = new JLabel(icoImage);

        panel.add(iconLabel);
        panel.add(label);
        panel.add(textField);

        final Object[][] returnValue = new Object[1][1];

        JButton[] buttons = new JButton[options.length];
        for (int i = 0; i < options.length; i++) {
            final int option = i;
            buttons[i] = new JButton(options[i]);
            if(i == 0) {
                buttons[i].setBackground(new Color(78, 135, 82));
            } else {
                buttons[i].setBackground(new Color(227, 82, 82));
            }
            buttons[i].setUI(new StyledButtonUI());
            buttons[i].addActionListener(e -> {
                String text = textField.getText();
                JOptionPane.getRootFrame().dispose();
                returnValue[0] = new Object[]{option, text};
            });
        }

        JOptionPane.showOptionDialog(parentComponent, panel, title, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, buttons[0]);

        if (returnValue[0] == null) {
            return null;
        } else {
            Object[] selectedValues = returnValue[0];
            return (String) selectedValues[1];
        }
    }

    public void showMessageDialog(Component parentComponent, String message, String title) {
        JPanel panel = new JPanel();
        JLabel messageLabel = new JLabel(message);

        ImageIcon icon = createScaledImageIcon("/assets/pics/icon.png");
        JLabel iconLabel = new JLabel(icon);

        panel.add(iconLabel);
        panel.add(messageLabel);

        JButton button = new JButton("Okay");
        button.addActionListener(e -> JOptionPane.getRootFrame().dispose());
        button.setUI(new StyledButtonUI());
        button.setPreferredSize(new Dimension(75, 25));
        button.setBackground(new Color(227, 82, 82));

        JButton[] options = { button };
        JOptionPane.showOptionDialog(parentComponent, panel, title, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, -1);

    }

}

class MyDocumentListener implements DocumentListener {
    private final JButton button;
    private final JTextField inputField;
    private final String buttonText;

    public MyDocumentListener(JButton button, JTextField inputField, String buttonText) {
        this.button = button;
        this.inputField = inputField;
        this.buttonText = buttonText;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        checkButtonEnabled();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        checkButtonEnabled();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        checkButtonEnabled();
    }

    private void checkButtonEnabled() {
        View view = new View();
        String text = inputField.getText();
        char lastChar = text.length() > 0 ? text.charAt(text.length() - 1) : 'F';
        if (buttonText.equals("C")) {
            button.setEnabled(text.length() > 1 && lastChar != 'F');
        } else if (buttonText.equals("=")) {
            button.setEnabled(text.contains("+") || text.contains("-") || text.contains("/") || text.contains("*"));
        } else if (buttonText.equals("←")) {
            button.setEnabled(text.length() > 0 && lastChar != 'F');
        } else if (buttonText.equals("x" + view.diagonalFraction(1, 2))) {
            String s = Character.toString(lastChar);
            button.setEnabled(!s.matches("[₀-₉]"));
        } else {
            button.setEnabled(lastChar != '+' && lastChar != '-' && lastChar != '*' && lastChar != '/' && lastChar != 'F');
        }
    }
}





