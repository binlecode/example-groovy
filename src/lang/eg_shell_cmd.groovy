

// print out shell command output
// Note: the shell is responsible for expanding wildcard characters. 
// Therefore, explicitly including the shell in your command makes sense.
println 'bash -c ls -l *.groovy'.execute().text
// If you don't have wildcard, explicit sheel call can be omitted.
println 'ls -l'.execute().text