plugins {
    base
    id("test-report-aggregation")
    `jvm-test-suite`
}

dependencies {
    testReportAggregation(project(":parlio-graphql-api-core"))
    testReportAggregation(project(":parlio-tweede-kamer-graphql-api"))
}

reporting {
    reports {
        val testAggregateTestReport by creating(AggregateTestReport::class) {
            testType.set(TestSuiteType.UNIT_TEST)
        }
        val integrationTestAggregateTestReport by creating(AggregateTestReport::class) {
            testType.set(TestSuiteType.INTEGRATION_TEST)
        }
    }
}

tasks.check {
    dependsOn(tasks.named<TestReport>("testAggregateTestReport"))
    dependsOn(tasks.named<TestReport>("integrationTestAggregateTestReport"))
}