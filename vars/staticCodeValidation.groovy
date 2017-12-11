def call() {
    try {
        echo 'Static code validation'
        checkout scm

        libraryHelpers.loadAndDump('code-validation.dockerfile')

        def validationEnv = dockerHelpers.build('code-validation', 'code-validation.dockerfile .')

        validationEnv.inside {
        	  // REMOVE --exit-zero when all errors are fixed
            helpers.shell('python3 -m flake8')
        }
    }
    finally {
        echo 'Static code validation: Cleanup'
        step([$class: 'WsCleanup'])
    }
}
