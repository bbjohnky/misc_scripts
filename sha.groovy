import java.security.MessageDigest

def rootDirectory = args[0]
def newDirectory = args[1]
def shaMap = [:]

println "Loading the list of files from $rootDirectory"
new File(rootDirectory).eachFileRecurse { file ->
	def sha = getSha(file, true)
	shaMap[sha] = true
}

println ""
println "Begin checking files in $newDirectory"
new File(newDirectory).eachFileRecurse { file ->
	def sha = getSha(file)
	if(!shaMap[sha]) {
		println "Did not find $file.name"
	}
}
	

def getSha(def f, def logging = false) {
	int KB = 1024
	int MB = 1024*KB
	//File f = new File(file)
	if (!f.exists() || !f.isFile()) {
	  println "Invalid file $f provided"
	  println "Usage: groovy sha1.groovy <file_to_hash>"
	}

	if(logging) {
		println "Calculating sha for $f.name"
	}
	def messageDigest = MessageDigest.getInstance("SHA1")

	f.eachByte(MB) { byte[] buf, int bytesRead ->
	  messageDigest.update(buf, 0, bytesRead);
	}

	def sha1Hex = new BigInteger(1, messageDigest.digest()).toString(16).padLeft( 40, '0' )
	return sha1Hex
}
