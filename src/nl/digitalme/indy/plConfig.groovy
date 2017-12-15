#!/usr/bin/env groovy

package nl.digitalme.indy

class plConfig implements Serializable {
	// Project name
	protected String name;
	// Flag for the extended lib support
	protected Boolean extended = false;
	// Stages
	protected ArrayList dists;
	protected ArrayList stages;
	protected Map labels = [
		docker:		'docker',
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
		dists		= p.listDists.split("\n")
		stages		= p.listStages.split("\n")
		labels = [
			docker:		p.labelDocker,
		];
		verbose		= p.verbose as Integer;
		dryRun		= p.dryRun;
		failfast	= p.failFast;
		pkgDeps		= p.pkgDeps;
		if (b) {
			branch		= b;
		}
	}
}