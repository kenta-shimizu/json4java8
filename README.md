# json4java8

## Introduction

This library is JSON ([RFC8259](https://tools.ietf.org/html/rfc8259)) parser implementation on Java8. Also supports JSONPath, JSONC reading.

## Example of use

```java
public class POJO {
	
	public int num;
	public String str;
	public boolean bool;
	public List<String> array;
	
	public POJO() {
		num = 100;
		str = "STRING";
		bool = true;
		array = Arrays.asList("a", "b", "c");
	}
	
	public static void main(String[] args) {

		POJO pojo = new POJO();
		String json = JsonHub.fromPojo(pojo).toJson();
		System.out.println(json);

		/* {"num":100,"str":"STRING","bool":true,"array":["a","b","c"]} */
	}
}
```

[Javadoc](https://kenta-shimizu.github.io/json4java8/index.html)

## Convert

1. Create JsonHub instance from `#fromPojo`, `#fromJson`, `#fromFile`, `#fromBytes`.
1. Convert using `#toPojo`, `#toJson`, `#writeFile`, `#getBytes`, `#writeBytes`.


### From POJO (Plain Old Java Objec) to JSON

```java
/* to String */
String json = JsonHub.fromPojo(pojo).toJson();

/* to Writer */
Writer writer = new StringWriter()
JsonHub.fromPojo(pojo).toJson(writer);

/* to file */
Path path = Paths.get("path/of/file.json");
JsonHub.fromPojo(pojo).writeFile(path);
```

#### From POJO conditions

- Field is `public`
- Field is *not* `static`

See also ["/src/examples/example02/PojoParseToJsonString.java"](/src/examples/example02/PojoParseToJsonString.java)  
See also ["/src/examples/example03/PojoWriteJsonToFile.java"](/src/examples/example03/PojoWriteJsonToFile.java)


### From JSON to POJO

```java
/* from JSON String */
String json = "{\"num\": 100, \"str\": \"STRING\", \"bool\": true}";
Pojo pojo = JsonHub.fromJson(json).toPojo(Pojo.class);

/* from Reader */
Reader reader = new StringReader(json);
Pojo pojo = JsonHub.fromJson(reader).toPojo(Pojo.class);

/* from file */
Path path = Paths.get("path/of/file.json");
Pojo pojo = JsonHub.fromFile(path).toPojo(Pojo.class);
```

#### To POJO conditions

- Class has `public new()` (arguments is 0)
- Field is `public`
- Field is *not* `static`
- Field is *not* `final`

See also ["/src/examples/example01/JsonStringParseToPojo.java"](/src/examples/example01/JsonStringParseToPojo.java)  
See also ["/src/examples/example04/ReadJsonFileParseToPojo.java"](/src/examples/example04/ReadJsonFileParseToPojo.java)

### From POJO to UTF-8 bytes

```java
/* to bytes */
byte[] bytes = JsonHub.fromPojo(pojo).getBytes();

/* to OutputStream */
OutputStream strm = new ByteArrayOutputStream();
JsonHub.fromPojo(pojo).writeBytes(strm);
```

### From UTF-8 bytes to POJO

```java
/* from bytes */
byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
Pojo pojo = JsonHub.fromBytes(bytes).toPojo(Pojo.class);

/* from InputStream */
InputStream strm = new ByteArrayInputStream(bytes);
Pojo pojo = JsonHub.fromBytes(strm).toPojo(Pojo.class);
```

## Pretty Print

1. Create JsonHub instance from `#fromPojo`, ...
1. PrettyPrint using `#prettyPrint`

```java
/* to String */
String prettyPrintJson = JsonHub.fromPojo(pojo).prettyPrint();

/* write to file */
Path path = Paths.get("path/of/file.json");
JsonHub.fromPojo(pojo).prettyPrint(path);

/* write to Writer */
Writer writer = new StringWriter();
JsonHub.fromPojo(pojo).prettyPrint(writer);
```

See also ["/src/examples/example05/PojoParseToPrettyPrintJsonString.java"](/src/examples/example05/PojoParseToPrettyPrintJsonString.java)  
See also ["/src/examples/example06/PojoWritePrettyPrintJsonToFile.java"](/src/examples/example06/PojoWritePrettyPrintJsonToFile.java)  
See also ["/src/examples/example09/ChangePrettyPrintFormat.java"](/src/examples/example09/ChangePrettyPrintFormat.java)

## Get value from JsonHub instance

```java
String json
= "{                                 "
+ "  \"num\":   100,                 "
+ "  \"str\":   \"STRING\",          "
+ "  \"bool\":  true,                "
+ "  \"array\": [\"a\", \"b\", \"c\"]"
+ "}                                 ";

JsonHub jh = JsonHub.fromJson(json);

int num = jh.get("num").intValue();  /* 100 */
String str = jh.get("str").toString();  /* "STRING" */
boolean bool = jh.get("bool").booleanValue();  /* true */
String array_0 = jh.get("array").get(0).toString();  /* "a" */
```

See also ["/src/examples/example08/ForEachJsonHub.java"](/src/examples/example08/ForEachJsonHub.java)

### Methods for seek value in OBJECT or ARRAY

✓ is available.  
"blank" is throw `JsonHubUnsupportedOperationException`.

| Method | Object | Array | Number | String | true | false | null |
|:--|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
|get(CharSequence) | ✓ |  |  |  |  |  |  |  |
|get(String...)| ✓ |  |  |  |  |  |  |  |
|get(int)|  | ✓ |  |  |  |  |  |  |  |
|iterator() | ✓ | ✓ |  |  |  |  |  |
|stream() | ✓ | ✓ |  |  |  |  |  |
|forEach(Consumer<? super JsonHub)| ✓ | ✓ |  |  |  |  |  |
|forEach(BiConsumer<? super JsonString, ? super JsonHub)| ✓ | ✓ |  |  |  |  |  |
|values() | ✓ | ✓ |  |  |  |  |  |
|keySet() | ✓ | |  |  |  |  |  |
|containsKey(CharSequence) | ✓ | |  |  |  |  |  |
|getOrDefault(CharSequence)| ✓ | |  |  |  |  |  |
|getOrDefault(CharSequence, JsonHub)| ✓ | |  |  |  |  |  |

### Methods for get value

✓ is available.  
"blank" is throw `JsonHubUnsupportedOperationException`.

| Method | Object | Array | Number | String | true | false | null |
|:--|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
|intValue() |  |  | ✓ |  |  |  |  |
|longValue() |  | | ✓ |  |  |  |  |
|doubleValue() |  | | ✓ |  |  |  |  |
|booleanValue() |  | |  |  | ✓ | ✓ |  |
|optionalInt() | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
|optionalLong() | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
|optionalDouble() | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
|optionalBoolean() | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
|optionalNumber() | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
|optionalString() | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
|length() | ✓ | ✓ |  | ✓ |  |  |  |
|isEmpty() | ✓ | ✓ |  | ✓ |  |  |  |
|toString() | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |

### Methods for check type

✓ is available.

| Method | Object | Array | Number | String | true | false | null |
|:--|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
|type() | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
|isObject() | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
|isArray() | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
|isNumber() | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
|isString() | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
|isTrue() | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
|isFalse() | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
|isBoolean() | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
|isNull() | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
|nonNull() | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |

## Create JsonHub instance by builder

Use JsonHubBuilder

```java
JsonHubBuilder jhb = JsonHub.getBuilder();

JsonHub jh = jhb.object(
    jhb.pair("str", "STRING"),
    jhb.pair("num", 100),
    jhb.pair("bool", true),
    jhb.pair("array", jhb.array(
        jhb.build("a"),
        jhb.build("b"),
        jhb.build("c")
    ))
);

String json = jh.toJson();

System.out.println(json);

/* {"str":"STRING","num":100,"bool":true,"array":["a","b","c"]} */
```

See also ["/src/examples/example07/CreateJsonStringByBuilder.java"](/src/examples/example07/CreateJsonStringByBuilder.java)

## JSONPath

JSONPath is ["https://goessner.net/articles/JsonPath/"](https://goessner.net/articles/JsonPath/)

### Supports

| Operator | Description |
|:--|:--|
|`$`|The root element.|
|`*`|Wildcard, all object-name or array-number.|
|`..`|Recursive descent.|
|`.<name>`|Child object name operator.|
|`[<name>(, <name>)]`|Child object name(s) operator.|
|`[<number>(, <number>)]`|Child array number(s) operator.|
|`[start:end:step]`|Child array slice operator.|

Not support `@`, `?()`, `()`

```java
List<JsonHub> results = jh.jsonPath("$.store.book[*].author");
```

See also ["/src/examples/example11/JsonPath.java"](/src/examples/example11/JsonPath.java)

## JSONC reading

JSONC (JSON with comments) support.

- /* comment... */
- // comment...
- Array trailing comma(,)
- Object trailing comma(,)

```java
Path path = Paths.get("path/of/file.jsonc");
JsonHub jh = JsoncReader.fromFile(path);
System.out.println(jh.prettyPrint());
```

See also ["/src/examples/example10/ReadJsoncFile.java"](/src/examples/example10/ReadJsoncFile.java)
