package channelpopularity.state.factory;

import channelpopularity.state.StateI;
import channelpopularity.state.StateName;

/**
 * @author Madhan Thangavel
 *
 */

/**
 * Interface for Simple State Factory and its functionalities
 */
public interface SimpleStateFactoryI {
  public StateI create(StateName statename);
}
