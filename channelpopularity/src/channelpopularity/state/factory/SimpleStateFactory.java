package channelpopularity.state.factory;

import channelpopularity.state.StateI;
import channelpopularity.state.StateName;
import channelpopularity.state.States;

/**
 * @author Madhan Thangavel
 *
 */
public class SimpleStateFactory implements SimpleStateFactoryI {
	private StateI stateI;

	/**
	 * To instantiate states for Simple State factory
	 * 
	 * @param statename
	 * @return object stateI
	 */
	@Override
	public StateI create(StateName statename) {
		stateI = new States();
		stateI.setState(statename);
		return stateI;
	}

	/**
	 * To covert the object to string.
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		return "SimpleStateFactory [stateI=" + stateI + "]";
	}
}
