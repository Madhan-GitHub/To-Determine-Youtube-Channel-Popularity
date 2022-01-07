package channelpopularity.state;

/**
 * @author Madhan Thangavel
 *
 */

/**
 * Interface for State and its functionalities
 */
public interface StateI {
	public StateName getState();

	public void setState(StateName state);

	public int getpopularityPercentage(int totalVideos, int views, int likes, int dislikes);
}
