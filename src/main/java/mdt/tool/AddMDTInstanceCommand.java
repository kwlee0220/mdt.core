package mdt.tool;

import java.time.Duration;

import org.slf4j.Logger;

import utils.LoggerNameBuilder;
import utils.UnitUtils;
import utils.func.FOption;

import mdt.impl.MDTConfig;
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
@Command(name = "add", description = "Register an MDT instance.")
public class AddMDTInstanceCommand extends MDTCommand {
	private static final Logger s_logger = LoggerNameBuilder.from(AddMDTInstanceCommand.class).dropSuffix(2)
															.append("register.mdt_instances").getLogger();

	@Parameters(index="0", paramLabel="repo:tag", description="tagged repository name to register")
	private String m_imageName;
	
	@Option(names={"--aas"}, paramLabel="path", required=true, description="AAS Json file path")
	private String m_aasFile;
	
	@Option(names={"--force"}, description="force to add the instance even if the same instance exists.")
	private boolean m_force = false;
	
	@Option(names={"--timeout"}, paramLabel="seconds", required=false, description="registration timeout")
	private String m_timeout = null;

	public AddMDTInstanceCommand() {
		setLogger(s_logger);
	}

	@Override
	public void run(MDTConfig configs) throws Exception {
		MDTInstanceManager mgr = this.createMDTInstanceManager(configs);
		
		FOption<Duration> timeout = FOption.ofNullable(m_timeout).map(to -> UnitUtils.parseDuration(to));
		mgr.addInstance(m_imageName, m_aasFile, m_force, timeout);
	}

	public static final void main(String... args) throws Exception {
		AddMDTInstanceCommand cmd = new AddMDTInstanceCommand();

		CommandLine commandLine = new CommandLine(cmd).setUsageHelpWidth(100);
		try {
			commandLine.parse(args);

			if ( commandLine.isUsageHelpRequested() ) {
				commandLine.usage(System.out, Ansi.OFF);
			}
			else {
				try {
					cmd.run();
				}
				catch ( Exception e ) {
					System.err.println(e);
				}
			}
		}
		catch ( Throwable e ) {
			System.err.println(e);
			commandLine.usage(System.out, Ansi.OFF);
		}
	}
}
