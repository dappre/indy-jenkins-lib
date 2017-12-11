def loadAndDump(name, file=null) {
	file = file || name
	def preparePackage = libraryResource name
    writeFile file: file, text: preparePackage
    sh "chmod 777 $file"
}