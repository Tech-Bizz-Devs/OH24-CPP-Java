import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Base64;

public class EncoderDecoder extends JFrame implements ActionListener {
    private JTextArea inputTextArea, outputTextArea;
    private JComboBox<String> algorithmComboBox;
    private JButton encodeButton, decodeButton;

    public EncoderDecoder() {
        setTitle("Encoder Decoder");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        inputTextArea = new JTextArea(5, 20);
        JScrollPane inputScrollPane = new JScrollPane(inputTextArea);

        outputTextArea = new JTextArea(5, 20);
        JScrollPane outputScrollPane = new JScrollPane(outputTextArea);
        outputTextArea.setEditable(false);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(1, 2));
        inputPanel.add(new JLabel("Input:"));
        inputPanel.add(inputScrollPane);

        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new GridLayout(1, 2));
        outputPanel.add(new JLabel("Output:"));
        outputPanel.add(outputScrollPane);

        JPanel buttonPanel = new JPanel();
        encodeButton = new JButton("Encode");
        decodeButton = new JButton("Decode");
        buttonPanel.add(encodeButton);
        buttonPanel.add(decodeButton);

        JPanel algorithmPanel = new JPanel();
        algorithmComboBox = new JComboBox<>();
        algorithmComboBox.addItem("Caesar Cipher");
        algorithmComboBox.addItem("Base64 Encoding");
        // Add more encoding algorithms as needed
        algorithmPanel.add(new JLabel("Select Algorithm:"));
        algorithmPanel.add(algorithmComboBox);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(outputPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(algorithmPanel, BorderLayout.WEST);

        encodeButton.addActionListener(this);
        decodeButton.addActionListener(this);

        add(mainPanel);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == encodeButton) {
            // Get selected algorithm
            String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
            // Encode input text
            String encodedText = encode(inputTextArea.getText(), selectedAlgorithm);
            // Display encoded text
            outputTextArea.setText(encodedText);
        } else if (e.getSource() == decodeButton) {
            // Get selected algorithm
            String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
            // Decode input text
            String decodedText = decode(inputTextArea.getText(), selectedAlgorithm);
            // Display decoded text
            outputTextArea.setText(decodedText);
        }
    }

    private String encode(String input, String algorithm) {
    StringBuilder encodedText = new StringBuilder();

    if (algorithm.equals("Caesar Cipher")) {
        int shift = 3; // Define the shift value for the Caesar Cipher (you can make it dynamic if needed)
        for (char c : input.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                encodedText.append((char) (((c - base + shift) % 26) + base));
            } else {
                encodedText.append(c);
            }
        }
    } else if (algorithm.equals("Base64 Encoding")) {
        encodedText.append(Base64.getEncoder().encodeToString(input.getBytes()));
    } else {
        // Handle custom encoding algorithms
        // For example, let's say you have a custom substitution cipher where 'A' maps to 'X', 'B' maps to 'Y', etc.
        StringBuilder customEncodedText = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                char encodedChar = (char) (((c - base + 3) % 26) + base); // This is just an example custom encoding logic
                customEncodedText.append(encodedChar);
            } else {
                customEncodedText.append(c);
            }
        }
        encodedText.append(customEncodedText);
    }

    return encodedText.toString();
}


    private String decode(String input, String algorithm) {
    StringBuilder decodedText = new StringBuilder();

    if (algorithm.equals("Caesar Cipher")) {
        int shift = 3; // Define the shift value for the Caesar Cipher (you can make it dynamic if needed)
        for (char c : input.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                decodedText.append((char) (((c - base - shift + 26) % 26) + base));
            } else {
                decodedText.append(c);
            }
        }
    } else if (algorithm.equals("Base64 Encoding")) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(input);
            decodedText.append(new String(decodedBytes));
        } catch (IllegalArgumentException e) {
            decodedText.append("Invalid Base64 encoded string.");
        }
    } else {
        // Handle custom decoding algorithms
        // For example, let's say you have a custom substitution cipher where 'X' maps to 'A', 'Y' maps to 'B', etc.
        StringBuilder customDecodedText = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                char decodedChar = (char) (((c - base - 3 + 26) % 26) + base); // This is just an example custom decoding logic
                customDecodedText.append(decodedChar);
            } else {
                customDecodedText.append(c);
            }
        }
        decodedText.append(customDecodedText);
    }

    return decodedText.toString();
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EncoderDecoder());
    }
}
