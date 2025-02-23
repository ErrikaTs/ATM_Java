//Enter an account number between 1000 and 1999 to access an account.
//The PIN is the same as the account number.
//The ATM is stocked with 100 banknotes of each denomination (€10, €20, and €50).


import java.util.Random;
import java.util.Scanner;

public class ATMSystem {
    static int[][] accounts = new int[1000][2];   // [accountNumber, balance]
    static int[] atmCash = {100, 100, 100};   // {50€, 20€, 10€}
    static int[] billValues = {50, 20, 10};
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            accounts[i][0] = 1000 + i;     // 1000 - 1999
            accounts[i][1] = random.nextInt(2001);  // 0€ - 2000€
        }
        atmInterface();
    }

    static void atmInterface() {
        System.out.print("Enter account number: ");
        int accountNumber = scanner.nextInt();

        int index = findAccount(accountNumber);
        while (index == -1) { 
            System.out.println("Account not found. Please enter account number again: \n");
            accountNumber = scanner.nextInt();
            index = findAccount(accountNumber);
        }


        System.out.print("Enter your PIN: ");
        int tries = 3;
        while (scanner.nextInt() != accountNumber && tries > 0) {
            tries -= 1;
            if (tries == 0) {
                System.out.println("Account Blocked!!!");
                return;
            }
            System.out.println("Wrong PIN. You have " + tries + " attempts left. Please try again: \n");
        }

        while (true) {
            System.out.println("1. Check Balance\n2. Deposit\n3. Withdraw\n4. Exit");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();

            if (choice == 1) {
                checkBalance(index);
            }
            else if (choice == 2) {
                System.out.print("Deposit amount: ");
                deposit(index, scanner.nextInt());
            } else if (choice == 3) {
                System.out.print("Withdraw amount: ");
                withdraw(index, scanner.nextInt());
            } else if (choice == 4) {
                System.out.println("Logging out...\n");
                break;
            } else System.out.println("Invalid choice.\n");
        }
    }

    static int findAccount(int accountNumber) {
        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i][0] == accountNumber) return i;
        }
        return -1;
    }

    static void checkBalance(int index) {
        System.out.println("Balance: " + accounts[index][1] + "€\n");
    }

    static void deposit(int index, int amount) {
        if (amount > 0) {
            accounts[index][1] += amount;
            System.out.println("Deposited " + amount + "€. New balance: " + accounts[index][1] + "€\n");
        } else System.out.println("Invalid amount.\n");
    }

    static void withdraw(int index, int amount) {
        if (amount > accounts[index][1]) {
            System.out.println("Insufficient balance.\n");
            return;
        }
        if (amount % 10 != 0) {
            System.out.println("ATM dispenses 10€, 20€, and 50€ bills only.\n");
            return;
        }

        int remaining = amount;
        int[] billsNeeded = new int[3];
        for (int i = 0; i < billValues.length; i++) {
            billsNeeded[i] = Math.min(remaining / billValues[i], atmCash[i]);
            remaining -= billsNeeded[i] * billValues[i];
        }
        if (remaining > 0) {
            System.out.println("Not enough bills in ATM.\n");
            return;
        }
        for (int i = 0; i < billValues.length; i++) atmCash[i] -= billsNeeded[i];

        accounts[index][1] -= amount;
        System.out.println("Withdrawal successful. New balance: " + accounts[index][1] + "€\n");
    }
}