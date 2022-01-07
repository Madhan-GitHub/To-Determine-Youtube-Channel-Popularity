package channelpopularity.state;

/**
 * @author Madhan Thangavel
 *
 */
public class States extends AbstractState {
  private StateName state;

  /**
   * converts the object to string
   *
   * @return String
   */
  @Override
  public String toString() {
    return "States [state=" + state + "]";
  }

  /**
   * @return StateName
   */
  @Override
  public StateName getState() {
    return state;
  }

  /**
   * @param state
   */
  @Override
  public void setState(StateName state) {
    this.state = state;
  }

  /**
   * To calculate the popularity of the channel by using the formulae
   * 
   * @param totalVideos
   * @param views
   * @param likes
   * @param dislikes
   * @return int popPercent
   */
  @Override
  public int getpopularityPercentage(int totalVideos, int views, int likes, int dislikes) {
    int popPercent = ((views) + 2 * (likes - dislikes)) / totalVideos;
    return popPercent;
  }
}
