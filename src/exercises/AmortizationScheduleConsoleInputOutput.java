/**
 * 
 */
package exercises;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.IllegalFormatException;

import exercises.beans.AmortizationScheduleInput;
import exercises.util.AmortizationHelper;

/**
 * Class to read Input required for amortization Schedule
 * 
 */
public class AmortizationScheduleConsoleInputOutput {

	public AmortizationScheduleInput readUserInputFromConsole() {
		String[] userPrompts = {
				"Please enter the amount you would like to borrow: ",
				"Please enter the annual percentage rate used to repay the loan: ",
				"Please enter the term, in years, over which the loan is repaid: " };

		String line = "";
		AmortizationScheduleInput input = new AmortizationScheduleInput();
		double amount = 0;
		double apr = 0;
		int years = 0;

		for (int i = 0; i < userPrompts.length;) {
			String userPrompt = userPrompts[i];
			try {
				line = ConsoleInputOutput.readLine(userPrompt);
			} catch (IOException e) {
				ConsoleInputOutput
						.print("An IOException was encountered. Terminating program.\n");
				return input;
			}

			boolean isValidValue = true;
			try {
				switch (i) {
				case 0:
					amount = Double.parseDouble(line);
					if (AmortizationHelper.isValidBorrowAmount(amount) == false) {
						isValidValue = false;
						double range[] = AmortizationHelper.getBorrowAmountRange();
						ConsoleInputOutput
								.print("Please enter a positive value between "
										+ range[0] + " and " + range[1] + ". ");
					}else{
						input.setAmount(amount);
					}
					break;
				case 1:
					apr = Double.parseDouble(line);
					if (AmortizationHelper.isValidAPRValue(apr) == false) {
						isValidValue = false;
						double range[] = AmortizationHelper.getAPRRange();
						ConsoleInputOutput
								.print("Please enter a positive value between "
										+ range[0] + " and " + range[1] + ". ");
					}else{
						input.setApr(apr);
					}
					break;
				case 2:
					years = Integer.parseInt(line);
					if (AmortizationHelper.isValidTerm(years) == false) {
						isValidValue = false;
						int range[] = AmortizationHelper.getTermRange();
						ConsoleInputOutput
								.print("Please enter a positive integer value between "
										+ range[0] + " and " + range[1] + ". ");
					}else{
						input.setYears(years);
					}
					break;
				}
			} catch (NumberFormatException e) {
				isValidValue = false;
			}
			if (isValidValue) {
				i++;
			} else {
				ConsoleInputOutput.print("An invalid value was entered.\n");
			}
		}
		return input;

	}
}
