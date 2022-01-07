package channelpopularity.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import channelpopularity.operation.Operation;
import channelpopularity.state.StateI;
import channelpopularity.state.StateName;
import channelpopularity.state.States;
import channelpopularity.state.factory.SimpleStateFactoryI;
import channelpopularity.util.Results;

/**
 * @author Madhan Thangavel
 *
 */
public class ChannelContext implements ContextI {
	private int popularityScore = 0;
	private StateI curState;
	private Map<StateName, StateI> availableStates;
	Results sdi = new Results();
	List<String> ouputList = new ArrayList<String>();

	/**
	 * To create current state popularity.
	 *
	 * @param stateFactoryIn
	 * @param stateNames
	 */
	public ChannelContext(SimpleStateFactoryI stateFactoryIn, List<StateName> stateNames) {
		availableStates = new HashMap<StateName, StateI>();
		for (StateName statename : stateNames) {
			availableStates.put(statename, stateFactoryIn.create(statename));
		}
		curState = stateFactoryIn.create(StateName.UNPOPULAR);
	}

	/**
	 * To set current state popularity.
	 *
	 * @param nextState
	 */
	public void setCurrentState(StateName nextState) {
		if (availableStates.containsKey(nextState)) {
			curState = availableStates.get(nextState);
		}
	}

	/**
	 * To add video to the channel context
	 *
	 * @param input
	 * @param finaloutput
	 * @param tv
	 * @exception ChannelPopularityException
	 */
	@Override
	public void addVideo(String input, Map<String, Channel> finaloutput, TotalViews tv) {
		try {
			String[] splittingInput = input.split("::");
			String videoName = splittingInput[1];
			if (finaloutput.containsKey(videoName)) {
				String msg = "Video Already Exists, cannot be added";
				ChannelPopularityException ex = new ChannelPopularityException(msg);
				throw ex;
			} else {
				Channel ch = new Channel(videoName, 0, 0, 0);
				finaloutput.put(videoName, ch);
				StateI stateI = new States();
				popularityScore = stateI.getpopularityPercentage(finaloutput.size(), tv.getViewsfinal(),
						tv.getLikesfinal(), tv.getDislikesfinal());
				String result = curState.getState().name() + "_" + Operation.VIDEO_ADDED + "::" + videoName;
				getStateName(popularityScore);
				ouputList.add(result);
			}
		} catch (ChannelPopularityException ex) {
			sdi.display(ex.getMessage());
		}
	}

	/**
	 * To remove video from the channel context.
	 *
	 * @param input
	 * @param finaloutput
	 * @param tv
	 * @exception ChannelPopularityException
	 */
	@Override
	public void removeVideo(String input, Map<String, Channel> finaloutput, TotalViews tv) {
		try {
			String[] splittingInput = input.split("::");
			String videoName = splittingInput[1];
			if (finaloutput.containsKey(videoName)) {
				Channel ch1 = finaloutput.get(videoName);
				StateI stateI = new States();
				tv.setViewsfinal(tv.getViewsfinal() - ch1.getViews());
				tv.setLikesfinal(tv.getLikesfinal() - ch1.getLikes());
				tv.setDislikesfinal(tv.getDislikesfinal() - ch1.getDislikes());
				finaloutput.remove(videoName);
				if (finaloutput.size() != 0)
					popularityScore = stateI.getpopularityPercentage(finaloutput.size(), tv.getViewsfinal(),
							tv.getLikesfinal(), tv.getDislikesfinal());
				String result = curState.getState().name() + "_" + Operation.VIDEO_REMOVED + "::" + videoName;
				getStateName(popularityScore);
				ouputList.add(result);

			} else {
				String msg = "Video Does not Exists, cannot be removed";
				ChannelPopularityException ex = new ChannelPopularityException(msg);
				throw ex;
			}
		} catch (ChannelPopularityException ex) {
			sdi.display(ex.getMessage());
		}
	}

	/**
	 * To calculate metrics of the channel(popuarity)
	 *
	 * @param input
	 * @param finaloutput
	 * @param tv
	 * @exception ChannelPopularityException
	 */
	@Override
	public void metrics(String input, Map<String, Channel> finaloutput, TotalViews tv) {
		Map<String, Integer> metricsMap = new HashMap<String, Integer>();
		try {
			int views = 0;
			int likes = 0;
			int dislikes = 0;
			int viewsfinal, likesfinal, dislikesfinal = 0;
			String[] splittingInput = input.split("::");
			String key = splittingInput[0];
			String value = splittingInput[1];

			String[] splittingKey = key.split("__");
			String videoName = splittingKey[1];

			String regx = "[]";
			char[] ca = regx.toCharArray();
			for (char c : ca) {
				value = value.replace("" + c, "");
			}
			String[] splittingValue = value.split(",");
			for (String spValue : splittingValue) {
				String[] finalSplit = spValue.split("=");
				metricsMap.put(finalSplit[0], Integer.parseInt(finalSplit[1]));
			}

			if (finaloutput.containsKey(videoName)) {
				Channel ch1 = finaloutput.get(videoName);
				views = ch1.getViews() + metricsMap.get("VIEWS");
				likes = ch1.getLikes() + metricsMap.get("LIKES");
				dislikes = ch1.getDislikes() + metricsMap.get("DISLIKES");
				Channel ch = new Channel(videoName, views, likes, dislikes);
				finaloutput.put(videoName, ch);

				if (metricsMap.get("VIEWS") >= 0) {
					viewsfinal = tv.getViewsfinal() + metricsMap.get("VIEWS");
					tv.setViewsfinal(viewsfinal);
				} else {
					String msg = "Negative value for number of views in an input line";
					ChannelPopularityException ex = new ChannelPopularityException(msg);
					throw ex;
				}
				if ((tv.getLikesfinal() + metricsMap.get("LIKES")) > 0) {
					likesfinal = tv.getLikesfinal() + metricsMap.get("LIKES");
					tv.setLikesfinal(likesfinal);
				} else {
					String msg = "Decrease in likes is more than the total number of likes";
					ChannelPopularityException ex = new ChannelPopularityException(msg);
					throw ex;
				}
				if ((tv.getDislikesfinal() + metricsMap.get("DISLIKES")) > 0) {
					dislikesfinal = tv.getDislikesfinal() + metricsMap.get("DISLIKES");
					tv.setDislikesfinal(dislikesfinal);
				} else {
					String msg = "Decrease in dislikes is more than the total number of dislikes";
					ChannelPopularityException ex = new ChannelPopularityException(msg);
					throw ex;
				}

				StateI stateI = new States();
				popularityScore = stateI.getpopularityPercentage(finaloutput.size(), viewsfinal, likesfinal,
						dislikesfinal);
				String result = curState.getState().name() + "_" + Operation.POPULARITY_SCORE_UPDATE + "::"
						+ popularityScore;
				getStateName(popularityScore);
				ouputList.add(result);

			} else {
				String msg = "Video Does Not Exists, Metrics cannot be computed";
				ChannelPopularityException ex = new ChannelPopularityException(msg);
				throw ex;
			}
		} catch (ChannelPopularityException ex) {
			sdi.display(ex.getMessage());
		}
	}

	/**
	 * To process request for advertisements in channel context.
	 *
	 * @param input
	 * @param finaloutput
	 * @exception ChannelPopularityException
	 */
	@Override
	public void adRequest(String input, Map<String, Channel> finaloutput) {
		try {
			String[] splittingInput = input.split("::");
			String key = splittingInput[0];
			String value = splittingInput[1];

			String[] splittingKey = key.split("__");
			String videoName = splittingKey[1];

			String[] splittingValue = value.split("=");
			int len = Integer.parseInt(splittingValue[1]);

			if (finaloutput.containsKey(videoName)) {
				switch (curState.getState()) {
					case UNPOPULAR:
						if (len > 1 && len <= 10) {
							String result = curState.getState().name() + "_" + Operation.AD_REQUEST + "::"
									+ Operation.APPROVED;
							ouputList.add(result);
						} else {
							String result = curState.getState().name() + "_" + Operation.AD_REQUEST + "::"
									+ Operation.REJECTED;
							ouputList.add(result);
						}
						break;
					case HIGHLY_POPULAR:
						if (len > 1 && len <= 30) {
							String result = curState.getState().name() + "_" + Operation.AD_REQUEST + "::"
									+ Operation.APPROVED;
							ouputList.add(result);
						} else {
							String result = curState.getState().name() + "_" + Operation.AD_REQUEST + "::"
									+ Operation.REJECTED;
							ouputList.add(result);
						}
						break;
					case MILDLY_POPULAR:
						if (len > 1 && len <= 20) {
							String result = curState.getState().name() + "_" + Operation.AD_REQUEST + "::"
									+ Operation.APPROVED;
							ouputList.add(result);
						} else {
							String result = curState.getState().name() + "_" + Operation.AD_REQUEST + "::"
									+ Operation.REJECTED;
							ouputList.add(result);
						}
						break;
					case ULTRA_POPULAR:
						if (len > 1 && len <= 40) {
							String result = curState.getState().name() + "_" + Operation.AD_REQUEST + "::"
									+ Operation.APPROVED;
							ouputList.add(result);
						} else {
							String result = curState.getState().name() + "_" + Operation.AD_REQUEST + "::"
									+ Operation.REJECTED;
							ouputList.add(result);
						}
						break;
					default:
						break;
				}

			} else {
				String msg = "Ad-Video request Does Not Exists";
				ChannelPopularityException ex = new ChannelPopularityException(msg);
				throw ex;
			}
		} catch (ChannelPopularityException ex) {
			sdi.display(ex.getMessage());
		}
	}

	/**
	 * To get the popularity channel context.
	 *
	 * @param popularityScore
	 */
	public void getStateName(int popularityScore) {

		if (popularityScore >= 0 && popularityScore <= 1000) {
			setCurrentState(StateName.UNPOPULAR);
		} else if (popularityScore > 1000 && popularityScore <= 10000) {
			setCurrentState(StateName.MILDLY_POPULAR);
		} else if (popularityScore > 10000 && popularityScore <= 100000) {
			setCurrentState(StateName.HIGHLY_POPULAR);
		} else if (popularityScore > 100000 && popularityScore <= Integer.MAX_VALUE) {
			setCurrentState(StateName.ULTRA_POPULAR);
		}
	}

	/**
	 * To store the result in output file
	 *
	 * @param outputPath
	 */
	@Override
	public void sendOuput(String outputPath) {
		if (ouputList.size() > 0) {
			sdi.display(ouputList);
			sdi.writeFileTo(ouputList, outputPath);
		}
	}

	/**
	 * To String method to return the popularity score.
	 *
	 * @return String popularityScore of the channel
	 */
	@Override
	public String toString() {
		return "ChannelContext [popularityScore=" + popularityScore + "]";
	}

}
