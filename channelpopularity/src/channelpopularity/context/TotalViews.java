package channelpopularity.context;

/**
 * @author Madhan Thangavel
 *
 */
public class TotalViews {
	private int viewsfinal;
	private int likesfinal;
	private int dislikesfinal;

	/**
	 * @return int
	 */
	public int getViewsfinal() {
		return viewsfinal;
	}

	/**
	 * @param viewsfinal
	 */
	public void setViewsfinal(int viewsfinal) {
		this.viewsfinal = viewsfinal;
	}

	/**
	 * @return int
	 */
	public int getLikesfinal() {
		return likesfinal;
	}

	/**
	 * @param likesfinal
	 */
	public void setLikesfinal(int likesfinal) {
		this.likesfinal = likesfinal;
	}

	/**
	 * @return int
	 */
	public int getDislikesfinal() {
		return dislikesfinal;
	}

	/**
	 * @param dislikesfinal
	 */
	public void setDislikesfinal(int dislikesfinal) {
		this.dislikesfinal = dislikesfinal;
	}

	/**
	 * Returns values from the constructor as type.
	 *
	 * @return String
	 */
	@Override
	public String toString() {
		return "TotalViews [viewsfinal=" + viewsfinal + ", likesfinal=" + likesfinal + ", dislikesfinal="
				+ dislikesfinal + "]";
	}
}
