# RI: Reflection Invoker

![image](https://user-images.githubusercontent.com/19273732/104975331-46636500-59af-11eb-935d-cbeb7852ed3c.png)

Simple, Easy & Powerful Java Reflection utility for invoking methods.

Invoke Java static methods, class methods and constructors using Java reflection at runtime using one schema.

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

### How to invoke static method using Java Reflection Invoker

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
### How to invoke a constructor using Java Reflection Invoker

Below example shows how to constructor Date object by calling constructor using Java Reflection Invoker

```java
Target target = new Target();
target.setClazz("java.util.Date");
target.setMethod("Date");
target.setMethodArgs(ImmutableList.of(1610996650749L));
target.setMethodArgsType(ImmutableList.of("long"));

ReflectionInvoker invoker = new Invoker();
Date date = (Date) invoker.invoke(target);
```

### How to invoke class method using Java Reflection Invoker

```java
Target target = new Target();
target.setClazz("java.util.Date");
target.setMethod("toString");
target.setMethodArgs(ImmutableList.of());
target.setMethodArgsType(ImmutableList.of());
target.setClazzInstance(date);

ReflectionInvoker invoker = new Invoker();
String dateToString = (String) invoker.invoke(target);
```

Class instance can be passed as Target type as well. RI will take care of creating class instance before invoking
method

```java
Target dateInstance = new Target();
dateInstance.setClazz("java.util.Date");
dateInstance.setMethod("Date");
dateInstance.setMethodArgs(ImmutableList.of(1610996650749L));
dateInstance.setMethodArgsType(ImmutableList.of("long"));

Target target = new Target();
target.setClazz("java.util.Date");
target.setMethod("toString");
target.setMethodArgs(ImmutableList.of());
target.setMethodArgsType(ImmutableList.of());
target.setClazzInstance(dateInstance);

ReflectionInvoker invoker = new Invoker();
String dateToString = invoker.invoke(target);
```


### Chaining Executor: How to invoke multiple methods using Java Reflection Invoker

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

### How to invoke methods using Java Reflection Invoker represented by JSON configuration

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