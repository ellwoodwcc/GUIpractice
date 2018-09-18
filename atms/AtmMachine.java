package atms;


import javax.swing.*;
import java.awt.*;

public class AtmMachine {
	//don't need actionlistener interface  to use it 
    /* declare Class Fields */
    private JFrame frame;
    private JPanel buttonPanel, inputPanel;
    private JTextField inputField;
    private JButton withdrawButton, depositButton, transferButton ,balanceButton;
    private JRadioButton savingsRadio, checkingRadio;
    private Account savingsAccount, checkingAccount;

    /* Constructor */
    private AtmMachine(double checkingBalance, double savingsBalance) {

        //setupFrame
        frame = new JFrame("ATM Machine");
        frame.setLayout(new FlowLayout());
        frame.setSize(300,200);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //setupButtonPanel
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0,2));

        //Add components to buttonPanel
        withdrawButton = new JButton("Withdraw");
        buttonPanel.add(withdrawButton);
        depositButton = new JButton("Deposit");
        buttonPanel.add(depositButton);
        transferButton = new JButton("Transfer");
        buttonPanel.add(transferButton);
        balanceButton = new JButton("Balance");
        buttonPanel.add(balanceButton);
        checkingRadio = new JRadioButton("Checking");
        buttonPanel.add(checkingRadio);
        savingsRadio = new JRadioButton("Savings");
        buttonPanel.add(savingsRadio);

        //setupInputPanel
        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0,1));
        //Add components to inputPanel
        inputField = new JTextField("",20);
        inputPanel.add(inputField);

        frame.add(buttonPanel); //must setupPanel first else get null pointer error
        frame.add(inputPanel); //must be setupPanel first else null pointer

        //setupButtonGroup();
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(checkingRadio);
        buttonGroup.add(savingsRadio);
        checkingRadio.setSelected(true); //by setting as default this avoids null pointers

        //setupEventListeners
        withdrawButton.addActionListener(actionEvent -> processWithdraw()); //Event Handler for Withdraw button click. No more actionhandler!!
        depositButton.addActionListener(actionEvent -> processDeposit()); //Event Handler for Deposit button click
        balanceButton.addActionListener(actionEvent -> processBalance()); //Event Handler for Balance button click
        transferButton.addActionListener(actionEvent -> processTransfer()); //Event Handler for Transfer button click

        //createAccounts
        checkingAccount = new Account(checkingBalance);
        savingsAccount = new Account(savingsBalance);

        // Show AtmMachine
        frame.setVisible(true);
    }

    /* Method For handling withdraws */
    private void processWithdraw(){
        if (isValidDouble()) {
            try {
                double amt = getInput();

                if (amt % 20 != 0) { //check if increment of 20
                    JOptionPane.showMessageDialog(frame, "Tender in denominations of $20.00 only");
                    return;
                }
                getActiveAccount().withdraw(amt);
                if (getActiveAccount().getSvcToken() > 4) {
                    JOptionPane.showMessageDialog(frame,
                            String.format("$%,.2f Withdrawn from %s\n($1.50 Service Charge applied)",
                                    amt, getAccountName()));
                } else {
                    JOptionPane.showMessageDialog(frame, String.format("$%,.2f Withdrawn from %s\n",
                            amt, getAccountName()));
                }
            } catch (InsufficientFunds e) {
                JOptionPane.showMessageDialog(frame, "Insufficient Funds");
            }
        }
    }

    /* Method For handling deposits */
    private void processDeposit(){
        if (isValidDouble()) {
            double amt = getInput();
            getActiveAccount().deposit(amt);
            JOptionPane.showMessageDialog(frame, String.format(" $%,.2f Deposited in %s",
                    amt, getAccountName()));
        }
    }

    /* Method For handling balance queries */
    private void processBalance(){
        JOptionPane.showMessageDialog(frame, String.format("%s Account Balance: $%,.2f",
                getAccountName(), getActiveAccount().getBalance()));
    }

    /* Method For handling transfers */
    private void processTransfer(){
        if (isValidDouble()) {
            try {
                double amt = getInput();
                String rcvAccountName = "Checking";

                if (getActiveAccount() == checkingAccount) {
                    savingsAccount.transferAmountTo(amt, checkingAccount);
                } else {
                    checkingAccount.transferAmountTo(amt, savingsAccount);
                    rcvAccountName = "Savings";
                }
                JOptionPane.showMessageDialog(frame, String.format(
                        "$%,.2f Transferred\nTo: %s\nFrom: %s", amt, rcvAccountName, getAccountName()));
            } catch (InsufficientFunds ex) {
                JOptionPane.showMessageDialog(frame, "Insufficient Funds");
            }
        }
    }


    /* Method to determine selected RadioButton and return corresponding account */
    private Account getActiveAccount(){
        return checkingRadio.isSelected() ? checkingAccount : savingsAccount;
    }

    /* Method to determine selected RadioButton and return corresponding account name string */
    private String getAccountName() {
        return checkingRadio.isSelected() ? "Checking" : "Savings";
    }

    /* Method for checking generic preconditions before processing events */
    private boolean isValidDouble() {
        boolean bool = false;
        try {
            //check if value is <=0 or can be parsed as a double
            double amt = Double.parseDouble(inputField.getText().replaceAll("[,$]",""));
            if (amt <= 0) {
                throw new NumberFormatException();
            }
            bool = true;
        }
        catch (NumberFormatException ex) {
            inputField.setText("");
            JOptionPane.showMessageDialog(frame,"Invalid Input");
        }
        //return pass;
        return bool;
    }

    /* Method to ensure the value in the text field is numeric. */
    //can throw exception. make sure it is either caught or proceeded by isValidDouble
    private double getInput() throws NumberFormatException {
        double amt = Double.parseDouble(inputField.getText().replaceAll("[,$]",""));
        inputField.setText("");
        return amt;
    }

    /* Main Method */
    public static void main(String[] args) {
        AtmMachine gui = new AtmMachine(20.00,20.00);
    }

}//End AtmMachine Class
