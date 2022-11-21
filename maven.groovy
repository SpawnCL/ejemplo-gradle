def maven_compile()
	{
		sh 'mvn clean compile -e'
    	}
def mavel_test()
	{
		sh 'mvn clean test -e'
	}
def mavel_package()
	{
		sh 'mvn clean package -e'
	}

return this
