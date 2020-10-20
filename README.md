# Feature Flags for JAVA

#### This project is for toggle off/on of java features.

**Follow steps:**
1 . Add below dependency to pom.xml of your project
```java
<dependency>
		<groupId>io.github.akayibrahim</groupId>
		<artifactId>featureflags</artifactId>
		<version>1.0.0</version>
</dependency>
```
2 . Add below import annotation to your spring boot main class.
`@Import(FeatureFlagsAspect.class)`

Example usage:
```java
@SpringBootApplication
@Import(FeatureFlagsAspect.class)
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```
3 . Now you are ready. You can add FeatureFlags annotation to your methods like below.
```java
@FeatureFlags(enabled = "featureFlags.demo")
@Override
public void demo() {
	System.out.println("Demo Feature Flags");
}
```
4 . Don't forget add toggle parameter to your config file. (application.properties / application.yml)
```yaml
featureFlags:
  demo: true
```
