package mdt.tool;

import java.sql.Connection;

import org.slf4j.Logger;

import utils.LoggerNameBuilder;
import utils.jdbc.JdbcProcessor;

import mdt.impl.MDTConfig;
import mdt.impl.MDTInstanceStore;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Ansi;


/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
@Command(name = "format",
		description = "format mdt_instances table.")
public class FormatMDTInstanceStoreCommand extends MDTCommand {
	private static final Logger s_logger = LoggerNameBuilder.from(FormatMDTInstanceStoreCommand.class)
																.dropSuffix(2)
																.append("format.mdt_instances")
																.getLogger();
	
	public FormatMDTInstanceStoreCommand() {
		setLogger(s_logger);
	}
	
	@Override
	public void run(MDTConfig configs) throws Exception {
		JdbcProcessor jdbc = configs.newJdbcProcessor();
		try ( Connection conn = jdbc.connect() ) {
			MDTInstanceStore.dropTable(conn);
			MDTInstanceStore.createTable(conn);
		}
	}
	
	public static final void main(String... args) throws Exception {
		FormatMDTInstanceStoreCommand cmd = new FormatMDTInstanceStoreCommand();
		
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
