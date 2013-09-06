//
// Exercise Details:
// Build an amortization schedule program using Java. 
// 
// The program should prompt the user for
//		the amount he or she is borrowing,
//		the annual percentage rate used to repay the loan,
//		the term, in years, over which the loan is repaid.  
// 
// The output should include:
//		The first column identifies the payment number.
//		The second column contains the amount of the payment.
//		The third column shows the amount paid to interest.
//		The fourth column has the current balance.  The total payment amount and the interest paid fields.
// 
// Use appropriate variable names and comments.  You choose how to display the output (i.e. Web, console).  
// Amortization Formula
// This will get you your monthly payment.  Will need to update to Java.
// M = P * (J / (1 - (Math.pow(1/(1 + J), N))));
// 
// Where:
// P = Principal
// I = Interest
// J = Monthly Interest in decimal form:  I / (12 * 100)
// N = Number of months of loan
// M = Monthly Payment Amount
// 
// To create the amortization table, create a loop in your program and follow these steps:
// 1.      Calculate H = P x J, this is your current monthly interest
// 2.      Calculate C = M - H, this is your monthly payment minus your monthly interest, so it is the amount of principal you pay for that month
// 3.      Calculate Q = P - C, this is the new balance of your principal of your loan.
// 4.      Set P equal to Q and go back to Step 1: You thusly loop around until the value Q (and hence P) goes to zero.
// 

package exercises;

import java.io.Console;
import java.lang.Math;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.NumberFormatException;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;
import java.lang.IllegalArgumentException;

import exercises.beans.AmortizationScheduleInput;
import exercises.beans.AmortizationScheduleLineItem;
import exercises.util.AmortizationHelper;
import exercises.util.FormatStringHelper;

/**
 * @author jNut
 *
 */
public class AmortizationSchedule {

	private long amountBorrowed = 0;		// in cents
	private double apr = 0d;
	private int initialTermMonths = 0;
	
	private final double monthlyInterestDivisor = 12d * 100d;
	private double monthlyInterest = 0d;
	private long monthlyPaymentAmount = 0;	// in cents


	
	private long calculateMonthlyPayment() {
		// M = P * (J / (1 - (Math.pow(1/(1 + J), N))));
		//
		// Where:
		// P = Principal
		// I = Interest
		// J = Monthly Interest in decimal form:  I / (12 * 100)
		// N = Number of months of loan
		// M = Monthly Payment Amount
		// 
		
		// calculate J
		monthlyInterest = apr / monthlyInterestDivisor;
		
		// this is 1 / (1 + J)
		double tmp = Math.pow(1d + monthlyInterest, -1);
		
		// this is Math.pow(1/(1 + J), N)
		tmp = Math.pow(tmp, initialTermMonths);
		
		// this is 1 / (1 - (Math.pow(1/(1 + J), N))))
		tmp = Math.pow(1d - tmp, -1);
		
		// M = P * (J / (1 - (Math.pow(1/(1 + J), N))));
		double rc = amountBorrowed * monthlyInterest * tmp;
		
		return Math.round(rc);
	}
	
	// The output should include:
	//	The first column identifies the payment number.
	//	The second column contains the amount of the payment.
	//	The third column shows the amount paid to interest.
	//	The fourth column has the current balance.  The total payment amount and the interest paid fields.
	public void outputAmortizationSchedule(List<AmortizationScheduleLineItem> lineItems, String headerFormatString, String lineItemFormatString) {
		

		
		ConsoleInputOutput.printf(headerFormatString,
				"PaymentNumber", "PaymentAmount", "PaymentInterest",
				"CurrentBalance", "TotalPayments", "TotalInterestPaid");
		
		long balance = amountBorrowed;
		int paymentNumber = 0;
		long totalPayments = 0;
		long totalInterestPaid = 0;
		
		
		
		ConsoleInputOutput.printf(lineItemFormatString, paymentNumber++, 0d, 0d,
				((double) amountBorrowed) / 100d,
				((double) totalPayments) / 100d,
				((double) totalInterestPaid) / 100d);
		
		final int maxNumberOfPayments = initialTermMonths + 1;
		while ((balance > 0) && (paymentNumber <= maxNumberOfPayments)) {
			// Calculate H = P x J, this is your current monthly interest
			//long curMonthlyInterest = Math.round(((double) balance) * monthlyInterest);
			long curMonthlyInterest = Math.round(((double) balance) * monthlyInterest);

			// the amount required to payoff the loan
			long curPayoffAmount = balance + curMonthlyInterest;
			
			// the amount to payoff the remaining balance may be less than the calculated monthlyPaymentAmount
			long curMonthlyPaymentAmount = Math.min(monthlyPaymentAmount, curPayoffAmount);
			
			// it's possible that the calculated monthlyPaymentAmount is 0,
			// or the monthly payment only covers the interest payment - i.e. no principal
			// so the last payment needs to payoff the loan
			if ((paymentNumber == maxNumberOfPayments) &&
					((curMonthlyPaymentAmount == 0) || (curMonthlyPaymentAmount == curMonthlyInterest))) {
				curMonthlyPaymentAmount = curPayoffAmount;
			}
			
			// Calculate C = M - H, this is your monthly payment minus your monthly interest,
			// so it is the amount of principal you pay for that month
			long curMonthlyPrincipalPaid = curMonthlyPaymentAmount - curMonthlyInterest;
			
			// Calculate Q = P - C, this is the new balance of your principal of your loan.
			long curBalance = balance - curMonthlyPrincipalPaid;
			
			totalPayments += curMonthlyPaymentAmount;
			totalInterestPaid += curMonthlyInterest;
			
			// output is in dollars
			ConsoleInputOutput.printf(lineItemFormatString, paymentNumber++,
					((double) curMonthlyPaymentAmount) / 100d,
					((double) curMonthlyInterest) / 100d,
					((double) curBalance) / 100d,
					((double) totalPayments) / 100d,
					((double) totalInterestPaid) / 100d);
						
			// Set P equal to Q and go back to Step 1: You thusly loop around until the value Q (and hence P) goes to zero.
			balance = curBalance;
		}
	}
	
	public AmortizationSchedule(AmortizationScheduleInput input) throws IllegalArgumentException {

		if (validate(input)) {
			throw new IllegalArgumentException();
		}

		amountBorrowed = Math.round(input.getAmount() * 100);
		apr = input.getApr();
		initialTermMonths = input.getYears() * 12;
		
		monthlyPaymentAmount = calculateMonthlyPayment();
		
		// the following shouldn't happen with the available valid ranges
		// for borrow amount, apr, and term; however, without range validation,
		// monthlyPaymentAmount as calculated by calculateMonthlyPayment()
		// may yield incorrect values with extreme input values
		if (monthlyPaymentAmount > amountBorrowed) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Validate input to constructor
	 * @param amount
	 * @param interestRate
	 * @param years
	 * @return
	 */
	private boolean validate(AmortizationScheduleInput input) {
		return (AmortizationHelper.isValidBorrowAmount(input.getAmount()) == false) ||
				(AmortizationHelper.isValidAPRValue(input.getApr()) == false) ||
				(AmortizationHelper.isValidTerm(input.getYears()) == false);
	}	
	
	// 
	public static void main(String [] args) {
		
		//Read Input
		AmortizationScheduleInput input = new AmortizationScheduleConsoleInputOutput().readUserInputFromConsole();

		//Generate AmortizationSchedule
		try {	
			//Init amortizationschedule
			AmortizationSchedule as = new AmortizationSchedule(input);
			//calculate amortization Schedule
			List<AmortizationScheduleLineItem> lineItems =  as.calculateAmortizationSchedule();
			//now output the schedule
			FormatStringHelper fsHelper = new FormatStringHelper();
			as.outputAmortizationSchedule(lineItems, fsHelper.getHeaderFormatString(), fsHelper.getLineItemFormatString());
		} catch (IllegalArgumentException e) {
			ConsoleInputOutput.print("Unable to process the values entered. Terminating program.\n");
		}
	}

	/**
	 * 	// To create the amortization table, create a loop in your program and follow these steps:
	*	// 1.      Calculate H = P x J, this is your current monthly interest
	*	// 2.      Calculate C = M - H, this is your monthly payment minus your monthly interest, so it is the amount of principal you pay for that month
	*	// 3.      Calculate Q = P - C, this is the new balance of your principal of your loan.
	*	// 4.      Set P equal to Q and go back to Step 1: You thusly loop around until the value Q (and hence P) goes to zero.
	*	// 
	 *  
	 * @return
	 */
	private List<AmortizationScheduleLineItem> calculateAmortizationSchedule() {
		
		List<AmortizationScheduleLineItem> lineItems = new ArrayList<AmortizationScheduleLineItem>();
		// TODO Move code to calculateAmortizationSchedule
		
		return lineItems;
	}
}

