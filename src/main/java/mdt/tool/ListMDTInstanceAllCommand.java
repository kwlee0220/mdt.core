package mdt.tool;

import java.util.List;

import org.nocrala.tools.texttablefmt.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.stream.FStream;

import mdt.impl.MDTConfig;
import mdt.model.MDTInstance;
import mdt.model.MDTInstanceManager;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Ansi;

/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
@Command(name = "list", description = "list all MDTInstances.")
public class ListMDTInstanceAllCommand extends MDTCommand {
	private static final Logger s_logger = LoggerFactory.getLogger(ListMDTInstanceAllCommand.class);
	
	public ListMDTInstanceAllCommand() {
		setLogger(s_logger);
	}

	@Override
	public void run(MDTConfig configs) throws Exception {
		MDTInstanceManager mgr = this.createMDTInstanceManager(configs);
		List<MDTInstance> instances = mgr.getInstanceAll();
		
		Table table = new Table(6);
		table.addCell(" INSTANCE ");
		table.addCell("  TAG  ");
		table.addCell(" AAS_ID ");
		table.addCell(" SUB_MODELS ");
		table.addCell(" STATUS ");
		table.addCell(" ENDPOINT ");
		for ( MDTInstance inst : instances ) {
			table.addCell(inst.getId());
			table.addCell(inst.getTag());
			table.addCell(inst.getAssId());
			table.addCell(FStream.from(inst.getSubmodelList()).join(", "));
			table.addCell(inst.getStatus().toString());
			table.addCell(inst.getServiceEndpoint());
		}
		System.out.println(table.render());
	}

	public static final void main(String... args) throws Exception {
		ListMDTInstanceAllCommand cmd = new ListMDTInstanceAllCommand();

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
