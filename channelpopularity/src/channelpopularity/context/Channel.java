package channelpopularity.context;

/**
 * @author Madhan Thangavel
 *
 */
public class Channel {

	private String videoName;
	private int popularityScore;
	private int views;
	private int likes;
	private int dislikes;

	/**
	 * Channel constructor
	 * 
	 * @param String videoname
	 * @param int    views
	 * @param int    likes
	 * @param int    dislikes
	 */
	public Channel(String videoName, int views, int likes, int dislikes) {
		this.videoName = videoName;
		this.views = views;
		this.likes = likes;
		this.dislikes = dislikes;
	}

	/**
	 * @return int
	 */
	public int getViews() {
		return views;
	}

	/**
	 * @param views
	 */
	public void setViews(int views) {
		this.views = views;
	}

	/**
	 * @return int
	 */
	public int getLikes() {
		return likes;
	}

	/**
	 * @param likes
	 */
	public void setLikes(int likes) {
		this.likes = likes;
	}

	/**
	 * @return int
	 */
	public int getDislikes() {
		return dislikes;
	}

	/**
	 * @param dislikes
	 */
	public void setDislikes(int dislikes) {
		this.dislikes = dislikes;
	}

	/**
	 * @return String
	 */
	public String getVideoName() {
		return videoName;
	}

	/**
	 * @param videoName
	 */
	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	/**
	 * @return int
	 */
	public int getPopularityScore() {
		return popularityScore;
	}

	/**
	 * @param popularityScore
	 */
	public void setPopularityScore(int popularityScore) {
		this.popularityScore = popularityScore;
	}

	/**
	 * Returns values from the constructor.
	 *
	 * @return String
	 */
	@Override
	public String toString() {
		return "Channel [videoName=" + videoName + ", popularityScore=" + popularityScore + ", views=" + views
				+ ", likes=" + likes + ", dislikes=" + dislikes + "]";
	}
}
