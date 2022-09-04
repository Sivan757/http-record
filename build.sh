native-image --no-fallback --report-unsupported-elements-at-runtime \
--install-exit-handlers --initialize-at-build-time=io.ktor,kotlinx,kotlin,ch.qos.logback \
-H:+ReportUnsupportedElementsAtRuntime -H:+ReportExceptionStackTraces \
-H:ReflectionConfigurationFiles=reflection.json -jar ./build/libs/http-record-1.1.0-all.jar

