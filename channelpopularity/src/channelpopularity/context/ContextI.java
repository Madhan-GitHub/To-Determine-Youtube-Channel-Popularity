package channelpopularity.context;

import java.util.Map;

/**
 * @author Madhan Thangavel
 *
 */

/**
 * Interface for channel context and its functionalities
 */
public interface ContextI {
	public void addVideo(String input, Map<String, Channel> finaloutput, TotalViews tv);

	public void removeVideo(String input, Map<String, Channel> finaloutput, TotalViews tv);

	public void metrics(String input, Map<String, Channel> finaloutput, TotalViews tv);

	public void adRequest(String input, Map<String, Channel> finaloutput);

	public void sendOuput(String output);
}
