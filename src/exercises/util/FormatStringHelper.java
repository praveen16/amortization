/**
 * 
 */
package exercises.util;

/**
 * All things related to format
 *
 */
public class FormatStringHelper {

	private String headerFormatString = "%1$-20s%2$-20s%3$-20s%4$-20s%5$-20s%6$-20s\n";
	// output is in dollars
	private String lineItemFormatString = "%1$-20d%2$-20.2f%3$-20.2f%4$-20.2f%5$-20.2f%6$-20.2f\n";
	
	
	/**
	 * @return the headerFormat
	 */
	public String getHeaderFormatString() {
		return headerFormatString;
	}
	/**
	 * @param headerFormat the headerFormat to set
	 */
	public void setHeaderFormatString(String headerFormatString) {
		this.headerFormatString = headerFormatString;
	}
	/**
	 * @return the lineItemFormat
	 */
	public String getLineItemFormatString() {
		return lineItemFormatString;
	}
	/**
	 * @param lineItemFormat the lineItemFormat to set
	 */
	public void setLineItemFormatString(String lineItemFormatString) {
		this.lineItemFormatString = lineItemFormatString;
	}
}
