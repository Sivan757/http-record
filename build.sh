native-image --no-fallback --enable-all-security-services --report-unsupported-elements-at-runtime \
--install-exit-handlers --allow-incomplete-classpath --initialize-at-build-time=io.ktor,kotlinx,kotlin,org\
--target=debian-arm64 -H:+ReportUnsupportedElementsAtRuntime -H:+ReportExceptionStackTraces \
-H:ReflectionConfigurationFiles=./reflection.json -cp ./build/libs/http-record-0.0.1-all.jar \
-H:Class=cn.kapukapu.ApplicationKt -H:Name=http-record
