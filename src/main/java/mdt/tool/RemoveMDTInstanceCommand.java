package mdt.tool;

import java.util.List;

import org.slf4j.Logger;

import utils.LoggerNameBuilder;

import mdt.harbor.HarborImageId;
import mdt.impl.MDTConfig;
import mdt.impl.MDTInstanceImpl;
import mdt.impl.MDTInstanceManagerImpl;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Ansi;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
@Command(name = "remove", description = "Unregister the MDT instance.")
public class RemoveMDTInstanceCommand extends MDTCommand {
	private static final Logger s_logger = LoggerNameBuilder.from(RemoveMDTInstanceCommand.class).dropSuffix(2)
															.append("unregister.mdt_instances").getLogger();
	
	@Parameters(index="0..*", paramLabel="id", description="MDTInstance id to unregister")
	private List<String> m_instanceId;
	
	@Option(names={"--all", "-a"}, description="remove all MDTInstances")
	private boolean m_removeAll;

	public RemoveMDTInstanceCommand() {
		setLogger(s_logger);
	}

	@Override
	public void run(MDTConfig configs) throws Exception {
		MDTInstanceManagerImpl mgr = this.createMDTInstanceManager(configs);
		
		if ( m_removeAll ) {
			mgr.removeInstanceAll();
		}
		else {
			for ( String instId: m_instanceId ) {
				mgr.removeInstance(instId);
			}
		}
	}

	public static final void main(String... args) throws Exception {
		RemoveMDTInstanceCommand cmd = new RemoveMDTInstanceCommand();

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
