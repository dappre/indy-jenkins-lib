#!/usr/bin/env groovy

/*
 * This work is protected under copyright law in the Kingdom of
 * The Netherlands. The rules of the Berne Convention for the
 * Protection of Literary and Artistic Works apply.
 * Digital Me B.V. is the copyright owner.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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