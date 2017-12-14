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
	protected Boolean verbose	= 1;
	protected Boolean dryRun	= false;
	protected Boolean failfast	= false;
	protected Boolean pkgDeps	= true;
	
	public plConfig(String name, Map params) {
		name		= name;
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
		verbose		= params.verbose;
		dryRun		= params.dryRun;
		failfast	= params.failFast;
		pkgDeps		= params.pkgDeps;
	}
}