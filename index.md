# RI: Reflection Invoker

Simple & easy to use Java Reflection utility for invoking static methods, class methods, constructors.
 RI lifts heavy redundant Java Reflection generic code, data type translation, primitive data type handling, wraps 
 exceptions, exposes builder and utilities to statically and/or dynamically define targets to be invoked.

Reflection Invoker provides framework for extending method invoking from user configuration. Built-in support for JSON
schema and reader. Provision for user to extend schema in JSON, YAML or other configuration language and implement its
 reader.

## How to use 
### Add to Maven project dependency
```xml
<dependency>
    <groupId>io.github.jay-g-mehta</groupId>
    <artifactId>reflectioninvoker</artifactId>
    <version>0.0.2</version>
</dependency>
```

## Usage

- Simple invoking static method
```java
// i. Define what is to be invoked
Target target = new Target();
target.setClazz("org.apache.commons.lang3.ArrayUtils");
target.setMethod("contains");
target.setMethodArgs(ImmutableList.of(ImmutableList.of("Mumbai", "Paris", "Tokyo"), "Helsinki"));
target.setMethodArgsType(ImmutableList.of("[Ljava.lang.Object;", "java.lang.Object"));

// ii. Invoke & get results
ReflectionInvoker invoker = new Invoker();
Boolean actual = (Boolean) invoker.invoke(target);
```

- Chaining Executor: Invoke multiple static methods

```java

// i. Define first target what is to be invoked
Target targetInstance = new Target();
targetInstance.setClazz("org.apache.commons.lang3.ArrayUtils");
targetInstance.setMethod("contains");
targetInstance.setMethodArgs(ImmutableList.of(ImmutableList.of("Mumbai", "Paris", "Tokyo"), "Helsinki"));
targetInstance.setMethodArgsType(ImmutableList.of("[Ljava.lang.Object;", "java.lang.Object"));

// ii. Define more targets what is to be invoked
Target targetInstance2 = new Target();
targetInstance2.setClazz("org.apache.commons.collections4.CollectionUtils");
targetInstance2.setMethod("containsAny");
targetInstance2.setMethodArgs(ImmutableList.of(ImmutableList.of("Tesla", "Einstein", "Homi Bhabha"),
        ImmutableList.of("Messi")));
targetInstance2.setMethodArgsType(ImmutableList.of("java.util.Collection", "java.util.Collection"));

// iii. Chain targets
TargetsProvider targetsProvider = BuildTargetsProvider.builder()
        .target(targetInstance)
        .target(targetInstance2)
        .build();

// iv. Define Chain executor
ReflectionInvoker invoker = new Invoker();
SerialSynchronousReflectionInvokerExecutor serialSynchronousReflectionInvokerExecutor =
        new SerialSynchronousReflectionInvokerExecutor(invoker);

// v. Invoke & get result
List<Object> actual = serialSynchronousReflectionInvokerExecutor.execute(targetsProvider);
```

- Define Targets using JSON configuration

Define methods to invoke by reflection via built-in JSON configuration schema:
```json
[{
  "clazz": "org.apache.commons.lang3.ArrayUtils",
  "method": "contains",
  "methodArgs": [
    ["Mumbai", "Paris", "Tokyo"], "Helsinki"
  ],
  "methodArgsType": ["[Ljava.lang.Object;", "java.lang.Object"]
}]
```
Invoke all methods defined in JSON configuration using generic code:
```java
// i. Define reader above json config
TargetsConfigReader reader = new FileSystemConfigReader("UTF-8", "/path/to/config.json");
// ii. Define translator to deserialize config to Target object
TargetsConfigTranslator translator = new TargetsJsonTranslator(new ObjectMapper());
// iii. Define and build config targets provider
ConfigTargetsProvider configTargetsProvider = new ConfigTargetsProvider(reader, translator);
configTargetsProvider.build();
// iv. Define Chain executor
ReflectionInvoker invoker = new Invoker();
SerialSynchronousReflectionInvokerExecutor serialSynchronousReflectionInvokerExecutor =
        new SerialSynchronousReflectionInvokerExecutor(invoker);
// v. Invoke & get result
List<Object> actual = serialSynchronousReflectionInvokerExecutor.execute(configTargetsProvider);
```
