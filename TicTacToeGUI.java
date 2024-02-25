import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class TicTacToeGUI extends JFrame implements ActionListener {
    private JButton[][] buttons;
    private boolean xTurn = true;
    private JLabel statusLabel;
    private int xWins = 0;
    private int oWins = 0;
    private int draws = 0;
    private Connection connection;

    public TicTacToeGUI() {
        setTitle("Tic Tac Toe");
        setSize(300, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 3));
        buttons = new JButton[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
                buttons[i][j].setPreferredSize(new Dimension(100, 100));
                buttons[i][j].addActionListener(this);
                panel.add(buttons[i][j]);
            }
        }

        statusLabel = new JLabel("X's turn");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton resetButton = new JButton("Reset Scores");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetScores();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(resetButton);

        JMenuBar menuBar = new JMenuBar();
        JMenu historyMenu = new JMenu("History");
        JMenuItem statsItem = new JMenuItem("Show Stats");
        statsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStats();
            }
        });
        historyMenu.add(statsItem);
        JMenuItem highScoresItem = new JMenuItem("High Scores");
        highScoresItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHighScores();
            }
        });
        historyMenu.add(highScoresItem);
        menuBar.add(historyMenu);

        setJMenuBar(menuBar);

        add(panel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        connectToDatabase();
        createTable();
        setVisible(true);
    }

    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shubham", "root", "");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS scores (player VARCHAR(255) PRIMARY KEY, wins INT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        if (!button.getText().equals("")) {
            return;
        }

        if (xTurn) {
            button.setText("X");
            statusLabel.setText("O's turn");
        } else {
            button.setText("O");
            statusLabel.setText("X's turn");
        }

        if (checkWin()) {
            String winner = xTurn ? "X" : "O";
            JOptionPane.showMessageDialog(this, "Player " + winner + " wins!");
            if (winner.equals("X")) {
                xWins++;
            } else {
                oWins++;
            }
            updateScores(winner);
            resetGame();
        } else if (checkDraw()) {
            JOptionPane.showMessageDialog(this, "It's a draw!");
            draws++;
            updateScores(null); // No winner
            resetGame();
        }

        xTurn = !xTurn;
    }

    private boolean checkWin() {
        String[][] board = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = buttons[i][j].getText();
            }
        }

        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2]) && !board[i][0].equals("")) {
                return true;
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            if (board[0][j].equals(board[1][j]) && board[1][j].equals(board[2][j]) && !board[0][j].equals("")) {
                return true;
            }
        }

        // Check diagonals
        if ((board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2]) && !board[0][0].equals("")) ||
                (board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0]) && !board[0][2].equals(""))) {
            return true;
        }

        return false;
    }

    private boolean checkDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        statusLabel.setText("X's turn");
        xTurn = true;
    }

    private void showStats() {
        double totalGames = xWins + oWins + draws;
        double xRatio = (xWins / totalGames) * 100;
        double oRatio = (oWins / totalGames) * 100;
        double drawRatio = (draws / totalGames) * 100;

        JOptionPane.showMessageDialog(this, String.format("X Wins: %.2f%%\nO Wins: %.2f%%\nDraws: %.2f%%", xRatio, oRatio, drawRatio),
                "Winning Ratios", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showHighScores() {
        StringBuilder message = new StringBuilder("High Scores:\n");
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM scores")) {
            while (resultSet.next()) {
                String player = resultSet.getString("player");
                int wins = resultSet.getInt("wins");
                message.append(player).append(": ").append(wins).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(this, message.toString(), "High Scores", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateScores(String winner) {
        if (winner != null) {
            try {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO scores (player, wins) VALUES (?, 1) ON DUPLICATE KEY UPDATE wins = wins + 1");
                statement.setString(1, winner);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void resetScores() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to reset all scores?", "Reset Scores", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("TRUNCATE TABLE scores");
                xWins = 0;
                oWins = 0;
                draws = 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToeGUI::new);
    }
}
