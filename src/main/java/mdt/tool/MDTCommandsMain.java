package mdt.tool;

import utils.UsageHelp;

import mdt.tool.MDTCommandsMain.MDTInstanceCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help;
import picocli.CommandLine.Help.Ansi;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
@Command(name="mdt",
		parameterListHeading = "Parameters:%n",
		optionListHeading = "Options:%n",
		description="Manufactoring DigitalTwin (MDT) related commands",
		subcommands = {
			MDTInstanceCommand.class,
		})
public class MDTCommandsMain implements Runnable {
	@Spec private CommandSpec m_spec;
	@Mixin private UsageHelp m_help;
	
	@Override
	public void run() {
		m_spec.commandLine().usage(System.out, Ansi.OFF);
	}

	public static final void main(String... args) throws Exception {
		MDTCommandsMain cmd = new MDTCommandsMain();
		CommandLine.run(cmd, System.out, System.err, Help.Ansi.OFF, args);
	}

	@Command(name="instance",
			description="MDT Instance related commands",
			subcommands= {
				FormatMDTInstanceStoreCommand.class,
				GetMDTInstanceCommand.class,
				ListMDTInstanceAllCommand.class,
				AddMDTInstanceCommand.class,
				RemoveMDTInstanceCommand.class,
				StartMDTInstanceCommand.class,
				StopMDTInstanceCommand.class,
			})
	public static class MDTInstanceCommand implements Runnable {
		@Spec private CommandSpec m_spec;
		@Mixin private UsageHelp m_help;
		
		@Override
		public void run() {
			m_spec.commandLine().usage(System.out, Ansi.OFF);
		}
	}
}
