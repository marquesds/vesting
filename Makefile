.SILENT: run

bundle:
	./gradlew uberJar

run:
	java -jar build/libs/vesting-1.0-uber.jar $(file_path) $(target_date) $(precision)