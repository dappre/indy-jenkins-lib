import groovy.transform.Field  
@Field sourcePackages = ['indy-node', 'sovrin', 'indy-plenum', 'indy-anoncreds']

@NonCPS
def extractVersionFromText(match, text) {
    def pattern = /.*(${match}[-a-z=\\.0-9]*)'/
    def matcher = (text =~ pattern)
    return matcher[0][1]
}

@NonCPS
def dropVersion(text) {
    def pattern = /([-a-z0-9]+)==.*/
    def matcher = (text =~ pattern)
    return matcher[0][1]
}

@NonCPS
def getRepoDetails(githubUrl) {
    def pattern = /github.com\/([^\/]+)\/([^\/]+)(\/|\.git)/
    def matcher = (githubUrl =~ pattern)
    return matcher.asBoolean() ? ["owner": matcher[0][1], "repo": matcher[0][2]] : null
}

@NonCPS
def entries(m) {m.collect {k, v -> [k, v]}}

@NonCPS
def uniqValues(m) {(m.collect{k,v->v}.unique{a,b->a<=>b})}

@NonCPS
def listToMap(l, value) {
    return l.collectEntries {item -> [item, value]}
}

def sliceListAfter(l, condition) {
    def ret = []
    def toAdd = false
    for(def item : l) {
        if (condition(item)) {
            toAdd = true
        }
        if (toAdd) {
            ret.add(item)
        }
    }
    return ret
}

def shell(command) {
    if (isUnix()) {
        sh command
    } else {
        bat command
    }
}

Map mergeMapsWith(Map originalMap, Map newMap, Closure predicate){
    Map ret = [:]
    ret << originalMap
    for(def item in originalMap) {
        def newItem = newMap.get(item.key, null)
        if (newItem) {
            ret[item.key] = predicate(item.value, newItem)
        }
    }
    for(def item in newMap) {
        def exists = ret.get(item.key, null)
        if (!exists) {
            ret[item.key] = item.value
        }
    }
    return ret
}

def dropPostfixesInFile(text, file='setup.py') {
    sh "sed -ir \"s/$text[-a-z]/$text/g\" $file"
}

def dropVersionsInFile(text, file='setup.py') {
    sh "sed -ir \"s/$text[=0-9\\.]*/$text/g\" $file"
}

def extractVersion(match, file='setup.py', strict=false) {
    def versionPostfixMatcher = '[-a-z=\\.0-9]*'
    if (strict) {
        versionPostfixMatcher = '[-a-z0-9]*==[0-9\\.]*'   
    }
    def text = sh(returnStdout: true, script: "grep \"${match}${versionPostfixMatcher}'\" ${file}").trim()
    echo "${match}Version -> matching against ${text}"
    return extractVersionFromText(match, text)
}

def getUserUid() {
    return sh(returnStdout: true, script: 'id -u').trim()
}

def functionOrMissingStub(map, function) {
    return map.get(function, {
        echo "TODO: Implement $function"
    })
}

def safeWinPath(path) {
    return path.replace('\\', '/')
}

def downloadPackageAndProcess(target, processFn) {
    sh "rm -rf /home/sovrin/tmp@${target}"
    sh "mkdir -p /home/sovrin/tmp@${target}"
    sh "pip download -b /home/sovrin/tmp@${target} --no-clean ${target}"

    def packageWithoutVersion = dropVersion("${target}")
    def ret = processFn("/home/sovrin/tmp@${target}/${packageWithoutVersion}/setup.py")
    sh "rm -rf /home/sovrin/tmp@${target}"
    return ret
}

def extractVersionOfSubdependency(master, subdependency) {
    return downloadPackageAndProcess(master, {file -> extractVersion(subdependency, file)})
}

def updatePostfix(replacePostfixMap=[:], file='setup.py') {
    def originalPostfix = replacePostfixMap.get('original', null)
    def newPostfix = replacePostfixMap.get('new', null)
    if (originalPostfix != null && newPostfix != null) {
        for (def targetPackage : sourcePackages) {
            sh "sed -i -r \"s/${targetPackage}${originalPostfix}/${targetPackage}${newPostfix}/g\" ${file}"
            if (newPostfix) {
                sh "sed -i -r \"s/SETUP_DIRNAME, '${targetPackage}${newPostfix}/SETUP_DIRNAME, '${targetPackage}${originalPostfix}/g\" ${file}"
            }
        }
    }
}

def getDependencies(name, version, file='setup.py') {
    def dependencies = ['indy-node': null, 'sovrin': null]
    def subdependencies = [:]
    if (dependencies.containsKey(name)) {
        dependencies[name] = version
    }
    subdependencies << dependencies
    echo "Depenedencies ${dependencies}, subdependencies ${subdependencies}"
    for (def targetPackage : sourcePackages) {
        if (dependencies.containsKey(targetPackage)) {
            continue
        }
        dependencies[targetPackage] = null
        try {
            def packageWithVersion = extractVersion(targetPackage, file, true)
            def splitOfPackageWithVersion = packageWithVersion.split('==')
            dependencies[targetPackage] = splitOfPackageWithVersion[1]

            def packageSubdependencies = downloadPackageAndProcess(packageWithVersion, {depFile -> getDependencies(name, version, depFile)})
            subdependencies = mergeMapsWith(subdependencies, packageSubdependencies, {a, b -> a ? a : b})
        }
        catch(e) {
            //Enable for debug
            //echo "${e}"
        }
    }
    dependencies = mergeMapsWith(dependencies, subdependencies, {a, b -> a ? a : b})
    echo "Depenedencies to return ${dependencies}"
    return dependencies
}

//For future endeavors
void setBuildStatus(String owner, String repo, String commit_sha, String message, String state) {
  step([
      $class: "GitHubCommitStatusSetter",
      reposSource: [$class: "ManuallyEnteredRepositorySource", url: "https://github.com/$owner/$repo"],
      commitShaSource: [$class: "ManuallyEnteredShaSource", sha: "$commit_sha"],
      contextSource: [$class: "ManuallyEnteredCommitContextSource", context: "ci/jenkins/build-status"],
      errorHandlers: [[$class: "ChangingBuildStatusErrorHandler", result: "UNSTABLE"]],
      statusResultSource: [ $class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]] ]
  ]);
}

boolean isCollectionOrArray(object) {    
    [Collection, Object[]].any { it.isAssignableFrom(object.getClass()) }
}
