def maven_compile()
	{
		sh './mvnw clean compile -e'
    	}
def mavel_test()
	{
		sh './mvnw clean test -e'
	}
def mavel_package()
	{
		sh './mvnw clean package -e'
	}

return this
