## How to release

Use username/password of sonatype https://s01.oss.sonatype.org/ 

```shell
MAVEN_USERNAME={} MAVEN_PASSWORD={} ./gradlew build jar publish
```

## Generate gpg key

Generate gpg key with name and email

```shell
 gpg --gen-key 
```

Export keyring

```shell
 gpg --keyring secring.gpg --export-secret-keys > ~/.gnupg/secring.gpg
```

## Gradle properties

Gradle properties should looks something like

```shell
$ cat ~/.gradle/gradle.properties

signing.keyId=5DBFACD8
signing.password={passphrase}
signing.secretKeyRingFile=//Users/jsalinas/.gnupg/secring.gpg 
```

