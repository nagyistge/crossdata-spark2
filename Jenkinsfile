@Library('libpipelines@master') _

hose {
    MAIL = 'crossdata'
    SLACKTEAM = 'stratiocrossdata'
    MODULE = 'crossdata'
    REPOSITORY = 'crossdata-spark2'
    DEVTIMEOUT = 65
    RELEASETIMEOUT = 50
    MAXITRETRIES = 2
    EXPOSED_PORTS = [13420,13422]
    
    UT_FLAKINESS_PERCENTAGE = 0
    UT_NEW_FLAKINESS_PERCENTAGE = 0
    IT_FLAKINESS_PERCENTAGE = 0
    IT_NEW_FLAKINESS_PERCENTAGE = 0
    AT_FLAKINESS_PERCENTAGE = 0
    AT_NEW_FLAKINESS_PERCENTAGE = 0

    ITSERVICES = [
        ['ZOOKEEPER': [
            'image': 'jplock/zookeeper:3.5.2-alpha'],
            'sleep': 30,
            'healthcheck': 2181
            ]
    ]

    ITPARAMETERS = """
        |    -DCROSSDATA_CORE_CATALOG_ZOOKEEPER_CONNECTION_STRING=%%ZOOKEEPER:2181
        | """

    DEV = { config ->
        doCompile(conf: config, crossbuild: 'scala-2.11')

        parallel(UT: {
            doUT(conf: config, crossbuild: 'scala-2.11')
        }, IT: {
            doIT(conf: config, crossbuild: 'scala-2.11')
        }, failFast: config.FAILFAST)

        doPackage(conf: config, crossbuild: 'scala-2.11')

        // TODO: Restore doDocker when all modules migrated
        parallel(DOC: {
           doDoc(conf: config, crossbuild: 'scala-2.11')
        }, DEPLOY: {
            doDeploy(conf: config, crossbuild: 'scala-2.11')
        }, failFast: config.FAILFAST)

        // doAT(conf: config, groups: ['micro-cassandra']) //TODO: Restore when all modules migrated
     }
}
