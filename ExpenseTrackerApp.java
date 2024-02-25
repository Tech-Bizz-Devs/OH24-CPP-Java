import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExpenseTrackerApp extends JFrame {

    private JTable expenseTable;
    private DefaultTableModel tableModel;
    private double totalExpenses = 0.0;
    private Double budget = null;

    public ExpenseTrackerApp() {
        setTitle("Expense Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu searchMenu = new JMenu("Search");
        JMenuItem searchByCategory = new JMenuItem("Search by Category");
        searchByCategory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String category = JOptionPane.showInputDialog(null, "Enter category to search:");
                if (category != null && !category.isEmpty()) {
                    searchExpensesByCategory(category);
                }
            }
        });
        searchMenu.add(searchByCategory);
        menuBar.add(searchMenu);
        setJMenuBar(menuBar);

        // Expense Input Panel
        JPanel expenseInputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        expenseInputPanel.setBorder(BorderFactory.createTitledBorder("Add Expense"));

        JLabel amountLabel = new JLabel("Amount:");
        JTextField amountField = new JTextField();
        JLabel categoryLabel = new JLabel("Category:");
        JTextField categoryField = new JTextField();
        JLabel descriptionLabel = new JLabel("Description:");
        JTextField descriptionField = new JTextField();

        // Add ActionListener to each text field to listen for Enter key press
        amountField.addActionListener(new EnterListener(categoryField));
        categoryField.addActionListener(new EnterListener(descriptionField));
        descriptionField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addExpense(amountField.getText(), categoryField.getText(), descriptionField.getText());
                clearFields(amountField, categoryField, descriptionField);
            }
        });

        expenseInputPanel.add(amountLabel);
        expenseInputPanel.add(amountField);
        expenseInputPanel.add(categoryLabel);
        expenseInputPanel.add(categoryField);
        expenseInputPanel.add(descriptionLabel);
        expenseInputPanel.add(descriptionField);

        // Expense Table Panel
        JPanel expenseTablePanel = new JPanel(new BorderLayout());
        expenseTablePanel.setBorder(BorderFactory.createTitledBorder("Expense List"));

        tableModel = new DefaultTableModel(new Object[]{"Amount", "Category", "Description"}, 0);
        expenseTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(expenseTable);

        expenseTablePanel.add(scrollPane, BorderLayout.CENTER);

        // Budget Panel
        JPanel budgetPanel = new JPanel(new BorderLayout());
        budgetPanel.setBorder(BorderFactory.createTitledBorder("Budget"));

        JLabel budgetLabel = new JLabel("Enter Budget:");
        JTextField budgetField = new JTextField(10);
        JButton setBudgetButton = new JButton("Set Budget");
        setBudgetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String budgetStr = budgetField.getText();
                if (isValidNumber(budgetStr)) {
                    budget = Double.parseDouble(budgetStr);
                    budgetLabel.setText("Budget: $" + budget);
                    budgetField.setText("");
                } else {
                    showError("Error: Please enter a valid budget.");
                }
            }
        });

        budgetPanel.add(budgetLabel, BorderLayout.NORTH);
        budgetPanel.add(budgetField, BorderLayout.CENTER);
        budgetPanel.add(setBudgetButton, BorderLayout.SOUTH);

        // Summary Panel
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Summary"));

        JButton totalExpensesButton = new JButton("Total Expenses");
        totalExpensesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Total Expenses: $" + totalExpenses);
            }
        });

        JButton budgetUtilizationButton = new JButton("Budget Utilization");
        budgetUtilizationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (budget == null) {
                    showError("Error: Please set the budget first.");
                } else {
                    double utilization = (totalExpenses / budget) * 100.0;
                    JOptionPane.showMessageDialog(null, "Budget Utilization: " + String.format("%.2f", utilization) + "%");
                }
            }
        });

        summaryPanel.add(totalExpensesButton, BorderLayout.NORTH);
        summaryPanel.add(budgetUtilizationButton, BorderLayout.SOUTH);

        mainPanel.add(expenseInputPanel, BorderLayout.NORTH);
        mainPanel.add(expenseTablePanel, BorderLayout.CENTER);
        mainPanel.add(budgetPanel, BorderLayout.WEST);
        mainPanel.add(summaryPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private static class EnterListener implements ActionListener {
        private JTextField nextField;

        public EnterListener(JTextField nextField) {
            this.nextField = nextField;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            nextField.requestFocusInWindow();
        }
    }

    private void addExpense(String amountStr, String category, String description) {
        if (!isValidNumber(amountStr)) {
            showError("Error: Please enter a valid amount.");
            return;
        }

        double amount = Double.parseDouble(amountStr);
        totalExpenses += amount;
        Object[] rowData = {String.format("$%.2f", amount), category, description};
        tableModel.addRow(rowData);

        if (budget != null && totalExpenses > budget) {
            JOptionPane.showMessageDialog(null, "Warning: Total expenses have exceeded the budget!");
        }
    }

    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    private boolean isValidNumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    private void searchExpensesByCategory(String category) {
        DefaultTableModel filteredModel = new DefaultTableModel(new Object[]{"Amount", "Category", "Description"}, 0);
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 1).toString().equalsIgnoreCase(category)) {
                filteredModel.addRow(new Object[]{tableModel.getValueAt(i, 0), tableModel.getValueAt(i, 1), tableModel.getValueAt(i, 2)});
            }
        }
        if (filteredModel.getRowCount() > 0) {
            JTable filteredTable = new JTable(filteredModel);
            JOptionPane.showMessageDialog(null, new JScrollPane(filteredTable), "Expenses for Category '" + category + "'", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No expenses found for Category '" + category + "'.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            ExpenseTrackerApp app = new ExpenseTrackerApp();
            app.setVisible(true);
        });
    }
}
