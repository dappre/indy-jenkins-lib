#!/usr/bin/env groovy

package nl.digitalme.indy

class plConfig implements Serializable {
	// Project name
	protected String name;
	// Flag for the extended lib support
	protected Boolean extended = false;
	// Stages
	protected Map st = [
		validate:	true,
		compile:	true,
		test:		true,
		package:	true,
		approve:	true,
		release:	true,
		deliver:	true,
		notify:		true,
	];
	protected Map lb = [
		docker:		'doker',
	];
	// Other options
	protected Integer verbose	= 1;
	protected Boolean dryRun	= false;
	protected Boolean failfast	= false;
	protected Boolean pkgDeps	= true;
	protected String branch		= 'master';
	
	public plConfig(String n, Map p, String b) {
		name		= n;
		extended 	= extended;
		st = [
			validate:	!params.skipValidate,
			compile:	!params.skipCompile,
			test:		!params.skipTest,
			package:	!params.skipPackage,
			approve:	!params.skipApprove,
			release:	!params.skipRelease,
			deliver:	!params.skipDeliver,
			notify:		!params.skipNotify,
		];
		lb = [
			docker:		params.lbDocker,
		];
//		if (params.verbose.isInteger()) {
			verbose		= params.verbose as Integer;
//		}
		dryRun		= params.dryRun;
		failfast	= params.failFast;
		pkgDeps		= params.pkgDeps;
		if (branch) {
			branch		= b;
		}
	}
}