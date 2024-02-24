# Encoder Decoder Application

The Encoder Decoder application is a Java program with a graphical user interface (GUI) that allows users to encode plaintext messages into ciphertext and decode ciphertext back into plaintext using various encoding algorithms.

## Features

- Encode plaintext messages into ciphertext using different encoding algorithms.
- Decode ciphertext back into plaintext.
- Support for multiple encoding algorithms such as Caesar cipher, Base64 encoding, and custom substitution ciphers.
- User-friendly graphical interface for inputting messages, selecting encoding algorithms, and viewing the encoded or decoded output.

## How to Run

1. Ensure you have Java installed on your PC. If not, download and install Java from [here](https://www.java.com/en/download/).
2. Download the `editor.java` file from this repository.
3. Open Command Prompt (on Windows) or Terminal (on macOS/Linux).
4. Navigate to the directory where the `editor.java` file is located.
5. Compile the Java source file using the following command:
    ```bash
    javac EncoderDecoder.java
    ```
6. Run the compiled Java program using the following command:
    ```bash
    java EncoderDecoder
    ```

## Usage

**Input Message:** Enter the message you want to encode/decode into the input text area.
**Select Algorithm:** Choose the encoding algorithm from the dropdown menu.
**Encode:** Click the "Encode" button to encode the input message using the selected algorithm.
**Decode:** Click the "Decode" button to decode the input message using the selected algorithm.
**Output Message:** View the encoded or decoded message in the output text area.

## Supported Algorithms
**Caesar Cipher:** Shifts each letter in the message by a fixed number of positions.
**Base64 Encoding:** Converts binary data into ASCII text format.
## Notes


- The application uses Java Swing for the graphical user interface.

