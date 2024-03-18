package mdt.tool;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.stream.FStream;

import mdt.impl.MDTConfig;
import mdt.model.MDTInstance;
import mdt.model.MDTInstanceManager;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Ansi;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
@Command(name = "stop", description = "Stop a running MDT instance.")
public class StopMDTInstanceCommand extends MDTCommand {
	private static final Logger s_logger = LoggerFactory.getLogger(StopMDTInstanceCommand.class);
	
	@Parameters(index="0..*", paramLabel="ids", description="MDTInstance id list to start")
	private List<String> m_instanceId;
	
	@Option(names={"--all", "-a"}, description="start all stopped MDTInstances")
	private boolean m_startAll;

	public StopMDTInstanceCommand() {
		setLogger(s_logger);
	}

	@Override
	public void run(MDTConfig configs) throws Exception {
		MDTInstanceManager mgr = this.createMDTInstanceManager(configs);
		
		List<MDTInstance> instances;
		if ( m_startAll ) {
			instances = mgr.getInstanceAll().stream()
							.filter(MDTInstance::isRunning)
							.toList();
		}
		else {
			instances = FStream.from(m_instanceId)
								.mapOrIgnore(mgr::getInstance)
								.toList();
		}
		for ( MDTInstance inst: instances ) {
			try {
				inst.stop();
				System.out.printf("stopped instance: %s%n", inst.getId());
			}
			catch ( Exception e ) {
				System.out.printf("fails to stop instance: %s, cause=%s%n", inst.getId(), e);
			}
		}
	}

	public static final void main(String... args) throws Exception {
		StopMDTInstanceCommand cmd = new StopMDTInstanceCommand();

		CommandLine commandLine = new CommandLine(cmd).setUsageHelpWidth(100);
		try {
			commandLine.parse(args);

			if ( commandLine.isUsageHelpRequested() ) {
				commandLine.usage(System.out, Ansi.OFF);
			}
			else {
				cmd.run();
			}
		}
		catch ( Throwable e ) {
			System.err.println(e);
			commandLine.usage(System.out, Ansi.OFF);
		}
	}
}
