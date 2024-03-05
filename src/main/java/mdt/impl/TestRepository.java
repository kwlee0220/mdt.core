package mdt.impl;

import java.nio.file.Paths;

import utils.func.FOption;

import mdt.harbor.HarborImpl;
import mdt.harbor.HarborProjectImpl;
import mdt.harbor.Repository;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class TestRepository {
	public static final void main(String... args) throws Exception {
		MDTConfig config = MDTConfig.load(Paths.get("mdt_configs.yaml"));
		HarborImpl harbor = HarborImpl.builder(config.getHarborConfig()).build();
		HarborProjectImpl prj = harbor.getMDTInstanceProject();
		
		FOption<Repository> repo;
		repo = prj.getRepository("busybox");
		System.out.println(repo);
		repo = prj.getRepository("xxx");
		System.out.println(repo);
	}
}
