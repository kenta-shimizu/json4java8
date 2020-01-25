# json4java8

## Introduction
This library is JSON ([RFC8259](https://tools.ietf.org/html/rfc8259)) parser implementation on Java8.


## Convert

1. Create JsonHub instance from `#fromPojo`, `#fromJson`, `#readFile`, `#fromBytes`.
1. Convert using `#toPojo`, `#toJson`, `#writeFile`, `#getBytes`, `#writeBytes`.


### from POJO (Plain Old Java Objec) to JSON

```
/* to String */
String json = JsonHub.fromPojo(pojo).toJson();

/* to Writer */
Writer writer = new StringWriter()
JsonHub.fromPojo(pojo).toJson(writer);

/* to file */
Path path = Paths.get("path_of_file.json");
JsonHub.fromPojo(pojo).writeFile(path);
```

### from JSON to POJO

```
/* from JSON String */
String json = "{\"num\": 100, \"str\": \"STRING\", \"bool\": true}";
Pojo pojo = JsonHub.fromJson(json).toPojo(Pojo.class);

/* from Reader */
Reader reader = new StringReader(json);
Pojo pojo = JsonHub.fromJson(reader).toPojo(Pojo.class);

/* from file */
Path path = Paths.get("path_of_file.json");
Pojo pojo = JsonHub.readFile(path).toPojo(Pojo.class);
```

### from POJO to UTF-8 bytes

```
/* to bytes */
byte[] bytes = JsonHub.fromPojo(pojo).getBytes();

/* to OutputStream */
OutputStream strm = new ByteArrayOutputStream();
JsonHub.fromPojo(pojo).writeBytes(strm);
```

### from UTF-8 bytes to POJO
```
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

```
/* to String */
String prettyPrintJson = JsonHub.fromPoso(pojo).prettyPrint();

/* write to file */
Path path = Paths.get("path_of_file.json");
JsonHub.fromPojo(pojo).prettyPrint(path);

/* write to Writer */
Writer writer = new StringWriter();
JsonHub.fromPojo(pojo).prettyPrint(writer;
```

see also "example".


## Get Value

```
String json = "{
            "  \"num\":    100,
            "  \"str\":    \"STRING\",
            "  \"bool\":   true,
            "   \"array\":  [
                                "a",
                                "b",
                                "c"
                            ]
                ]
                }";

JsonHub jsonHub = JsonHub.fromJson(json);



```

### Methods for seek value

✓ is useable.

"blank" is throw JsonHubUnsupportedOperationException.

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
|containsKey() | ✓ | |  |  |  |  |  |
|getOrDefault(CharSequence)| ✓ | |  |  |  |  |  |
|getOrDefault(CharSequence, JsonHub)| ✓ | |  |  |  |  |  |

### Methods for get value

✓ is useable.

"blank" is throw JsonHubUnsupportedOperationException.

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

### Methods for judge type

✓ is useable.

"blank" is throw JsonHubUnsupportedOperationException.

| Method | Object | Array | Number | String | true | false | null |
|:--|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
|type() | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
|isObject() | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
|isArray() | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
|isNumber() | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
|isString() | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
|isTrue() | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
|isFalse() | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
|isNull() | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
|nonNull() | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |


See also ["/src/examples/example1/ExampleHttpGeneralServer.java"](/src/examples/example1/)


[^1]: Optional is empty

[^2]: number



## Create JsonHub instance by Builder

Use JsonHubBuilder

```
JsonHubBuilder jhb = JsonHub.getBuilder();

JsonHub jsonHub = jhb.object(
    jhb.pair("num", 100),
    jhb.pair("str", "STRING"),
    jhb.pair("bool", true),
    jhb.pair("array", jhb.array(
        jhb.build("a"),
        jhb.build("b"),
        jhb.build("c")
    )
);

String json = jsonHub.toJson();

System.out.println(json);

/* {} */
```



