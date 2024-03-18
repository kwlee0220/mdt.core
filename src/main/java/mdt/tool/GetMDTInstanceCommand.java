package mdt.tool;

import org.nocrala.tools.texttablefmt.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.stream.FStream;

import mdt.impl.MDTConfig;
import mdt.impl.MDTInstanceImpl;
import mdt.impl.MDTInstanceManagerImpl;
import mdt.model.EnvironmentSummary;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Ansi;
import picocli.CommandLine.Parameters;

/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
@Command(name = "get", description = "get MDTInstance runtime information.")
public class GetMDTInstanceCommand extends MDTCommand {
	private static final Logger s_logger = LoggerFactory.getLogger(GetMDTInstanceCommand.class);
	
	@Parameters(index="0", paramLabel="id", description="MDTInstance id to show")
	private String m_instanceId;
	
	public GetMDTInstanceCommand() {
		setLogger(s_logger);
	}

	@Override
	public void run(MDTConfig configs) throws Exception {
		MDTInstanceManagerImpl mgr = this.createMDTInstanceManager(configs);
		MDTInstanceImpl instance = mgr.getInstance(m_instanceId);
		EnvironmentSummary summary = instance.getEnvironmentSummary();
		
		Table table = new Table(2);

		table.addCell(" FIELD "); table.addCell(" VALUE");
		table.addCell(" INSTANCE "); table.addCell(" " + instance.getId());
		table.addCell(" DOCKER_IMAGE "); table.addCell(" " + instance.getImageId());
		table.addCell(" AAS_ID "); table.addCell(" " + summary.getAASId() + " ");
		FStream.from(summary.getSubmodelIdList())
				.zipWithIndex()
				.forEach(tup -> {
					table.addCell(String.format(" SUB_MODEL[%02d] ", tup._2));
					table.addCell(" " + tup._1 + " ");
				});
		table.addCell(" STATUS "); table.addCell(" " + instance.getStatus().toString());
		String epStr = instance.getServiceEndpoint();
		epStr = (epStr != null) ? instance.getServiceEndpoint() : "";
		table.addCell(" ENDPOINT "); table.addCell(" " + epStr);
		
		System.out.println(table.render());
	}

	public static final void main(String... args) throws Exception {
		GetMDTInstanceCommand cmd = new GetMDTInstanceCommand();

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
