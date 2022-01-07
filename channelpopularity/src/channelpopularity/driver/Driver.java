package channelpopularity.driver;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import channelpopularity.context.Channel;
import channelpopularity.context.ChannelContext;
import channelpopularity.context.ChannelPopularityException;
import channelpopularity.context.TotalViews;
import channelpopularity.operation.Operation;
import channelpopularity.state.StateName;
import channelpopularity.state.factory.SimpleStateFactory;
import channelpopularity.state.factory.SimpleStateFactoryI;
import channelpopularity.util.FileProcessor;
import channelpopularity.util.Results;
import channelpopularity.util.StdoutDisplayInterface;

/**
 * @author John Doe
 *
 */
public class Driver {
	private static final int REQUIRED_NUMBER_OF_CMDLINE_ARGS = 2;

	/**
	 * Driver code to determine the popularity of the channel.
	 * 
	 * @return String The processed line read from the input file.
	 * @exception Exception On error encountered when handling the input.
	 */
	public static void main(String[] args) throws Exception {

		/*
		 * As the build.xml specifies the arguments as input,output or metrics, in case
		 * the argument value is not given java takes the default value specified in
		 * build.xml. To avoid that, below condition is used
		 */
		if ((args.length != 2) || (args[0].equals("${input}")) || (args[1].equals("${output}"))) {
			System.err.printf("Error: Incorrect number of arguments. Program accepts %d arguments.",
					REQUIRED_NUMBER_OF_CMDLINE_ARGS);
			System.exit(0);
		} else {
			StdoutDisplayInterface sdi = new Results();
			try {
				if (null != args[0]) {
					Map<String, Channel> finaloutput = new HashMap<String, Channel>();
					TotalViews tv = new TotalViews();
					SimpleStateFactoryI stateFactoryIn = new SimpleStateFactory();
					List<StateName> stateNames = Arrays.asList(StateName.values());
					ChannelContext context = new ChannelContext(stateFactoryIn, stateNames);

					String pattern = "[A-Z]{3}\\S[A-Z]{5}\\S{2}([\\x00-\\x7FF]+)|[A-Z]{7}\\S{2}([\\x00-\\x7FF]+)\\S{2}\\S[A-Z]{5}\\S(-?\\d*)\\S[A-Z]{5}\\S(-?\\d*)\\S[A-Z]{8}\\S(-?\\d*)\\S|[A-Z]{2}\\S[A-Z]{7}\\S{2}([\\x00-\\x7FF]+)\\S{2}[A-Z]{3}\\S(-?\\d*)|[A-Z]{6}\\S[A-Z]{5}\\S{2}([\\x00-\\x7FF]+)";
					Pattern p = Pattern.compile(pattern);

					FileProcessor fp = new FileProcessor(args[0]);
					String line;
					if (new File(args[0]).length() > 0) {
						while ((line = fp.poll()) != null) {
							Matcher m = p.matcher(line);
							boolean b = m.matches();
							if (line.trim().isEmpty()) {
								String msg = "Input file has blank line.";
								sdi.display(msg);
							}
							if (b) {
								if (line.contains(Operation.ADD_VIDEO.toString())) {
									context.addVideo(line, finaloutput, tv);
								}
								if (line.contains(Operation.REMOVE_VIDEO.toString())) {
									context.removeVideo(line, finaloutput, tv);
								}
								if (line.contains(Operation.AD_REQUEST.toString())) {
									context.adRequest(line, finaloutput);
								}
								if (line.contains(Operation.METRICS.toString())) {
									context.metrics(line, finaloutput, tv);
								}
							} else {
								String msg = "Input line " + line
										+ " is Invalid. So considering entered input file as invalid";
								sdi.display(msg);
								System.exit(0);
							}
						}
					} else {
						String msg = "Input File is either empty or blank. Terminating code....";
						ChannelPopularityException ex = new ChannelPopularityException(msg);
						throw ex;
					}

					if (null != args[1]) {
						context.sendOuput(args[1]);
					} else {
						String msg = "Missing ouput file for the argument";
						ChannelPopularityException ex = new ChannelPopularityException(msg);
						throw ex;
					}
				} else {
					String msg = "Missing input file for the argument";
					ChannelPopularityException ex = new ChannelPopularityException(msg);
					throw ex;
				}
			} catch (ChannelPopularityException ex) {
				sdi.display(ex.getMessage());
			} catch (InvalidPathException | SecurityException | IOException ex) {
				sdi.display(ex.getMessage());
			}
		}
	}
}
