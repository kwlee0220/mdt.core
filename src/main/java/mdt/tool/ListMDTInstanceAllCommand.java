package mdt.tool;

import java.util.List;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.stream.FStream;

import mdt.impl.MDTConfig;
import mdt.impl.MDTInstanceImpl;
import mdt.model.EnvironmentSummary;
import mdt.model.MDTInstanceManager;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Ansi;
import picocli.CommandLine.Option;

/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
@Command(name = "list", description = "list all MDTInstances.")
public class ListMDTInstanceAllCommand extends MDTCommand {
	private static final Logger s_logger = LoggerFactory.getLogger(ListMDTInstanceAllCommand.class);
	
	@Option(names={"--table", "-t"}, description="display instances in a table format.")
	private boolean m_tableFormat = false;
	
	public ListMDTInstanceAllCommand() {
		setLogger(s_logger);
	}

	@Override
	public void run(MDTConfig configs) throws Exception {
		MDTInstanceManager mgr = this.createMDTInstanceManager(configs);
		List<MDTInstanceImpl> instances = FStream.from(mgr.getInstanceAll())
												.castSafely(MDTInstanceImpl.class)
												.toList();
		
		if ( m_tableFormat ) {
			displayAsTable(instances);
		}
		else {
			displayWithoutTableFormat(instances);
		}
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
	
	private void displayWithoutTableFormat(List<MDTInstanceImpl> instances) {
		Table table = new Table(6, BorderStyle.BLANKS, ShownBorders.NONE);
		table.setColumnWidth(2, 20, 80);
		table.setColumnWidth(3, 20, 60);
		
		for ( MDTInstanceImpl inst : instances ) {
			table.addCell(inst.getId() + " | ");
			table.addCell(inst.getImageId() + " | ");
			
			EnvironmentSummary summary = inst.getEnvironmentSummary();
			String aasIdPair = summary.getAASId().getId();
			table.addCell(aasIdPair + " | ");
			table.addCell(FStream.from(summary.getSubmodelIdList())
								.map(pair -> pair.getIdShort())
								.join(", ") + " | ");
			table.addCell(inst.getStatus().toString() + " | ");
			
			String ep = inst.getServiceEndpoint();
			ep = (inst.getServiceEndpoint() != null) ? ep : "";
			table.addCell(ep + " | ");
		}
		System.out.println(table.render());
	}
	
	private void displayAsTable(List<MDTInstanceImpl> instances) {
		Table table = new Table(6);
		table.setColumnWidth(2, 20, 80);
		table.setColumnWidth(3, 20, 60);
		
		table.addCell(" INSTANCE ");
		table.addCell(" DOCKER_IMAGE ");
		table.addCell(" AAS_ID ");
		table.addCell(" SUB_MODELS ");
		table.addCell(" STATUS ");
		table.addCell(" ENDPOINT ");
		for ( MDTInstanceImpl inst : instances ) {
			table.addCell(inst.getId());
			table.addCell(inst.getImageId());
			
			EnvironmentSummary summary = inst.getEnvironmentSummary();
			table.addCell(summary.getAASId().getId());
			table.addCell(FStream.from(summary.getSubmodelIdList())
								.map(pair -> pair.getIdShort())
								.join(", "));
			table.addCell(inst.getStatus().toString());
			table.addCell(inst.getServiceEndpoint());
		}
		System.out.println(table.render());
	}
}
