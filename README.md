# Vesting

## Running (Make)
Make sure you have the `JDK_11` installed.

If you didn't it already, run the following command:
```shell
$ make bundle
```

And then:
```shell
$ make run file_path=<FILE_PATH> target_date=<TARGET_DATE> precision=<PRECISION>
```

Example:
```shell
$ make run file_path=/home/lucas/Documents/vest_entries.csv target_date=2020-04-01 precision=2
```

## Running (Gradle)
Make sure you have the `JDK_11` installed.

Just execute the following command:
```shell
$ ./gradlew run --args="<FILE_PATH> <TARGET_DATE> <PRECISION>" -q
```

Example:
```shell
$ ./gradlew run --args="/home/lucas/Documents/vest_entries.csv 2020-01-01 3" -q
```

## Running (Docker)
First, build the docker image:
```shell
$ docker build . -t vesting
```

Then, run the following command (which is not pretty straightforward, because you need to bind an external file to your docker container):
```shell
docker run --mount type=bind,source=<FULL_PATH>/<FILENAME>,target=/app/<FILENAME> -t vesting /app/<FILENAME> 2020-01-01 5
```

Example:
```shell
$ docker run --mount type=bind,source=/home/lucas/Documents/vest_entries.csv,target=/app/vest_entries.csv -t vesting /app/vest_entries.csv 2020-01-01 1
```

## Executing tests (Gradle)
All tests (with coverage verification):
```shell
./gradlew test
```

Unit tests:
```shell
./gradlew unittest
```

Integration tests:
```shell
./gradlew integrationtest
```

## Design Decisions

- This project has 91% of test coverage
- I decided to use Kotlin as the programming language just because I'm working with it right now and I like strong typed languages :P
- This project tries to follow the Uncle Bob's Clean Architecture principles
  - Allowing each layer to access only its subsequent (or sibling) layer would allow me to include other external layers in the future, like Web and Persistence.
  - Also, the "screaming architecture" helps other people to understand what each layer does. 
- Functional programming principles
  - I'm using only immutable data structures
  - Most of the functions are referentially transparent
  - This decision would allow me to avoid some mutable errors; also if I want to do some parallel programming I'd not have to deal with race condition and some other problems
  - I'm avoiding side effects (I'm using them just at the infra and Main level)
- SOLID principles
  - Specially the Single Responsibility Principle and Dependency Inversion Principle (all dependencies was set in the "terrible Main file")
  - I confess that I broke the Interface Segregation Principle on `FailFastVestRequestEventParser` class, but in this case I prefer to refactor it when the need arises (when there is other rules to validate the fields, for example)
- The most of the libs added on `build.gradle` are related to tests. But I also included a lib to read csv files and avoid the need to dealing with extra boilerplate
- I'm using `BigDecimal` for not losing the quantity precision
- I'm using the `BufferedInputStream` to read chunks of bytes until 2Gb. This would allow the processing of larger files and save some memory
- I've decided to not implement this software in an async way, since that opening a file would be the only "expensive" operation, and I'm reading just one file

### Questions about the program behavior
- What happens when receiving a file with some errored lines? A: For the sake of simplicity, I decided to skip those lines and work only with the succeeded ones.

### What I'd like to improve?
To not overrun the time-box, I decided to keep the project as simple as I could. But below I list some features that I'd like to implement:
- Use `ArchUnit` to test the software architecture and guarantee that its evolution will not break the Clean Architecture
- Apply some stress test using files with more than 2Gb in size
- Create an output file with all lines that was not processed and specify all errors in it
- The ability to process multiple files in an asynchronous way (including the events into a Queue)
- Add some logs and profiling
- Wrap the docker run with Makefile functions, since the command to use docker is a way big
- Using git, I'd like to follow [this guideline](https://www.conventionalcommits.org/en/v1.0.0/) for semantic commit messages
- Add a SonarQube to evaluate the quality of the code
- Set a maximum value to JVM memory

Thanks for sending me this test, I had a lot of fun doing it :)