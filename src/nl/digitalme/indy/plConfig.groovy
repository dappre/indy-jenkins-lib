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
			validate:	!p.skipValidate,
			compile:	!p.skipCompile,
			test:		!p.skipTest,
			package:	!p.skipPackage,
			approve:	!p.skipApprove,
			release:	!p.skipRelease,
			deliver:	!p.skipDeliver,
			notify:		!p.skipNotify,
		];
		lb = [
			docker:		p.lbDocker,
		];
//		if (p.verbose.isInteger()) {
			verbose		= p.verbose as Integer;
//		}
		dryRun		= p.dryRun;
		failfast	= p.failFast;
		pkgDeps		= p.pkgDeps;
		if (b) {
			branch		= b;
		}
	}
}